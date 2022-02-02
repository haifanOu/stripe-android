package com.stripe.android.identity.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.stripe.android.camera.CameraPreviewImage
import com.stripe.android.camera.framework.Analyzer
import com.stripe.android.camera.framework.AnalyzerFactory
import com.stripe.android.camera.framework.image.cropCameraPreviewToSquare
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.io.FileInputStream
import java.nio.channels.FileChannel

/**
 * Result category of IDDetector
 */
enum class Category { NO_ID, PASSPORT, ID_FRONT, ID_BACK, INVALID }

/**
 * Result bounding box coordinates of IDDetector, in percentage values with regard to original image's width/height
 */
data class BoundingBox(
    val top: Float,
    val left: Float,
    val width: Float,
    val height: Float,
)

/**
 * Analyzer to run a model input.
 * TODO(ccen): reimplement with ImageClassifier
 */
internal class IDDetectorAnalyzer(context: Context) :
    Analyzer<IDDetectorAnalyzer.Input, IDDetectorAnalyzer.State, IDDetectorAnalyzer.Output> {

    private val tfliteInterpreter = Interpreter(
        context.assets.openFd(modelName).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { input ->
                input.channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fileDescriptor.startOffset,
                    fileDescriptor.declaredLength
                )
            }
        }
    )

    /**
     * Input from CameraAdapter, note: the bitmap should already be encoded in RGB value
     */
    data class Input(val cameraPreviewImage: CameraPreviewImage<Bitmap>, val viewFinderBounds: Rect)

    data class State(val value: Int)

    /**
     * Output the category with highest score and the bounding box
     */
    data class Output(val boundingBox: BoundingBox, val category: Category, val score: Float)

    override suspend fun analyze(data: Input, state: State): Output {
        var tensorImage = TensorImage(INPUT_TENSOR_TYPE)
        val croppedImage = cropCameraPreviewToSquare(
            data.cameraPreviewImage.image,
            data.cameraPreviewImage.viewBounds,
            data.viewFinderBounds
        )

        tensorImage.load(croppedImage)

        // preprocess - resize the image to model input
        val imageProcessor =
            ImageProcessor.Builder().add(
                ResizeOp(INPUT_HEIGHT, INPUT_WIDTH, ResizeOp.ResizeMethod.BILINEAR)
            ).add(
                NormalizeOp(NORMALIZE_MEAN, NORMALIZE_STD) // normalize to (-1, 1)
            )
                .build() // add nomalization
        tensorImage = imageProcessor.process(tensorImage)

        // inference - input: (1, 224, 224, 3), output: (1, 4), (1, 5)
        val boundingBoxes = Array(1) { FloatArray(OUTPUT_BOUNDING_BOX_TENSOR_SIZE) }
        val categories = Array(1) { FloatArray(OUTPUT_CATEGORY_TENSOR_SIZE) }
        tfliteInterpreter.runForMultipleInputsOutputs(
            arrayOf(tensorImage.buffer),
            mapOf(
                OUTPUT_BOUNDING_BOX_TENSOR_INDEX to boundingBoxes,
                OUTPUT_CATEGORY_TENSOR_INDEX to categories,
            )
        )

        // find the category with highest score and build output
        val resultIndex = requireNotNull(categories[0].indices.maxByOrNull { categories[0][it] })

        if (categories[0][resultIndex] > THRESHOLD) {
            Log.d(
                TAG,
                "IDDetectorAnalyzer::analyze result: ${requireNotNull(INDEX_CATEGORY_MAP[resultIndex])} - ${categories[0][resultIndex]}"
            )

            Log.d(
                TAG,
                "IDDetectorAnalyzer::bounding box: (${boundingBoxes[0][0]}, ${boundingBoxes[0][1]}) - (${boundingBoxes[0][2]}, ${boundingBoxes[0][3]})"
            )
        } else {
            Log.d(
                TAG, "no_result"
            )
        }

        return Output(
            BoundingBox(
                boundingBoxes[0][0],
                boundingBoxes[0][1],
                boundingBoxes[0][2],
                boundingBoxes[0][3],
            ),
            requireNotNull(INDEX_CATEGORY_MAP[resultIndex]),
            categories[0][resultIndex]
        )
    }

    // TODO(ccen): check if we should enable this to track stats
    override val statsName: String? = null

    internal class Factory(
        private val context: Context
    ) : AnalyzerFactory<
            Input,
            State,
            Output,
            Analyzer<Input, State, Output>
            > {
        override suspend fun newInstance(): Analyzer<Input, State, Output>? {
            return IDDetectorAnalyzer(context)
        }
    }

    private companion object {
        const val INPUT_WIDTH = 224
        const val INPUT_HEIGHT = 224
        const val NORMALIZE_MEAN = 127.5f
        const val NORMALIZE_STD = 127.5f
        const val modelName = "2022IDDetectorWithoutMetadata.tflite"
        const val THRESHOLD = 0.4f
        const val OUTPUT_BOUNDING_BOX_TENSOR_INDEX = 0
        const val OUTPUT_CATEGORY_TENSOR_INDEX = 1
        const val OUTPUT_BOUNDING_BOX_TENSOR_SIZE = 4
        val INPUT_TENSOR_TYPE: DataType = DataType.FLOAT32
        val OUTPUT_CATEGORY_TENSOR_SIZE = Category.values().size
        val INDEX_CATEGORY_MAP = mapOf(
            0 to Category.NO_ID,
            1 to Category.PASSPORT,
            2 to Category.ID_FRONT,
            3 to Category.ID_BACK,
            4 to Category.INVALID,
        )
        val TAG: String = IDDetectorAnalyzer::class.java.simpleName
    }
}
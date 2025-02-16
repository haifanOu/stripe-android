package com.stripe.android.identity.camera

import com.stripe.android.camera.framework.AggregateResultListener
import com.stripe.android.camera.framework.ResultAggregator
import com.stripe.android.camera.framework.time.Clock
import com.stripe.android.camera.framework.time.milliseconds
import com.stripe.android.identity.ml.AnalyzerInput
import com.stripe.android.identity.ml.AnalyzerOutput
import com.stripe.android.identity.networking.models.VerificationPage
import com.stripe.android.identity.states.IOUTransitioner
import com.stripe.android.identity.states.IdentityScanState

internal class IDDetectorAggregator(
    identityScanType: IdentityScanState.ScanType,
    aggregateResultListener: AggregateResultListener<InterimResult, FinalResult>,
    verificationPage: VerificationPage
) : ResultAggregator<
    AnalyzerInput,
    IdentityScanState,
    AnalyzerOutput,
    IDDetectorAggregator.InterimResult,
    IDDetectorAggregator.FinalResult
    >(
    aggregateResultListener,
    IdentityScanState.Initial(
        type = identityScanType,
        timeoutAt = Clock.markNow() + verificationPage.documentCapture.autocaptureTimeout.milliseconds,
        transitioner = IOUTransitioner(
            iouThreshold = verificationPage.documentCapture.motionBlurMinIou,
            timeRequired = verificationPage.documentCapture.motionBlurMinDuration
        )
    ),
    statsName = null
) {
    private var isFirstResultReceived = false

    internal data class InterimResult(
        val identityState: IdentityScanState
    )

    internal data class FinalResult(
        val frame: AnalyzerInput,
        val result: AnalyzerOutput,
        val identityState: IdentityScanState
    )

    override suspend fun aggregateResult(
        frame: AnalyzerInput,
        result: AnalyzerOutput
    ): Pair<InterimResult, FinalResult?> {
        if (isFirstResultReceived) {
            val previousState = state
            state = previousState.consumeTransition(result)
            val interimResult = InterimResult(state)
            return if (state.isFinal) {
                interimResult to FinalResult(frame, result, state)
            } else {
                interimResult to null
            }
        } else {
            // If this is the very first result, don't transition state and post InterimResult with
            // current state(IdentityScanState.Initial).
            // This makes sure the receiver always receives IdentityScanState.Initial as the first
            // callback.
            isFirstResultReceived = true
            return InterimResult(state) to null
        }
    }
}

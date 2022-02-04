package com.stripe.android.ui.core.elements

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CardDetailsElementTest {
    @Test
    fun `test form field values returned and expiration date parsing`() {
        val cardController = CardDetailsController()
        val cardDetailsElement = CardDetailsElement(
            IdentifierSpec.Generic("card_details"),
            cardController
        )

        val flowValues = mutableListOf<List<Pair<IdentifierSpec, FormFieldEntry>>>()
        cardDetailsElement.getFormFieldValueFlow().asLiveData()
            .observeForever {
                flowValues.add(it)
            }

        cardDetailsElement.controller.numberElement.controller.onValueChange("4242424242424242")
        cardDetailsElement.controller.cvcElement.controller.onValueChange("321")
        cardDetailsElement.controller.expirationDateElement.controller.onValueChange("130")

        idleLooper()

        Truth.assertThat(flowValues[flowValues.size - 1]).isEqualTo(
            listOf(
                IdentifierSpec.Generic("number") to FormFieldEntry("4242424242424242", true),
                IdentifierSpec.Generic("cvc") to FormFieldEntry("321", true),
                IdentifierSpec.Generic("exp_month") to FormFieldEntry("1", true),
                IdentifierSpec.Generic("exp_year") to FormFieldEntry("30", true),
            )
        )
    }
}
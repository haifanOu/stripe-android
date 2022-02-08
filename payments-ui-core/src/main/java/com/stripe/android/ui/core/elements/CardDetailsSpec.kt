package com.stripe.android.ui.core.elements

import kotlinx.parcelize.Parcelize

@Parcelize
internal object CardDetailsSpec : SectionFieldSpec(IdentifierSpec.Generic("card_details")){
    fun transform(): SectionFieldElement = CardDetailsElement(
        IdentifierSpec.Generic("credit_detail")
    )
}

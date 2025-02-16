package com.stripe.android.ui.core.forms

import androidx.annotation.RestrictTo
import com.stripe.android.ui.core.R
import com.stripe.android.ui.core.elements.CountrySpec
import com.stripe.android.ui.core.elements.EmailSpec
import com.stripe.android.ui.core.elements.IdentifierSpec
import com.stripe.android.ui.core.elements.LayoutSpec
import com.stripe.android.ui.core.elements.MandateTextSpec
import com.stripe.android.ui.core.elements.SaveForFutureUseSpec
import com.stripe.android.ui.core.elements.SectionSpec
import com.stripe.android.ui.core.elements.SimpleTextSpec

internal val sofortNameSection = SectionSpec(
    IdentifierSpec.Generic("name_section"),
    SimpleTextSpec.NAME
)
internal val sofortEmailSection = SectionSpec(IdentifierSpec.Generic("email_section"), EmailSpec)
internal val sofortCountrySection =
    SectionSpec(
        IdentifierSpec.Generic("country_section"),
        CountrySpec(setOf("AT", "BE", "DE", "ES", "IT", "NL"))
    )
internal val sofortMandate = MandateTextSpec(
    IdentifierSpec.Generic("mandate"),
    R.string.sepa_mandate
)

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
val SofortForm = LayoutSpec.create(
    sofortNameSection,
    sofortEmailSection,
    sofortCountrySection,
    SaveForFutureUseSpec(listOf(sofortNameSection, sofortEmailSection, sofortMandate)),
    sofortMandate,
)

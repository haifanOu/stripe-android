package com.stripe.android.ui.core.elements

import android.os.Parcelable
import androidx.annotation.RestrictTo
import com.stripe.android.view.BecsDebitBanks
import kotlinx.parcelize.Parcelize

@Parcelize
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
data class BsbSpec(
    override val identifier: IdentifierSpec = IdentifierSpec.Generic("au_becs_debit[bsb_number]")
) : FormItemSpec(), RequiredItemSpec, Parcelable {
    fun transform(initialValues: Map<IdentifierSpec, String?>): BsbElement =
        BsbElement(
            this.identifier,
            banks,
            initialValues[this.identifier]
        )
}

private val banks: List<BecsDebitBanks.Bank> = listOf(
    BecsDebitBanks.Bank("00", "Stripe Test Bank"),
    BecsDebitBanks.Bank("10", "BankSA (division of Westpac Bank)"),
    BecsDebitBanks.Bank("11", "St George Bank (division of Westpac Bank)"),
    BecsDebitBanks.Bank("12", "Bank of Queensland"),
    BecsDebitBanks.Bank("14", "Rabobank"),
    BecsDebitBanks.Bank("15", "Town & Country Bank"),
    BecsDebitBanks.Bank("18", "Macquarie Bank"),
    BecsDebitBanks.Bank("19", "Bank of Melbourne (division of Westpac Bank)"),
    BecsDebitBanks.Bank("21", "JP Morgan Chase Bank"),
    BecsDebitBanks.Bank("22", "BNP Paribas"),
    BecsDebitBanks.Bank("23", "Bank of America"),
    BecsDebitBanks.Bank("24", "Citibank"),
    BecsDebitBanks.Bank("25", "BNP Paribas Securities"),
    BecsDebitBanks.Bank("26", "Bankers Trust Australia (division of Westpac Bank)"),
    BecsDebitBanks.Bank("29", "Bank of Tokyo-Mitsubishi"),
    BecsDebitBanks.Bank("30", "Bankwest (division of Commonwealth Bank)"),
    BecsDebitBanks.Bank("33", "St George Bank (division of Westpac Bank)"),
    BecsDebitBanks.Bank("34", "HSBC Bank Australia"),
    BecsDebitBanks.Bank("35", "Bank of China"),
    BecsDebitBanks.Bank("40", "Commonwealth Bank of Australia"),
    BecsDebitBanks.Bank("41", "Deutsche Bank"),
    BecsDebitBanks.Bank("42", "Commonwealth Bank of Australia"),
    BecsDebitBanks.Bank("45", "OCBC Bank"),
    BecsDebitBanks.Bank("46", "Advance Bank (division of Westpac Bank)"),
    BecsDebitBanks.Bank("47", "Challenge Bank (division of Westpac Bank)"),
    BecsDebitBanks.Bank("48", "Suncorp-Metway"),
    BecsDebitBanks.Bank("52", "Commonwealth Bank of Australia"),
    BecsDebitBanks.Bank("55", "Bank of Melbourne (division of Westpac Bank)"),
    BecsDebitBanks.Bank("57", "Australian Settlements"),
    BecsDebitBanks.Bank("61", "Adelaide Bank (division of Bendigo and Adelaide Bank)"),
    BecsDebitBanks.Bank("70", "Indue"),
    BecsDebitBanks.Bank("73", "Westpac Banking Corporation"),
    BecsDebitBanks.Bank("76", "Commonwealth Bank of Australia"),
    BecsDebitBanks.Bank("80", "Cuscal"),
    BecsDebitBanks.Bank("90", "Australia Post"),
    BecsDebitBanks.Bank("311", "in1bank"),
    BecsDebitBanks.Bank("313", "Bankmecu"),
    BecsDebitBanks.Bank("323", "KEB Hana Bank"),
    BecsDebitBanks.Bank("325", "Beyond Bank Australia"),
    BecsDebitBanks.Bank("432", "Standard Chartered Bank"),
    BecsDebitBanks.Bank("510", "Citibank N.A."),
    BecsDebitBanks.Bank("512", "Community First Credit Union"),
    BecsDebitBanks.Bank("514", "QT Mutual Bank"),
    BecsDebitBanks.Bank("517", "Australian Settlements Limited"),
    BecsDebitBanks.Bank("533", "Bananacoast Community Credit Union"),
    BecsDebitBanks.Bank("611", "Select Credit Union"),
    BecsDebitBanks.Bank("630", "ABS Building Society"),
    BecsDebitBanks.Bank("632", "B&E"),
    BecsDebitBanks.Bank("633", "Bendigo Bank"),
    BecsDebitBanks.Bank("634", "Uniting Financial Services"),
    BecsDebitBanks.Bank("636", "Cuscal Limited"),
    BecsDebitBanks.Bank("637", "Greater Building Society"),
    BecsDebitBanks.Bank("638", "Heritage Bank"),
    BecsDebitBanks.Bank("639", "Home Building Society (division of Bank of Queensland)"),
    BecsDebitBanks.Bank("640", "Hume Bank"),
    BecsDebitBanks.Bank("641", "IMB"),
    BecsDebitBanks.Bank("642", "Australian Defence Credit Union"),
    BecsDebitBanks.Bank("645", "Wide Bay Australia"),
    BecsDebitBanks.Bank("646", "Maitland Mutual Building Society"),
    BecsDebitBanks.Bank("647", "IMB"),
    BecsDebitBanks.Bank("650", "Newcastle Permanent Building Society"),
    BecsDebitBanks.Bank(
        "653",
        "Pioneer Permanent Building Society (division of Bank of Queensland)"
    ),
    BecsDebitBanks.Bank("654", "ECU Australia"),
    BecsDebitBanks.Bank("655", "The Rock Building Society"),
    BecsDebitBanks.Bank("656", "Wide Bay Australia"),
    BecsDebitBanks.Bank("657", "Greater Building Society"),
    BecsDebitBanks.Bank("659", "SGE Credit Union"),
    BecsDebitBanks.Bank("664", "Suncorp-Metway"),
    BecsDebitBanks.Bank("670", "Cuscal Limited"),
    BecsDebitBanks.Bank("676", "Gateway Credit Union"),
    BecsDebitBanks.Bank("680", "Greater Bank Limited"),
    BecsDebitBanks.Bank("721", "Holiday Coast Credit Union"),
    BecsDebitBanks.Bank("722", "Southern Cross Credit"),
    BecsDebitBanks.Bank("723", "Heritage Isle Credit Union"),
    BecsDebitBanks.Bank("724", "Railways Credit Union"),
    BecsDebitBanks.Bank("725", "Judo Bank Pty Ltd"),
    BecsDebitBanks.Bank("728", "Summerland Credit Union"),
    BecsDebitBanks.Bank("775", "Australian Settlements Limited"),
    BecsDebitBanks.Bank("777", "Police & Nurse"),
    BecsDebitBanks.Bank("812", "Teachers Mutual Bank"),
    BecsDebitBanks.Bank("813", "Capricornian"),
    BecsDebitBanks.Bank("814", "Credit Union Australia"),
    BecsDebitBanks.Bank("815", "Police Bank"),
    BecsDebitBanks.Bank("817", "Warwick Credit Union"),
    BecsDebitBanks.Bank("818", "Bank of Communications"),
    BecsDebitBanks.Bank("819", "Industrial & Commercial Bank of China"),
    BecsDebitBanks.Bank("820", "Global Payments Australia 1 Pty Ltd"),
    BecsDebitBanks.Bank("823", "Encompass Credit Union"),
    BecsDebitBanks.Bank("824", "Sutherland Credit Union"),
    BecsDebitBanks.Bank("825", "Big Sky Building Society"),
    BecsDebitBanks.Bank("833", "Defence Bank Limited"),
    BecsDebitBanks.Bank("840", "Split Payments Pty Ltd"),
    BecsDebitBanks.Bank("880", "Heritage Bank"),
    BecsDebitBanks.Bank("882", "Maritime Mining & Power Credit Union"),
    BecsDebitBanks.Bank("888", "China Construction Bank Corporation"),
    BecsDebitBanks.Bank("889", "DBS Bank Ltd."),
    BecsDebitBanks.Bank("911", "Sumitomo Mitsui Banking Corporation"),
    BecsDebitBanks.Bank("913", "State Street Bank & Trust Company"),
    BecsDebitBanks.Bank("917", "Arab Bank Australia"),
    BecsDebitBanks.Bank("918", "Mizuho Bank"),
    BecsDebitBanks.Bank("922", "United Overseas Bank"),
    BecsDebitBanks.Bank("923", "ING Bank"),
    BecsDebitBanks.Bank("931", "Mega International Commercial Bank"),
    BecsDebitBanks.Bank("932", "Community Mutual"),
    BecsDebitBanks.Bank("936", "ING Bank"),
    BecsDebitBanks.Bank("939", "AMP Bank"),
    BecsDebitBanks.Bank("941", "Delphi Bank (division of Bendigo and Adelaide Bank)"),
    BecsDebitBanks.Bank("942", "Bank of Sydney"),
    BecsDebitBanks.Bank("943", "Taiwan Business Bank"),
    BecsDebitBanks.Bank("944", "Members Equity Bank"),
    BecsDebitBanks.Bank("946", "UBS AG"),
    BecsDebitBanks.Bank("951", "BOQ Specialist Bank"),
    BecsDebitBanks.Bank("952", "Royal Bank of Scotland"),
    BecsDebitBanks.Bank("969", "Tyro Payments"),
    BecsDebitBanks.Bank("980", "Bank of China"),
    BecsDebitBanks.Bank("985", "HSBC Bank Australia"),
    BecsDebitBanks.Bank("01", "Australia and New Zealand Banking Group"),
    BecsDebitBanks.Bank("03", "Westpac Banking Corporation"),
    BecsDebitBanks.Bank("04", "Westpac Banking Corporation"),
    BecsDebitBanks.Bank("06", "Commonwealth Bank of Australia"),
    BecsDebitBanks.Bank("08", "National Australia Bank"),
    BecsDebitBanks.Bank("09", "Reserve Bank of Australia"),
)

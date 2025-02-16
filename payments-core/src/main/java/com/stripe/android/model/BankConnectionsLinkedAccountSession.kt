package com.stripe.android.model

import com.stripe.android.core.model.StripeModel
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class BankConnectionsLinkedAccountSession(
    val clientSecret: String?,
    val id: String?,
) : StripeModel

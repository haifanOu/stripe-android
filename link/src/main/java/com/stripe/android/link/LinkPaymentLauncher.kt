package com.stripe.android.link

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RestrictTo
import com.stripe.android.core.injection.ENABLE_LOGGING
import com.stripe.android.core.injection.IOContext
import com.stripe.android.core.injection.Injectable
import com.stripe.android.core.injection.InjectorKey
import com.stripe.android.core.injection.PUBLISHABLE_KEY
import com.stripe.android.core.injection.STRIPE_ACCOUNT_ID
import com.stripe.android.core.injection.UIContext
import com.stripe.android.core.injection.WeakMapInjectorRegistry
import com.stripe.android.core.networking.AnalyticsRequestExecutor
import com.stripe.android.link.injection.DaggerLinkPaymentLauncherComponent
import com.stripe.android.link.injection.NonFallbackInjector
import com.stripe.android.link.ui.signup.SignUpViewModel
import com.stripe.android.link.ui.verification.VerificationViewModel
import com.stripe.android.link.ui.wallet.WalletViewModel
import com.stripe.android.model.StripeIntent
import com.stripe.android.networking.PaymentAnalyticsRequestFactory
import com.stripe.android.networking.StripeRepository
import com.stripe.android.payments.core.injection.PRODUCT_USAGE
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Launcher for an Activity that will confirm a payment using Link.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class LinkPaymentLauncher @AssistedInject constructor(
    @Assisted private val activityResultLauncher: ActivityResultLauncher<LinkActivityContract.Args>,
    context: Context,
    @Named(PRODUCT_USAGE) private val productUsage: Set<String>,
    @Named(PUBLISHABLE_KEY) private val publishableKeyProvider: () -> String,
    @Named(STRIPE_ACCOUNT_ID) private val stripeAccountIdProvider: () -> String?,
    @Named(ENABLE_LOGGING) private val enableLogging: Boolean,
    @IOContext ioContext: CoroutineContext,
    @UIContext uiContext: CoroutineContext,
    paymentAnalyticsRequestFactory: PaymentAnalyticsRequestFactory,
    analyticsRequestExecutor: AnalyticsRequestExecutor,
    stripeRepository: StripeRepository
) {
    private val launcherComponentBuilder = DaggerLinkPaymentLauncherComponent.builder()
        .context(context)
        .ioContext(ioContext)
        .uiContext(uiContext)
        .analyticsRequestFactory(paymentAnalyticsRequestFactory)
        .analyticsRequestExecutor(analyticsRequestExecutor)
        .stripeRepository(stripeRepository)
        .enableLogging(enableLogging)
        .publishableKeyProvider(publishableKeyProvider)
        .stripeAccountIdProvider(stripeAccountIdProvider)
        .productUsage(productUsage)

    @InjectorKey
    private val injectorKey: String = WeakMapInjectorRegistry.nextKey(
        requireNotNull(LinkPaymentLauncher::class.simpleName)
    )

    fun present(
        stripeIntent: StripeIntent,
        merchantName: String,
        customerEmail: String? = null
    ) {
        val args = LinkActivityContract.Args(
            stripeIntent,
            merchantName,
            customerEmail,
            LinkActivityContract.Args.InjectionParams(
                injectorKey,
                productUsage,
                enableLogging,
                publishableKeyProvider(),
                stripeAccountIdProvider()
            )
        )

        setupInjector(args)
        activityResultLauncher.launch(args)
    }

    private fun setupInjector(args: LinkActivityContract.Args) {
        val launcherComponent = launcherComponentBuilder
            .starterArgs(args)
            .build()

        val injector = object : NonFallbackInjector {
            override fun inject(injectable: Injectable<*>) {
                when (injectable) {
                    is LinkActivityViewModel.Factory -> launcherComponent.inject(injectable)
                    is SignUpViewModel.Factory -> launcherComponent.inject(injectable)
                    is VerificationViewModel.Factory -> launcherComponent.inject(injectable)
                    is WalletViewModel.Factory -> launcherComponent.inject(injectable)
                    else -> {
                        throw IllegalArgumentException("invalid Injectable $injectable requested in $this")
                    }
                }
            }
        }

        WeakMapInjectorRegistry.register(injector, injectorKey)
    }
}

package com.commercetools.sunrise.framework.checkout.payment.viewmodels;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.common.models.carts.CartViewModelFactory;
import com.commercetools.sunrise.framework.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.framework.checkout.payment.PaymentMethodsWithCart;
import play.data.Form;

import javax.inject.Inject;

public class CheckoutPaymentPageContentFactory extends FormPageContentFactory<CheckoutPaymentPageContent, PaymentMethodsWithCart, CheckoutPaymentFormData> {

    private final PageTitleResolver pageTitleResolver;
    private final CartViewModelFactory cartViewModelFactory;
    private final CheckoutPaymentFormSettingsViewModelFactory checkoutPaymentFormSettingsViewModelFactory;

    @Inject
    public CheckoutPaymentPageContentFactory(final PageTitleResolver pageTitleResolver, final CartViewModelFactory cartViewModelFactory,
                                             final CheckoutPaymentFormSettingsViewModelFactory checkoutPaymentFormSettingsViewModelFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.cartViewModelFactory = cartViewModelFactory;
        this.checkoutPaymentFormSettingsViewModelFactory = checkoutPaymentFormSettingsViewModelFactory;
    }

    @Override
    protected CheckoutPaymentPageContent getViewModelInstance() {
        return new CheckoutPaymentPageContent();
    }

    @Override
    public final CheckoutPaymentPageContent create(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return super.create(paymentMethodsWithCart, form);
    }

    protected final void initialize(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        super.initialize(viewModel, paymentMethodsWithCart, form);
        fillCart(viewModel, paymentMethodsWithCart, form);
        fillForm(viewModel, paymentMethodsWithCart, form);
        fillFormSettings(viewModel, paymentMethodsWithCart, form);
    }

    @Override
    protected void fillTitle(final CheckoutPaymentPageContent viewModel, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("checkout:paymentPage.title"));
    }

    protected void fillCart(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setCart(cartViewModelFactory.create(paymentMethodsWithCart.getCart()));
    }

    protected void fillForm(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setPaymentForm(form);
    }

    protected void fillFormSettings(final CheckoutPaymentPageContent model, final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        model.setPaymentFormSettings(checkoutPaymentFormSettingsViewModelFactory.create(paymentMethodsWithCart, form));
    }
}
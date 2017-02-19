package controllers.myaccount;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.myaccount.AuthenticationReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.SunriseAddressBookDetailController;
import com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.view.AddressBookPageContentFactory;
import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import controllers.PageHeaderControllerComponentsSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class AddressBookDetailController extends SunriseAddressBookDetailController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public AddressBookDetailController(final TemplateRenderer templateRenderer,
                                       final CustomerFinder customerFinder,
                                       final AddressBookPageContentFactory addressBookPageContentFactory,
                                       final AuthenticationReverseRouter authenticationReverseRouter) {
        super(templateRenderer, customerFinder, addressBookPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.logInPageCall());
    }
}
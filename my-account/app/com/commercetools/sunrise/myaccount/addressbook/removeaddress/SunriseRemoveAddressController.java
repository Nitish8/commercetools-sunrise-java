package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookActionData;
import com.commercetools.sunrise.myaccount.addressbook.SunriseAddressBookManagementController;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookControllerData;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContent;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.AddressBookPageContentFactory;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerUpdateCommand;
import io.sphere.sdk.customers.commands.updateactions.RemoveAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

public abstract class SunriseRemoveAddressController extends SunriseAddressBookManagementController implements WithTemplateName, WithFormFlow<RemoveAddressFormData, AddressBookActionData, Customer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SunriseRemoveAddressController.class);

    private final AddressBookPageContentFactory addressBookPageContentFactory;

    protected SunriseRemoveAddressController(final AddressBookReverseRouter addressBookReverseRouter,
                                             final AddressBookPageContentFactory addressBookPageContentFactory) {
        super(addressBookReverseRouter);
        this.addressBookPageContentFactory = addressBookPageContentFactory;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("address-book", "remove-address", "address"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "my-account-address-book";
    }

    @Override
    public Class<? extends RemoveAddressFormData> getFormDataClass() {
        return RemoveAddressFormData.class;
    }

    @SunriseRoute("removeAddressFromAddressBookProcessFormCall")
    public CompletionStage<Result> process(final String languageTag, final String addressId) {
        return doRequest(() -> {
            LOGGER.debug("try to remove address with id={} in locale={}", addressId, languageTag);
            return findAddressBookActionData(addressId)
                    .thenComposeAsync(this::validateForm, HttpExecution.defaultContext());
        });
    }

    @Override
    public CompletionStage<? extends Customer> doAction(final RemoveAddressFormData formData, final AddressBookActionData context) {
        final RemoveAddress updateAction = RemoveAddress.of(context.getAddress());
        return sphere().execute(CustomerUpdateCommand.of(context.getCustomer(), updateAction));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final RemoveAddressFormData formData, final AddressBookActionData context, final Customer result) {
        return redirectToAddressBook();
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends RemoveAddressFormData> form, final AddressBookActionData context, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, LOGGER);
        return asyncBadRequest(renderPage(form, context, null));
    }

    @Override
    public void fillFormData(final RemoveAddressFormData formData, final AddressBookActionData context) {
        // Do nothing
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends RemoveAddressFormData> form, final AddressBookActionData context, @Nullable final Customer updatedCustomer) {
        final AddressBookControllerData addressBookControllerData = new AddressBookControllerData(context.getCustomer(), updatedCustomer);
        final AddressBookPageContent pageContent = addressBookPageContentFactory.create(addressBookControllerData);
        return renderPageWithTemplate(pageContent, getTemplateName());
    }
}

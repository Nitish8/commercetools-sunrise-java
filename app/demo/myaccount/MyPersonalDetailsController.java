package demo.myaccount;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.mydetails.DefaultMyPersonalDetailsFormData;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsPageContentFactory;
import com.commercetools.sunrise.myaccount.mydetails.MyPersonalDetailsUpdater;
import com.commercetools.sunrise.myaccount.mydetails.SunriseMyPersonalDetailsController;
import io.sphere.sdk.customers.Customer;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class MyPersonalDetailsController extends SunriseMyPersonalDetailsController<DefaultMyPersonalDetailsFormData> {

    private final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;
    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public MyPersonalDetailsController(final CustomerFinder customerFinder,
                                       final MyPersonalDetailsUpdater myPersonalDetailsUpdater,
                                       final MyPersonalDetailsPageContentFactory myPersonalDetailsPageContentFactory,
                                       final MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter,
                                       final AuthenticationReverseRouter authenticationReverseRouter) {
        super(customerFinder, myPersonalDetailsUpdater, myPersonalDetailsPageContentFactory);
        this.myPersonalDetailsReverseRouter = myPersonalDetailsReverseRouter;
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Override
    public Class<DefaultMyPersonalDetailsFormData> getFormDataClass() {
        return DefaultMyPersonalDetailsFormData.class;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultMyPersonalDetailsFormData formData, final Customer customer, final Customer updatedCustomer) {
        return redirectTo(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall());
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}

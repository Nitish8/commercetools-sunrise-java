package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionLocalizationReverseRouter.class)
interface LocalizationSimpleReverseRouter {

    Call processChangeLanguageForm();

    Call processChangeCountryForm(final String languageTag);
}

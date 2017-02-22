package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.SelectableViewModelFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Locale;

@RequestScoped
public class SortFormSelectableOptionViewModelFactory extends SelectableViewModelFactory<SortFormSelectableOptionViewModel, SortFormOption, String> {

    private final Locale locale;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final I18nResolver i18nResolver;

    @Inject
    public SortFormSelectableOptionViewModelFactory(final Locale locale, final I18nIdentifierFactory i18nIdentifierFactory,
                                               final I18nResolver i18nResolver) {
        this.locale = locale;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.i18nResolver = i18nResolver;
    }

    @Override
    protected SortFormSelectableOptionViewModel getViewModelInstance() {
        return new SortFormSelectableOptionViewModel();
    }

    @Override
    public final SortFormSelectableOptionViewModel create(final SortFormOption option, @Nullable final String selectedValue) {
        return super.create(option, selectedValue);
    }

    @Override
    protected final void initialize(final SortFormSelectableOptionViewModel model, final SortFormOption option, @Nullable final String selectedValue) {
        fillLabel(model, option, selectedValue);
        fillValue(model, option, selectedValue);
        fillSelected(model, option, selectedValue);
    }

    protected void fillLabel(final SortFormSelectableOptionViewModel model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(option.getFieldLabel());
        model.setLabel(i18nResolver.getOrKey(Collections.singletonList(locale), i18nIdentifier));
    }

    protected void fillValue(final SortFormSelectableOptionViewModel model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setValue(option.getFieldValue());
    }

    protected void fillSelected(final SortFormSelectableOptionViewModel model, final SortFormOption option, @Nullable final String selectedOptionValue) {
        model.setSelected(option.getFieldValue().equals(selectedOptionValue));
    }
}
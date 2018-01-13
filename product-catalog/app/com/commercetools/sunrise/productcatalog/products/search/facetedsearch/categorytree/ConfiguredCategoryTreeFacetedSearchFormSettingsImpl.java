package com.commercetools.sunrise.productcatalog.products.search.facetedsearch.categorytree;

import com.commercetools.sunrise.models.search.facetedsearch.AbstractConfiguredFacetedSearchFormSettings;

import javax.annotation.Nullable;

final class ConfiguredCategoryTreeFacetedSearchFormSettingsImpl extends AbstractConfiguredFacetedSearchFormSettings implements ConfiguredCategoryTreeFacetedSearchFormSettings {

    private final String fieldName;

    ConfiguredCategoryTreeFacetedSearchFormSettingsImpl(final String fieldName, final String fieldLabel, final String attributePath,
                                                        final boolean countDisplayed, @Nullable final String uiType) {
        super(fieldLabel, attributePath, countDisplayed, uiType);
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
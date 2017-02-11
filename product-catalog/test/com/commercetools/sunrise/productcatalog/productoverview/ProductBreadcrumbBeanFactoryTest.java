package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.controllers.TestableCall;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.reverserouter.ProductSimpleReverseRouter;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBean;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbLinkBean;
import com.commercetools.sunrise.productcatalog.productdetail.ProductBreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductDetailControllerData;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;
import play.mvc.Call;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static com.commercetools.sunrise.common.utils.JsonUtils.readCtpObject;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductBreadcrumbBeanFactoryTest {

    private static final CategoryTree CATEGORY_TREE = CategoryTree.of(readCtpObject("breadcrumb/breadcrumbCategories.json", CategoryQuery.resultTypeReference()).getResults());
    private static final ProductProjection PRODUCT = readCtpObject("breadcrumb/breadcrumbProduct.json", ProductProjection.typeReference());

    @Test
    public void createProductBreadcrumb() throws Exception {
        testProductBreadcrumb(PRODUCT,
                texts -> assertThat(texts).containsExactly("1st Level", "2nd Level", "Some product"),
                urls -> assertThat(urls).containsExactly("category-1st-level", "category-2nd-level", "product-some-product-some-sku"));
    }

    private void testProductBreadcrumb(final ProductProjection product, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        final ProductDetailControllerData productDetailControllerData = new ProductDetailControllerData(new ProductWithVariant(product, product.getMasterVariant()));
        final BreadcrumbBean breadcrumb = createBreadcrumbFactory().create(productDetailControllerData);
        testBreadcrumb(breadcrumb, texts, urls);
    }

    private void testBreadcrumb(final BreadcrumbBean breadcrumb, final Consumer<List<String>> texts, final Consumer<List<String>> urls) {
        texts.accept(breadcrumb.getLinks().stream()
                .map(BreadcrumbLinkBean::getText)
                .map(link -> link.get(Locale.ENGLISH))
                .collect(toList()));
        urls.accept(breadcrumb.getLinks().stream().map(BreadcrumbLinkBean::getUrl).collect(toList()));
    }

    private static ProductBreadcrumbBeanFactory createBreadcrumbFactory() {
        return new ProductBreadcrumbBeanFactory(Locale.ENGLISH, CATEGORY_TREE, reverseRouter());
    }

    private static ProductSimpleReverseRouter reverseRouter() {
        return new ProductSimpleReverseRouter() {
            @Override
            public Call productDetailPageCall(final String languageTag, final String productSlug, final String sku) {
                return new TestableCall("pdp");
            }

            @Override
            public Call productOverviewPageCall(final String languageTag, final String categorySlug) {
                return new TestableCall("pop");
            }

            @Override
            public Call processSearchProductsForm(final String languageTag) {
                return new TestableCall("search");
            }
        };
    }
}

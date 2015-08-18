package productcatalog.controllers;

import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.pages.*;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.Configuration;
import play.libs.F;
import play.mvc.Result;
import productcatalog.models.ShopShippingRate;
import productcatalog.pages.*;
import productcatalog.services.CategoryService;
import productcatalog.services.ProductProjectionService;
import productcatalog.services.ShippingMethodService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Locale.GERMAN;
import static java.util.stream.Collectors.toList;

@Singleton
public class ProductDetailPageController extends SunriseController {

    private final int numberOfSuggestions;
    private final ProductProjectionService productService;
    private final CategoryService categoryService;
    private final ShippingMethodService shippingMethodService;

    @Inject
    public ProductDetailPageController(final Configuration configuration, final ControllerDependency controllerDependency, final ProductProjectionService productService, final CategoryService categoryService, final ShippingMethodService shippingMethodService) {
        super(controllerDependency);
        this.productService = productService;
        this.categoryService = categoryService;
        this.shippingMethodService = shippingMethodService;
        this.numberOfSuggestions = configuration.getInt("pdp.productSuggestions.count");
    }

    public F.Promise<Result> pdp(final String slug, final String sku) {
        final F.Promise<Optional<ProductProjection>> productOptPromise = productService.searchProductBySlug(GERMAN, slug);

        return productOptPromise.flatMap(productOptional -> {
            final Optional<F.Promise<Result>> resultPromise = productOptional.flatMap(product ->
                    productService.findVariantBySku(product, sku).map(variant -> pdpx(product, variant)));
            return resultPromise.orElse(F.Promise.pure(notFound()));
        });
    }

    private F.Promise<Result> pdpx(final ProductProjection product, final ProductVariant variant) {

        final F.Promise<List<ProductProjection>> suggestionPromise =
                productService.getSuggestions(categoryService.getSiblingCategories(product.getCategories()), numberOfSuggestions);
        final List<ShopShippingRate> shippingRates = shippingMethodService.getShippingRates(context().user().zone());
        final List<Category> breadcrumbs = product.getCategories().stream().findFirst()
                .map(categoryService::getBreadCrumbCategories)
                .orElse(Collections.<Category>emptyList());

        return suggestionPromise.flatMap(suggestions -> withCms("pdp", cms -> {
            final Translator translator = context().translator();
            final PriceFormatter priceFormatter = context().user().priceFormatter();
            final PriceFinder priceFinder = context().user().priceFinder();

            final ProductThumbnailDataFactory thumbnailDataFactory = ProductThumbnailDataFactory.of(translator, priceFinder, priceFormatter);
            final ShippingRateDataFactory shippingRateDataFactory = ShippingRateDataFactory.of(priceFormatter);
            final CategoryLinkDataFactory categoryLinkDataFactory = CategoryLinkDataFactory.of(translator);

            final String additionalTitle = translator.findTranslation(product.getName());
            final PdpStaticData staticData = new PdpStaticData(cms, BagItemDataFactory.of().create(100), RatingDataFactory.of(cms).create());
            final List<LinkData> breadcrumbData = breadcrumbs.stream().map(categoryLinkDataFactory::create).collect(toList());
            final List<ImageData> galleryData = variant.getImages().stream().map(ImageData::of).collect(toList());
            final ProductData productData = ProductDataFactory.of(translator, priceFinder, priceFormatter).create(product, variant);
            final List<ShippingRateData> deliveryData = shippingRates.stream().map(shippingRateDataFactory::create).collect(toList());
            final List<ProductThumbnailData> suggestionData = suggestions.stream().map(thumbnailDataFactory::create).collect(toList());

            final ProductDetailPageContent content = new ProductDetailPageContent(additionalTitle, staticData, breadcrumbData, galleryData, productData, deliveryData, suggestionData);

            return render(view -> ok(view.productDetailPage(content)));
        }));
    }

    private F.Promise<Result> render(final Function<ProductCatalogView, Result> pageRenderer) {
        return withCommonCms(cms -> {
            final ProductCatalogView view = new ProductCatalogView(templateService(), context(), cms);
            return pageRenderer.apply(view);
        });
    }
}
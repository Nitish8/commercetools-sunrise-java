package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.carts.Cart;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class MyCartInCacheImpl extends AbstractResourceInCache<Cart> implements MyCartInCache {

    private final MyCartFetcher myCartFetcher;

    @Inject
    MyCartInCacheImpl(final MyCartInSession myCartInSession, final CacheApi cacheApi, final MyCartFetcher myCartFetcher) {
        super(myCartInSession, cacheApi);
        this.myCartFetcher = myCartFetcher;
    }

    @Override
    protected CompletionStage<Optional<Cart>> fetchResource() {
        return myCartFetcher.get();
    }

    @Override
    protected String generateCacheKey(final String cartId) {
        return "cart_" + cartId;
    }
}
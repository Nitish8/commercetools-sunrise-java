package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.carts.Cart;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class CartInCacheImpl extends AbstractResourceInCache<Cart> implements CartInCache {

    private final CartFinder cartFinder;

    @Inject
    CartInCacheImpl(final CartInSession cartInSession, final CacheApi cacheApi, final CartFinder cartFinder) {
        super(cartInSession, cacheApi);
        this.cartFinder = cartFinder;
    }

    @Override
    protected CompletionStage<Optional<Cart>> fetchResource() {
        return cartFinder.get();
    }

    @Override
    protected String generateCacheKey(final String cartId) {
        return "cart_" + cartId;
    }
}
package common.cms;

import java.util.Optional;

/**
 * Obtains content data for a page, usually coming from a Content Management System (CMS).
 */
@FunctionalInterface
public interface CmsPage {

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @param args optional list of arguments
     * @return the message identified by the key, with the given arguments, or empty string if not found
     */
    Optional<String> get(final String messageKey, final Object... args);

    /**
     * Gets the message corresponding to the given key, or empty in case it does not exist.
     * @param messageKey identifying the message
     * @param args optional list of arguments
     * @return the message identified by the key, with the given arguments, or empty string if not found
     */
    default String getOrEmpty(final String messageKey, final Object... args) {
        return get(messageKey, args).orElse("");
    }
}

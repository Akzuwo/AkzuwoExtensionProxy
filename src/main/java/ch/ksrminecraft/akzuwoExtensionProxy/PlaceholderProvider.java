package ch.ksrminecraft.akzuwoExtensionProxy;

/**
 * Simple service interface that allows other plugins to request
 * the custom placeholder values provided by this plugin.
 */
public interface PlaceholderProvider {

    /**
     * Resolve the value for a placeholder.
     *
     * @param placeholder placeholder identifier
     * @return resolved value or {@code null} if the placeholder is unknown
     */
    String getPlaceholderValue(String placeholder);
}

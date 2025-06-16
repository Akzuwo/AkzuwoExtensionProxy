package ch.ksrminecraft.akzuwoExtensionProxy;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;
import ch.ksrminecraft.akzuwoExtensionProxy.PlaceholderProvider;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Main plugin class and provider for custom placeholders.
 */

@Plugin(id = "akzuwoextensionproxy", name = "AkzuwoExtensionProxy", version = "1.0")
public class AkzuwoExtensionProxy implements PlaceholderProvider {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer proxy;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("AkzuwoExtensionProxy initialized");
        // Register this class as a simple placeholder provider so other plugins
        // can access the placeholders via the Velocity ServiceManager.
        proxy.getServiceManager().register(this, PlaceholderProvider.class, this);
    }

    /**
     * Returns the value of the custom placeholders.
     *
     * @param placeholder Placeholder identifier
     * @return replacement or {@code null} if unknown
     */
    public String getPlaceholderValue(String placeholder) {
        switch (placeholder.toLowerCase()) {
            case "%akzuwoextensionproxy_playercount%":
                return String.valueOf(proxy.getPlayerCount());
            case "%akzuwoextensionproxy_playercount_vanish%":
                return String.valueOf(getVisiblePlayerCount());
            default:
                return null;
        }
    }

    private int getVisiblePlayerCount() {
        int vanished = 0;
        try {
            Class<?> apiClass = Class.forName("de.myzelyam.api.vanish.VanishAPI");
            Method isInvisible = apiClass.getMethod("isInvisible", UUID.class);
            for (Player player : proxy.getAllPlayers()) {
                boolean invisible = (boolean) isInvisible.invoke(null, player.getUniqueId());
                if (invisible) {
                    vanished++;
                }
            }
        } catch (ClassNotFoundException e) {
            logger.warn("PremiumVanish API not found. Placeholder will include all players.");
        } catch (Exception e) {
            logger.error("Failed to query PremiumVanish API", e);
        }
        return proxy.getPlayerCount() - vanished;
    }
}

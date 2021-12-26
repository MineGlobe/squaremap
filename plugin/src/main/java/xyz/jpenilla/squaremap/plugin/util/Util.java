package xyz.jpenilla.squaremap.plugin.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static java.util.Objects.requireNonNull;

@DefaultQualifier(NonNull.class)
public final class Util {
    private Util() {
    }

    @SuppressWarnings("unchecked")
    public static <X extends Throwable> RuntimeException rethrow(final Throwable t) throws X {
        throw (X) t;
    }

    public static ThreadFactory squaremapThreadFactory(final String name) {
        return new NamedThreadFactory("squaremap-" + name);
    }

    public static ThreadFactory squaremapThreadFactory(final String name, final ServerLevel level) {
        return squaremapThreadFactory(name + "-[" + level.dimension().location() + "]");
    }

    public static void shutdownExecutor(final ExecutorService service, final TimeUnit timeoutUnit, final long timeoutLength) {
        service.shutdown();
        boolean didShutdown;
        try {
            didShutdown = service.awaitTermination(timeoutLength, timeoutUnit);
        } catch (final InterruptedException ignore) {
            didShutdown = false;
        }
        if (!didShutdown) {
            service.shutdownNow();
        }
    }

    public static <T> T requireEntry(final Registry<T> registry, final ResourceLocation location) {
        // manually check for key, we don't want the default value if registry is a DefaultedRegistry
        if (!registry.containsKey(location)) {
            throw new IllegalArgumentException("No such entry '" + location + "' in registry '" + registry.key() + "'");
        }
        return requireNonNull(registry.get(location));
    }
}

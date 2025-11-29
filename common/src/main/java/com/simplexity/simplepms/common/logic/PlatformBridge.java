package com.simplexity.simplepms.common.logic;

import org.jetbrains.annotations.NotNull;

public class PlatformBridge {

    private static PlatformAdapter adapter;

    /**
     * Sets the platform adapter.
     * <br/>
     * Any platform should implement their own version of {@link PlatformAdapter}
     * and then call this method.
     * @param adapter PlatformAdapter implemented by the platform.
     */
    public static void setPlatformAdapter(@NotNull PlatformAdapter adapter) {
        PlatformBridge.adapter = adapter;
    }

    /**
     * Retrieves the platform adapter for use in the common module.
     * <br/>
     * {@link #setPlatformAdapter(PlatformAdapter)} should be called
     * in the enable step of whatever platform is being used.
     * @return PlatformAdapter
     * @throws IllegalStateException If the platform adapter was never set before calling any PlatformAdapter methods.
     */
    public static @NotNull PlatformAdapter getPlatformAdapter() {
        if (adapter == null) throw new IllegalStateException("PlatformAdapter was not initialized.");
        return adapter;
    }

}

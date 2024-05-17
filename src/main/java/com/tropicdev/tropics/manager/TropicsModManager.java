package com.tropicdev.tropics.manager;

public final class TropicsModManager {

    public static void registerAll() {
        TropicsEventManager.registerEvents();
        TropicsRegistryManager.registerRegistries();
    }
}

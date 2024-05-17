package com.tropicdev.tropics;

import com.tropicdev.tropics.manager.TropicsModManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Tropics implements ModInitializer {
	public static final String MOD_NAME = "Tropics";
	public static final String MODID = "tropics";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		TropicsModManager.registerAll();
	}

	public static ResourceLocation prefix(String path) {
		return new ResourceLocation(MODID, path.toLowerCase(Locale.ROOT));
	}
}
package com.tropicdev.tropics.datagen;

import com.tropicdev.tropics.Tropics;
import com.tropicdev.tropics.datagen.providers.TropicsItemModelProvider;
import com.tropicdev.tropics.datagen.providers.TropicsLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TropicsDataGen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack cdPack = fabricDataGenerator.createPack();

		cdPack.addProvider(TropicsItemModelProvider::new);
		cdPack.addProvider(TropicsLanguageProvider::new);
	}

	@Override
	public String getEffectiveModId() {
		return Tropics.MODID;
	}
}

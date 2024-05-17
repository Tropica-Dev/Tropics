package com.tropicdev.tropics.datagen.providers;

import com.tropicdev.tropics.Tropics;
import com.tropicdev.tropics.registry.TropicsItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class TropicsItemModelProvider extends FabricModelProvider {

    public TropicsItemModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public String getName() {
        return Tropics.MOD_NAME.concat(": General (Block/Item) Models");
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        TropicsItems.getRegisteredItems().values().forEach(curItemSup -> { //TODO Edge-case automation (probably a bunch of yanderedev looking ahh code with the checks and all that :skull:)
            itemModelGenerator.generateFlatItem(curItemSup.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        });
    }
}

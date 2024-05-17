package com.tropicdev.tropics.registry;

import com.google.common.collect.ImmutableMap;
import com.tropicdev.tropics.Tropics;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TropicsItems {
    private static final Object2ObjectLinkedOpenHashMap<ResourceLocation, Supplier<Item>> REGISTERED_ITEMS = new Object2ObjectLinkedOpenHashMap<>();

    public static final Item PINA_COLADA = registerItem("pina_colada", () -> new Item(new Item.Properties().food(TropicsFood.PINA_COLADA).stacksTo(1)));

    protected static <I extends Item> I registerItem(String id, Supplier<I> itemSup) {
        return registerItem(Tropics.prefix(id), itemSup);
    }

    protected static <I extends Item> I registerItem(ResourceLocation id, Supplier<I> itemSup) {
        return (I) REGISTERED_ITEMS.putIfAbsent(id, (Supplier<Item>) itemSup);
    }

    public static ImmutableMap<ResourceLocation, Supplier<Item>> getRegisteredItems() {
        return ImmutableMap.copyOf(REGISTERED_ITEMS);
    }

    public static void registerItems() {
        REGISTERED_ITEMS.forEach((id, itemSup) -> Registry.register(BuiltInRegistries.ITEM, id, itemSup.get()));
    }
}

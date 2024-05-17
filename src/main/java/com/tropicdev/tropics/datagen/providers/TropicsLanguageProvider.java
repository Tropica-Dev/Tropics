package com.tropicdev.tropics.datagen.providers;

import com.google.common.collect.LinkedHashMultimap;
import com.tropicdev.tropics.Tropics;
import com.tropicdev.tropics.registry.TropicsItems;
import com.tropicdev.tropics.util.MiscUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public class TropicsLanguageProvider extends FabricLanguageProvider {
    private static final ObjectArrayList<String> DEFAULT_SEPARATORS = ObjectArrayList.of("Of", "And");
    private static final Object2ObjectOpenHashMap<String, String> MANUAL_TRANSLATIONS = new Object2ObjectOpenHashMap<>();
    private static final LinkedHashMultimap<String, String> MANUAL_INSERTIONS = LinkedHashMultimap.create();

    public TropicsLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public String getName() {
        return Tropics.MOD_NAME.concat(": Language Localization (en_us)"); //TODO Maybe multi-language support?
    }

    public static void addManualTranslation(String localisedKey, String translationValue) {
        MANUAL_INSERTIONS.put(localisedKey, translationValue);
    }

    private String handleInput(String displayName) {
        ObjectArrayList<String> finalizedList = new ObjectArrayList<>(1);

        MANUAL_INSERTIONS.asMap().forEach((curDisplayName, matchersAndInsertion) -> {
            if (!displayName.equalsIgnoreCase(curDisplayName)) return;

            ObjectArrayList<String> copiedMatchersAndInsertions = new ObjectArrayList<String>(matchersAndInsertion);
            ObjectArrayList<String> prunedMatchersAndInsertion = copiedMatchersAndInsertions.stream()
                    .filter(curString -> copiedMatchersAndInsertions.indexOf(curString) < 2)
                    .collect(Collectors.toCollection(ObjectArrayList::new));

            if (curDisplayName.indexOf(prunedMatchersAndInsertion.get(0)) == -1) return;

            int insertionLocation = curDisplayName.indexOf(prunedMatchersAndInsertion.get(0)) + prunedMatchersAndInsertion.get(0).length() - 1;

            curDisplayName = MiscUtil.insertStringAt(curDisplayName, prunedMatchersAndInsertion.get(1), insertionLocation);

            finalizedList.add(curDisplayName);
            Tropics.LOGGER.debug("[Inserted Manual String]: " + displayName + " -> " + curDisplayName);
        });

        return finalizedList.isEmpty() ? displayName : finalizedList.get(0);
    }

    // https://stackoverflow.com/questions/1892765/how-to-capitalize-the-first-character-of-each-word-in-a-string
    private static String formatString(String input, List<String> separators) {
        char[] charSet = input.toLowerCase().toCharArray();
        boolean found = false;

        for (int i = 0; i < charSet.length - 1; i++) {
            if (!found && Character.isLetter(charSet[i])) {
                charSet[i] = Character.toUpperCase(charSet[i]);
                found = true;
            } else if (Character.isWhitespace(charSet[i]) || charSet[i] == '.' || charSet[i] == '_') found = false;
        }

        String baseResult = String.valueOf(charSet);

        for (String lcw : DEFAULT_SEPARATORS) if (baseResult.contains(lcw)) baseResult = baseResult.replaceAll(lcw, lcw.toLowerCase());

        return baseResult;
    }

    private static String formatString(String input) {
        return formatString(input, DEFAULT_SEPARATORS);
    }

    private String getTranslatedRegistryName(String registryName, List<String> separators) {
        if (registryName.isBlank() || registryName.isEmpty() || registryName == null) return registryName;
        if (!registryName.contains(".")) return registryName;

        String regNameTemp = registryName;
        String formattedName = formatString(regNameTemp, separators);
        String displayName = formattedName.substring(formattedName.lastIndexOf(".") + 1).replaceAll("_", " ");

        return displayName;
    }

    private String getTranslatedRegistryName(String registryName) {
        return getTranslatedRegistryName(registryName, DEFAULT_SEPARATORS);
    }

    private void localizeGeneralRegistryName(String registryName, String translatedName, List<String> toTrim, TranslationBuilder builder) { //TODO Probably overload these fellas
        if (toTrim != null && !toTrim.isEmpty()) {
            for (int i = 0; i < toTrim.size(); i++) {
                String curTrim = toTrim.get(i);

                translatedName = translatedName.replaceFirst(curTrim, "");
            }
        }

        if (!MANUAL_TRANSLATIONS.containsKey(registryName)) builder.add(registryName, handleInput(translatedName));
    }

    private void localizeGeneralRegistryName(String registryName, List<String> separators, List<String> toTrim, TranslationBuilder builder) {
        String translatedRegName = getTranslatedRegistryName(registryName, separators);

        if (toTrim != null && !toTrim.isEmpty()) {
            for (int i = 0; i < toTrim.size(); i++) {
                String curTrim = toTrim.get(i);

                translatedRegName = translatedRegName.replaceFirst(curTrim, "");
            }
        }

        if (!MANUAL_TRANSLATIONS.containsKey(registryName)) builder.add(registryName, handleInput(translatedRegName));
    }

    private void localizeGeneralRegistryName(String registryName, String translatedName, TranslationBuilder builder) {
        localizeGeneralRegistryName(registryName, translatedName, List.of(), builder);
    }

    private void localizeGeneralRegistryName(String registryName, List<String> toTrim, TranslationBuilder builder) {
        localizeGeneralRegistryName(registryName, DEFAULT_SEPARATORS, toTrim, builder);
    }

    private void localizeGeneralRegistryName(String registryName, TranslationBuilder builder) {
        localizeGeneralRegistryName(registryName, List.of(), builder);
    }

    private void translateItems(TranslationBuilder builder) {
        TropicsItems.getRegisteredItems().values().forEach(itemRegEntry -> {
            Item itemEntry = itemRegEntry.get();
            String itemRegName = itemEntry.getDescriptionId();

            Tropics.LOGGER.debug("[Currently Translating Item]: " + itemRegName + " -> " + getTranslatedRegistryName(itemRegName));

            localizeGeneralRegistryName(itemRegName, builder);
        });
    }

    private void handleManualTranslations(TranslationBuilder builder) {
        MANUAL_TRANSLATIONS.forEach((translationKey, translationValue) -> {
            if ((translationKey == null || translationValue == null) || translationKey.isEmpty() || translationKey.isBlank() || translationValue.isEmpty() || translationValue.isBlank()) return;

            builder.add(translationKey, translationValue);
        });
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translateItems(translationBuilder);

        handleManualTranslations(translationBuilder);
    }
}

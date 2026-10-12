package com.brilliafy.magicstorage.storage;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class StationManager {
    public static final int STATION_SLOTS = 20;

    private boolean hasCraftingTable;
    private boolean hasFurnace;
    private boolean hasBrewingStand;
    private boolean hasAnvil;
    private boolean hasEnchantingTable;
    private boolean hasCuriosReforger;
    private boolean hasApotheosisScrapTome;
    private boolean hasApotheosisExtractionTome;
    private boolean hasQualityEquipment;

    public void updateStations(List<ItemStack> stations) {
        hasCraftingTable = false;
        hasFurnace = false;
        hasBrewingStand = false;
        hasAnvil = false;
        hasEnchantingTable = false;
        hasCuriosReforger = false;
        hasApotheosisScrapTome = false;
        hasApotheosisExtractionTome = false;
        hasQualityEquipment = false;

        for (ItemStack stack : stations) {
            if (stack.isEmpty()) continue;

            if (stack.is(Items.CRAFTING_TABLE)) {
                hasCraftingTable = true;
            } else if (stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE) || stack.is(Items.SMOKER)) {
                hasFurnace = true;
            } else if (stack.is(Items.BREWING_STAND)) {
                hasBrewingStand = true;
            } else if (stack.is(Items.ANVIL) || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL)) {
                hasAnvil = true;
            } else if (stack.is(Items.ENCHANTING_TABLE)) {
                hasEnchantingTable = true;
            }

            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String path = key.getPath();
            String namespace = key.getNamespace();

            if ("qualitytools".equals(namespace) || "quality_equipment".equals(namespace)) {
                if (path.contains("reforg")) {
                    hasQualityEquipment = true;
                    hasCuriosReforger = true;
                }
            } else if ("curios".equals(namespace) || "bountifulbaubles".equals(namespace)) {
                if (path.contains("reforg") || path.contains("anvil")) {
                    hasCuriosReforger = true;
                }
            } else if ("apotheosis".equals(namespace)) {
                if (path.contains("scrap") || path.equals("scrap_tome")) {
                    hasApotheosisScrapTome = true;
                }
                if (path.contains("extraction") || path.equals("extraction_tome")) {
                    hasApotheosisExtractionTome = true;
                }
            }
        }
    }

    public static boolean isAllowedStation(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(Items.CRAFTING_TABLE) || stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE)
                || stack.is(Items.SMOKER) || stack.is(Items.BREWING_STAND) || stack.is(Items.ANVIL)
                || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL) || stack.is(Items.ENCHANTING_TABLE)) {
            return true;
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String namespace = key.getNamespace();
        String path = key.getPath();

        if ("apotheosis".equals(namespace) && (path.contains("scrap") || path.contains("extraction") || path.contains("tome"))) {
            return true;
        }
        if (("curios".equals(namespace) || "bountifulbaubles".equals(namespace) || "qualitytools".equals(namespace) || "quality_equipment".equals(namespace))
                && (path.contains("reforg") || path.contains("anvil"))) {
            return true;
        }
        return false;
    }

    public boolean hasCraftingTable() { return hasCraftingTable; }
    public boolean hasFurnace() { return hasFurnace; }
    public boolean hasBrewingStand() { return hasBrewingStand; }
    public boolean hasAnvil() { return hasAnvil; }
    public boolean hasEnchantingTable() { return hasEnchantingTable; }
    public boolean hasCuriosReforger() { return hasCuriosReforger; }
    public boolean hasApotheosisScrapTome() { return hasApotheosisScrapTome; }
    public boolean hasApotheosisExtractionTome() { return hasApotheosisExtractionTome; }
    public boolean hasQualityEquipment() { return hasQualityEquipment; }
}

package com.brilliafy.magicstorage.storage;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class StationManager {
    public static final int STATION_SLOTS = 20;

    private boolean hasCraftingTable;
    private boolean hasFurnace;
    private boolean hasBrewingStand;
    private boolean hasAnvil;
    private boolean hasEnchantingTable;
    private boolean hasSmithingTable;
    private boolean hasGrindstone;
    private boolean hasStonecutter;
    private boolean hasLoom;
    private boolean hasCartographyTable;
    private boolean hasFletchingTable;
    private boolean hasCauldron;
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
        hasSmithingTable = false;
        hasGrindstone = false;
        hasStonecutter = false;
        hasLoom = false;
        hasCartographyTable = false;
        hasFletchingTable = false;
        hasCauldron = false;
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
            } else if (stack.is(Items.SMITHING_TABLE)) {
                hasSmithingTable = true;
            } else if (stack.is(Items.GRINDSTONE)) {
                hasGrindstone = true;
            } else if (stack.is(Items.STONECUTTER)) {
                hasStonecutter = true;
            } else if (stack.is(Items.LOOM)) {
                hasLoom = true;
            } else if (stack.is(Items.CARTOGRAPHY_TABLE)) {
                hasCartographyTable = true;
            } else if (stack.is(Items.FLETCHING_TABLE)) {
                hasFletchingTable = true;
            } else if (stack.is(Items.CAULDRON)) {
                hasCauldron = true;
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
                if (path.contains("reforg") || path.contains("anvil") || path.contains("token") || path.contains("tablet")) {
                    hasCuriosReforger = true;
                }
            } else if ("apotheosis".equals(namespace)) {
                if (path.contains("scrap") || path.equals("scrap_tome")) {
                    hasApotheosisScrapTome = true;
                }
                if (path.contains("extraction") || path.equals("extraction_tome")) {
                    hasApotheosisExtractionTome = true;
                }
                if (path.contains("salvag") || path.contains("reforg")) {
                    hasQualityEquipment = true;
                    hasCuriosReforger = true;
                }
            }
        }
    }

    public static boolean isAllowedStation(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(Items.CRAFTING_TABLE) || stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE)
                || stack.is(Items.SMOKER) || stack.is(Items.BREWING_STAND) || stack.is(Items.ANVIL)
                || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL) || stack.is(Items.ENCHANTING_TABLE)
                || stack.is(Items.SMITHING_TABLE) || stack.is(Items.GRINDSTONE) || stack.is(Items.STONECUTTER)
                || stack.is(Items.LOOM) || stack.is(Items.CARTOGRAPHY_TABLE) || stack.is(Items.FLETCHING_TABLE)
                || stack.is(Items.CAULDRON)) {
            return true;
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String namespace = key.getNamespace();
        String path = key.getPath();

        if ("apotheosis".equals(namespace) && (path.contains("scrap") || path.contains("extraction") || path.contains("tome") || path.contains("salvag") || path.contains("reforg"))) {
            return true;
        }
        if (("curios".equals(namespace) || "bountifulbaubles".equals(namespace) || "qualitytools".equals(namespace) || "quality_equipment".equals(namespace))
                && (path.contains("reforg") || path.contains("anvil") || path.contains("token") || path.contains("station") || path.contains("tablet"))) {
            return true;
        }
        return false;
    }

    public boolean hasCraftingTable() { return hasCraftingTable; }
    public boolean hasFurnace() { return hasFurnace; }
    public boolean hasBrewingStand() { return hasBrewingStand; }
    public boolean hasAnvil() { return hasAnvil; }
    public boolean hasEnchantingTable() { return hasEnchantingTable; }
    public boolean hasSmithingTable() { return hasSmithingTable; }
    public boolean hasGrindstone() { return hasGrindstone; }
    public boolean hasStonecutter() { return hasStonecutter; }
    public boolean hasLoom() { return hasLoom; }
    public boolean hasCartographyTable() { return hasCartographyTable; }
    public boolean hasFletchingTable() { return hasFletchingTable; }
    public boolean hasCauldron() { return hasCauldron; }
    public boolean hasCuriosReforger() { return hasCuriosReforger; }
    public boolean hasApotheosisScrapTome() { return hasApotheosisScrapTome; }
    public boolean hasApotheosisExtractionTome() { return hasApotheosisExtractionTome; }
    public boolean hasQualityEquipment() { return hasQualityEquipment; }
}

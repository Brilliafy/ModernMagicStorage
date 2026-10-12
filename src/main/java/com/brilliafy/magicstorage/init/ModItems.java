package com.brilliafy.magicstorage.init;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.item.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModItems {
    public static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();

    // Upgrades
    public static final Item UPGRADE_CRIMTANE = register("upgrade_crimtane", new ItemStorageUpgrade(1, 54, new Item.Properties().rarity(Rarity.COMMON)));
    public static final Item UPGRADE_DEMONITE = register("upgrade_demonite", new ItemStorageUpgrade(2, 54, new Item.Properties().rarity(Rarity.COMMON)));
    public static final Item UPGRADE_HELLSTONE = register("upgrade_hellstone", new ItemStorageUpgrade(3, 90, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item UPGRADE_HALLOWED = register("upgrade_hallowed", new ItemStorageUpgrade(4, 135, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item UPGRADE_BLUE_CHLOROPHYTE = register("upgrade_blue_chlorophyte", new ItemStorageUpgrade(5, 180, new Item.Properties().rarity(Rarity.RARE)));
    public static final Item UPGRADE_LUMINITE = register("upgrade_luminite", new ItemStorageUpgrade(6, 252, new Item.Properties().rarity(Rarity.RARE)));
    public static final Item UPGRADE_TERRA = register("upgrade_terra", new ItemStorageUpgrade(7, 360, new Item.Properties().rarity(Rarity.EPIC)));

    // Materials
    public static final Item STORAGE_COMPONENT = register("storage_component", new ItemMaterial(new Item.Properties()));
    public static final Item SHADOW_DIAMOND = register("shadow_diamond", new ItemMaterial(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item RADIANT_JEWEL = register("radiant_jewel", new ItemMaterial(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item TERRA_SINGULARITY = register("terra_singularity", new ItemMaterial(new Item.Properties().rarity(Rarity.EPIC)));
    public static final Item FRAGMENT_HELLSTONE = register("fragment_hellstone", new ItemMaterial(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item FRAGMENT_LUMINITE = register("fragment_luminite", new ItemMaterial(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item INGOT_CRIMTANE = register("ingot_crimtane", new ItemMaterial(new Item.Properties()));
    public static final Item INGOT_DEMONITE = register("ingot_demonite", new ItemMaterial(new Item.Properties()));
    public static final Item BAR_HELLSTONE = register("bar_hellstone", new ItemMaterial(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item INGOT_HALLOWED = register("ingot_hallowed", new ItemMaterial(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final Item BAR_CHLOROPHYTE = register("bar_chlorophyte", new ItemMaterial(new Item.Properties().rarity(Rarity.RARE)));
    public static final Item BAR_LUMINITE = register("bar_luminite", new ItemMaterial(new Item.Properties().rarity(Rarity.RARE)));

    // Portable Storage Remotes
    public static final Item PORTABLE_ACCESS_PREHM = register("portable_access_prehm", new ItemPortableAccess(200.0, false, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));
    public static final Item PORTABLE_ACCESS_HM = register("portable_access_hm", new ItemPortableAccess(-1.0, false, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item PORTABLE_ACCESS_ULTIMATE = register("portable_access_ultimate", new ItemPortableAccess(-1.0, true, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Portable Crafting Remotes
    public static final Item PORTABLE_CRAFTING_ACCESS_PREHM = register("portable_crafting_access_prehm", new ItemPortableCraftingAccess(200.0, false, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON)));
    public static final Item PORTABLE_CRAFTING_ACCESS_HM = register("portable_crafting_access_hm", new ItemPortableCraftingAccess(-1.0, false, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON)));
    public static final Item PORTABLE_CRAFTING_ACCESS_ULTIMATE = register("portable_crafting_access_ultimate", new ItemPortableCraftingAccess(-1.0, true, new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    static {
        // Register BlockItems for all blocks
        for (Map.Entry<ResourceLocation, Block> entry : ModBlocks.BLOCKS.entrySet()) {
            register(entry.getKey().getPath(), new BlockItem(entry.getValue(), new Item.Properties()));
        }
    }

    private static <T extends Item> T register(String name, T item) {
        //? > 1.19.2 {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        //?} <= 1.19.2 {
        /*ResourceLocation id = new ResourceLocation(Constants.MOD_ID, name);
        *///?}
        ITEMS.put(id, item);
        return item;
    }

    public static void registerAll() {
        //? fabric {
        for (Map.Entry<ResourceLocation, Item> entry : ITEMS.entrySet()) {
            Registry.register(BuiltInRegistries.ITEM, entry.getKey(), entry.getValue());
        }
        //?}
    }
}

package com.brilliafy.magicstorage.init;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.block.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModBlocks {
    public static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();

    public static final Block STORAGE_HEART = register("storage_heart", new BlockStorageHeart(
            BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL).lightLevel(state -> 15)));

    public static final Block STORAGE_ACCESS = register("storage_access", new BlockStorageAccess(
            BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)));

    public static final Block CRAFTING_ACCESS = register("crafting_access", new BlockCraftingAccess(
            BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)));

    public static final Block REMOTE_ACCESS = register("remote_access", new BlockRemoteAccess(
            BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)));

    public static final Block HELL_BRICK = register("hell_brick", new BlockHellBricks(
            BlockBehaviour.Properties.of().strength(2.0f, 6.0f).sound(SoundType.STONE)));

    public static final Block HELLSTONE_ORE = register("hellstone_ore", new BlockHellstoneOre(
            BlockBehaviour.Properties.of().strength(3.5f, 6.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    public static final Block LUMINITE_ORE = register("luminite_ore", new BlockLuminiteOre(
            BlockBehaviour.Properties.of().strength(4.0f, 9.0f).sound(SoundType.STONE).requiresCorrectToolForDrops()));

    // 8 Storage Unit tiers
    public static final Block STORAGE_UNIT_BASIC = register("storage_unit_basic", new BlockStorageUnit(
            0, 36, BlockBehaviour.Properties.of().strength(2.5f, 5.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_CRIMTANE = register("storage_unit_crimtane", new BlockStorageUnit(
            1, 54, BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_DEMONITE = register("storage_unit_demonite", new BlockStorageUnit(
            2, 54, BlockBehaviour.Properties.of().strength(3.0f, 6.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_HELLSTONE = register("storage_unit_hellstone", new BlockStorageUnit(
            3, 90, BlockBehaviour.Properties.of().strength(3.5f, 7.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_HALLOWED = register("storage_unit_hallowed", new BlockStorageUnit(
            4, 135, BlockBehaviour.Properties.of().strength(4.0f, 8.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_BLUE_CHLOROPHYTE = register("storage_unit_blue_chlorophyte", new BlockStorageUnit(
            5, 180, BlockBehaviour.Properties.of().strength(4.5f, 9.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_LUMINITE = register("storage_unit_luminite", new BlockStorageUnit(
            6, 252, BlockBehaviour.Properties.of().strength(5.0f, 10.0f).sound(SoundType.METAL)));

    public static final Block STORAGE_UNIT_TERRA = register("storage_unit_terra", new BlockStorageUnit(
            7, 360, BlockBehaviour.Properties.of().strength(6.0f, 12.0f).sound(SoundType.METAL)));

    private static <T extends Block> T register(String name, T block) {
        //? > 1.19.2 {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        //?} <= 1.19.2 {
        /*ResourceLocation id = new ResourceLocation(Constants.MOD_ID, name);
        *///?}
        BLOCKS.put(id, block);
        return block;
    }

    public static void registerAll() {
        //? fabric {
        for (Map.Entry<ResourceLocation, Block> entry : BLOCKS.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK, entry.getKey(), entry.getValue());
        }
        //?}
    }
}

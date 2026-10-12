package com.brilliafy.magicstorage.init;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.blockentity.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModBlockEntities {
    public static final Map<ResourceLocation, BlockEntityType<?>> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

    public static final BlockEntityType<StorageHeartBlockEntity> STORAGE_HEART = register(
            "storage_heart",
            BlockEntityType.Builder.of(StorageHeartBlockEntity::new, ModBlocks.STORAGE_HEART).build(null)
    );

    public static final BlockEntityType<StorageUnitBlockEntity> STORAGE_UNIT = register(
            "storage_unit",
            BlockEntityType.Builder.of(
                    StorageUnitBlockEntity::new,
                    ModBlocks.STORAGE_UNIT_BASIC,
                    ModBlocks.STORAGE_UNIT_CRIMTANE,
                    ModBlocks.STORAGE_UNIT_DEMONITE,
                    ModBlocks.STORAGE_UNIT_HELLSTONE,
                    ModBlocks.STORAGE_UNIT_HALLOWED,
                    ModBlocks.STORAGE_UNIT_BLUE_CHLOROPHYTE,
                    ModBlocks.STORAGE_UNIT_LUMINITE,
                    ModBlocks.STORAGE_UNIT_TERRA
            ).build(null)
    );

    public static final BlockEntityType<StorageAccessBlockEntity> STORAGE_ACCESS = register(
            "storage_access",
            BlockEntityType.Builder.of(StorageAccessBlockEntity::new, ModBlocks.STORAGE_ACCESS).build(null)
    );

    public static final BlockEntityType<CraftingAccessBlockEntity> CRAFTING_ACCESS = register(
            "crafting_access",
            BlockEntityType.Builder.of(CraftingAccessBlockEntity::new, ModBlocks.CRAFTING_ACCESS).build(null)
    );

    public static final BlockEntityType<RemoteAccessBlockEntity> REMOTE_ACCESS = register(
            "remote_access",
            BlockEntityType.Builder.of(RemoteAccessBlockEntity::new, ModBlocks.REMOTE_ACCESS).build(null)
    );

    private static <T extends BlockEntityType<?>> T register(String name, T type) {
        //? > 1.19.2 {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        //?} <= 1.19.2 {
        /*ResourceLocation id = new ResourceLocation(Constants.MOD_ID, name);
        *///?}
        BLOCK_ENTITY_TYPES.put(id, type);
        return type;
    }

    public static void registerAll() {
        //? fabric {
        for (Map.Entry<ResourceLocation, BlockEntityType<?>> entry : BLOCK_ENTITY_TYPES.entrySet()) {
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, entry.getKey(), entry.getValue());
        }
        //?}
    }
}

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
            createType(StorageHeartBlockEntity::new, ModBlocks.STORAGE_HEART)
    );

    public static final BlockEntityType<StorageUnitBlockEntity> STORAGE_UNIT = register(
            "storage_unit",
            createType(
                    StorageUnitBlockEntity::new,
                    ModBlocks.STORAGE_UNIT_BASIC,
                    ModBlocks.STORAGE_UNIT_CRIMTANE,
                    ModBlocks.STORAGE_UNIT_DEMONITE,
                    ModBlocks.STORAGE_UNIT_HELLSTONE,
                    ModBlocks.STORAGE_UNIT_HALLOWED,
                    ModBlocks.STORAGE_UNIT_BLUE_CHLOROPHYTE,
                    ModBlocks.STORAGE_UNIT_LUMINITE,
                    ModBlocks.STORAGE_UNIT_TERRA
            )
    );

    public static final BlockEntityType<StorageAccessBlockEntity> STORAGE_ACCESS = register(
            "storage_access",
            createType(StorageAccessBlockEntity::new, ModBlocks.STORAGE_ACCESS)
    );

    public static final BlockEntityType<CraftingAccessBlockEntity> CRAFTING_ACCESS = register(
            "crafting_access",
            createType(CraftingAccessBlockEntity::new, ModBlocks.CRAFTING_ACCESS)
    );

    public static final BlockEntityType<RemoteAccessBlockEntity> REMOTE_ACCESS = register(
            "remote_access",
            createType(RemoteAccessBlockEntity::new, ModBlocks.REMOTE_ACCESS)
    );

    private static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityType<T> createType(
            BlockEntityType.BlockEntitySupplier<T> supplier, Block... blocks) {
        //? >= 26.1 {
        /*return new BlockEntityType<>(supplier, java.util.Set.of(blocks));
        *///?} < 26.1 {
        return BlockEntityType.Builder.of(supplier, blocks).build(null);
        //?}
    }

    private static <T extends BlockEntityType<?>> T register(String name, T type) {
        ResourceLocation id = Constants.id(name);
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

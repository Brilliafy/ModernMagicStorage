package com.brilliafy.magicstorage.init;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.menu.*;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModMenus {
    public static final Map<ResourceLocation, MenuType<?>> MENU_TYPES = new LinkedHashMap<>();

    public static final MenuType<StorageAccessMenu> STORAGE_ACCESS = register(
            "storage_access",
            new MenuType<>(StorageAccessMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS)
    );

    public static final MenuType<CraftingAccessMenu> CRAFTING_ACCESS = register(
            "crafting_access",
            new MenuType<>(CraftingAccessMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS)
    );

    public static final MenuType<StorageUnitMenu> STORAGE_UNIT = register(
            "storage_unit",
            new MenuType<>(StorageUnitMenu::new, net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS)
    );

    private static <T extends MenuType<?>> T register(String name, T type) {
        ResourceLocation id = Constants.id(name);
        MENU_TYPES.put(id, type);
        return type;
    }

    public static void registerAll() {
        //? fabric {
        for (Map.Entry<ResourceLocation, MenuType<?>> entry : MENU_TYPES.entrySet()) {
            Registry.register(BuiltInRegistries.MENU, entry.getKey(), entry.getValue());
        }
        //?}
    }
}

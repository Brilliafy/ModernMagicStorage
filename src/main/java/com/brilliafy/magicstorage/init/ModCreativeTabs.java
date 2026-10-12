package com.brilliafy.magicstorage.init;

import com.brilliafy.magicstorage.Constants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {
    public static final ResourceLocation TAB_ID = Constants.id("main");

    public static final CreativeModeTab TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .title(Component.translatable("itemGroup.magicstorage"))
            .icon(() -> new ItemStack(ModBlocks.STORAGE_HEART))
            .displayItems((itemDisplayParameters, output) -> {
                ModItems.ITEMS.values().forEach(output::accept);
            })
            .build();

    public static void registerAll() {
        //? fabric {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_ID, TAB);
        //?}
    }
}

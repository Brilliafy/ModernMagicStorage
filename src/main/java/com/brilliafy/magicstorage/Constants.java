package com.brilliafy.magicstorage;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
    public static final String MOD_ID = "magicstorage";
    public static final String MOD_NAME = "Modern Magic Storage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static ResourceLocation id(String path) {
        //? > 1.20.1 {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
        //?} <= 1.20.1 {
        /*return new ResourceLocation(MOD_ID, path);
        *///?}
    }

    public static net.minecraft.world.InteractionResult success(net.minecraft.world.level.Level level) {
        //? >= 26.1 {
        /*return net.minecraft.world.InteractionResult.SUCCESS;
        *///?} < 26.1 {
        return net.minecraft.world.InteractionResult.sidedSuccess(level.isClientSide());
        //?}
    }
}

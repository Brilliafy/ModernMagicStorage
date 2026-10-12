package com.brilliafy.magicstorage.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

//? > 1.20.1 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.nbt.CompoundTag;
//?} <= 1.20.1 {
/*import net.minecraft.nbt.CompoundTag;
*///?}

public class ItemHelper {

    public static boolean canMerge(ItemStack a, ItemStack b) {
        if (a.isEmpty() || b.isEmpty()) return false;
        if (!a.is(b.getItem())) return false;
        //? > 1.20.1 {
        return ItemStack.isSameItemSameComponents(a, b);
        //?} <= 1.20.1 {
        /*return ItemStack.isSameItemSameTags(a, b);
        *///?}
    }

    public static void bindRemote(ItemStack stack, BlockPos pos, String dimension) {
        //? > 1.20.1 {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
            tag.putInt("HeartX", pos.getX());
            tag.putInt("HeartY", pos.getY());
            tag.putInt("HeartZ", pos.getZ());
            tag.putString("HeartDim", dimension);
        });
        //?} <= 1.20.1 {
        /*CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("HeartX", pos.getX());
        tag.putInt("HeartY", pos.getY());
        tag.putInt("HeartZ", pos.getZ());
        tag.putString("HeartDim", dimension);
        *///?}
    }

    public static CompoundTag getRemoteTag(ItemStack stack) {
        //? > 1.20.1 {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null ? customData.copyTag() : null;
        //?} <= 1.20.1 {
        /*return stack.getTag();
        *///?}
    }

    public static String getString(CompoundTag tag, String key) {
        //? >= 26.1 {
        /*return tag.getStringOr(key, "");
        *///?} < 26.1 {
        return tag.getString(key);
        //?}
    }

    public static int getInt(CompoundTag tag, String key) {
        //? >= 26.1 {
        /*return tag.getIntOr(key, 0);
        *///?} < 26.1 {
        return tag.getInt(key);
        //?}
    }

    public static void displayClientMessage(net.minecraft.world.entity.player.Player player, net.minecraft.network.chat.Component component, boolean overlay) {
        //? >= 26.1 {
        /*if (overlay) {
            player.sendOverlayMessage(component);
        } else {
            player.sendSystemMessage(component);
        }
        *///?} < 26.1 {
        player.displayClientMessage(component, overlay);
        //?}
    }
}

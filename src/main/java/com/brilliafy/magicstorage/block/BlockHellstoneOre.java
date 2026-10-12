package com.brilliafy.magicstorage.block;

import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.util.valueproviders.UniformInt;

public class BlockHellstoneOre extends DropExperienceBlock {

    public BlockHellstoneOre(Properties properties) {
        // Drops 2-5 XP when mined without Silk Touch
        //? > 1.20.1 {
        super(UniformInt.of(2, 5), properties);
        //?} <= 1.20.1 {
        /*super(properties, UniformInt.of(2, 5));
        *///?}
    }
}

package com.brilliafy.magicstorage.block;

import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.util.valueproviders.UniformInt;

public class BlockLuminiteOre extends DropExperienceBlock {

    public BlockLuminiteOre(Properties properties) {
        // Drops 3-7 XP when mined without Silk Touch
        //? > 1.20.1 {
        super(UniformInt.of(3, 7), properties);
        //?} <= 1.20.1 {
        /*super(properties, UniformInt.of(3, 7));
        *///?}
    }
}

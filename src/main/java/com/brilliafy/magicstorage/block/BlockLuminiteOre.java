package com.brilliafy.magicstorage.block;

import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.util.valueproviders.UniformInt;

public class BlockLuminiteOre extends DropExperienceBlock {

    public BlockLuminiteOre(Properties properties) {
        // Drops 3-7 XP when mined without Silk Touch
        super(UniformInt.of(3, 7), properties);
    }
}

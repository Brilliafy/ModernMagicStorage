package com.brilliafy.magicstorage.block;

import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.util.valueproviders.UniformInt;

public class BlockHellstoneOre extends DropExperienceBlock {

    public BlockHellstoneOre(Properties properties) {
        // Drops 2-5 XP when mined without Silk Touch
        super(UniformInt.of(2, 5), properties);
    }
}

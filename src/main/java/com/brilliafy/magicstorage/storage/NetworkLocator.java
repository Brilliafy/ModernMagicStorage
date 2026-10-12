package com.brilliafy.magicstorage.storage;

import com.brilliafy.magicstorage.block.BlockCraftingAccess;
import com.brilliafy.magicstorage.block.BlockRemoteAccess;
import com.brilliafy.magicstorage.block.BlockStorageAccess;
import com.brilliafy.magicstorage.block.BlockStorageUnit;
import com.brilliafy.magicstorage.blockentity.StorageHeartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class NetworkLocator {

    /**
     * Marks every Storage Heart reachable from the given position (and its immediate
     * neighbours) as needing a network rescan. Called when a network block is placed
     * or removed so that newly added/removed units are picked up without a reload.
     */
    public static void markNetworkDirty(Level level, BlockPos pos) {
        if (level == null || level.isClientSide()) return;

        Set<StorageHeartBlockEntity> hearts = new HashSet<>();
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            BlockEntity be = level.getBlockEntity(neighbor);
            if (be instanceof StorageHeartBlockEntity heart) {
                hearts.add(heart);
            } else {
                StorageHeartBlockEntity found = findConnectedHeart(level, neighbor);
                if (found != null) hearts.add(found);
            }
        }
        hearts.forEach(StorageHeartBlockEntity::markNetworkDirty);
    }

    public static StorageHeartBlockEntity findConnectedHeart(Level level, BlockPos startPos) {
        if (level == null) return null;

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        int maxSearch = 512;
        while (!queue.isEmpty() && visited.size() < maxSearch) {
            BlockPos current = queue.poll();

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = current.relative(dir);
                if (visited.add(neighbor)) {
                    BlockEntity be = level.getBlockEntity(neighbor);
                    if (be instanceof StorageHeartBlockEntity heart) {
                        return heart;
                    }

                    BlockState state = level.getBlockState(neighbor);
                    if (state.getBlock() instanceof BlockStorageUnit
                            || state.getBlock() instanceof BlockStorageAccess
                            || state.getBlock() instanceof BlockCraftingAccess
                            || state.getBlock() instanceof BlockRemoteAccess) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        return null;
    }
}

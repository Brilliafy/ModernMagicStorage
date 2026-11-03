package com.brilliafy.magicstorage.storage;

import com.brilliafy.magicstorage.compat.apotheosis.ApotheosisCompat;
import com.brilliafy.magicstorage.compat.farmersdelight.FarmersDelightCompat;
import com.brilliafy.magicstorage.compat.tetra.TetraCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class StationManager {
    public static final int STATION_SLOTS = 20;

    private boolean hasCraftingTable;
    private boolean hasFurnace;
    private boolean hasBlastFurnace;
    private boolean hasSmoker;
    private boolean hasBrewingStand;
    private boolean hasAnvil;
    private boolean hasEnchantingTable;
    private boolean hasSmithingTable;
    private boolean hasGrindstone;
    private boolean hasStonecutter;
    private boolean hasLoom;
    private boolean hasCartographyTable;
    private boolean hasFletchingTable;
    private boolean hasCauldron;
    private boolean hasCuriosReforger;
    private boolean hasQualityEquipment;
    private boolean hasApotheosisReforgingTable;
    private boolean hasApotheosisSalvagingTable;
    private boolean hasApotheosisGemCuttingTable;
    private boolean hasTetraModularWorkbench;
    private boolean hasFarmersDelightCookingPot;
    private boolean hasFarmersDelightCuttingBoard;
    private boolean hasFarmersDelightSkillet;
    private boolean hasFarmersDelightStove;

    public void updateStations(List<ItemStack> stations) {
        hasCraftingTable = false;
        hasFurnace = false;
        hasBlastFurnace = false;
        hasSmoker = false;
        hasBrewingStand = false;
        hasAnvil = false;
        hasEnchantingTable = false;
        hasSmithingTable = false;
        hasGrindstone = false;
        hasStonecutter = false;
        hasLoom = false;
        hasCartographyTable = false;
        hasFletchingTable = false;
        hasCauldron = false;
        hasCuriosReforger = false;
        hasQualityEquipment = false;
        hasApotheosisReforgingTable = false;
        hasApotheosisSalvagingTable = false;
        hasApotheosisGemCuttingTable = false;
        hasTetraModularWorkbench = false;
        hasFarmersDelightCookingPot = false;
        hasFarmersDelightCuttingBoard = false;
        hasFarmersDelightSkillet = false;
        hasFarmersDelightStove = false;

        for (ItemStack stack : stations) {
            if (stack.isEmpty()) continue;

            if (stack.is(Items.CRAFTING_TABLE)) {
                hasCraftingTable = true;
            } else if (stack.is(Items.FURNACE)) {
                hasFurnace = true;
            } else if (stack.is(Items.BLAST_FURNACE)) {
                hasBlastFurnace = true;
                hasFurnace = true;
            } else if (stack.is(Items.SMOKER)) {
                hasSmoker = true;
                hasFurnace = true;
            } else if (stack.is(Items.BREWING_STAND)) {
                hasBrewingStand = true;
            } else if (stack.is(Items.ANVIL) || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL)) {
                hasAnvil = true;
            } else if (stack.is(Items.ENCHANTING_TABLE)) {
                hasEnchantingTable = true;
            } else if (stack.is(Items.SMITHING_TABLE)) {
                hasSmithingTable = true;
            } else if (stack.is(Items.GRINDSTONE)) {
                hasGrindstone = true;
            } else if (stack.is(Items.STONECUTTER)) {
                hasStonecutter = true;
            } else if (stack.is(Items.LOOM)) {
                hasLoom = true;
            } else if (stack.is(Items.CARTOGRAPHY_TABLE)) {
                hasCartographyTable = true;
            } else if (stack.is(Items.FLETCHING_TABLE)) {
                hasFletchingTable = true;
            } else if (stack.is(Items.CAULDRON)) {
                hasCauldron = true;
            }

            if (ApotheosisCompat.isReforgingTable(stack)) {
                hasApotheosisReforgingTable = true;
                hasCuriosReforger = true;
            } else if (ApotheosisCompat.isSalvagingTable(stack)) {
                hasApotheosisSalvagingTable = true;
            } else if (ApotheosisCompat.isGemCuttingTable(stack)) {
                hasApotheosisGemCuttingTable = true;
            }

            if (TetraCompat.isModularWorkbench(stack)) {
                hasTetraModularWorkbench = true;
            }

            if (FarmersDelightCompat.isCookingPot(stack)) {
                hasFarmersDelightCookingPot = true;
            } else if (FarmersDelightCompat.isCuttingBoard(stack)) {
                hasFarmersDelightCuttingBoard = true;
            } else if (FarmersDelightCompat.isSkillet(stack)) {
                hasFarmersDelightSkillet = true;
                hasFurnace = true;
            } else if (FarmersDelightCompat.isStove(stack)) {
                hasFarmersDelightStove = true;
                hasFurnace = true;
                hasSmoker = true;
            }

            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String path = key.getPath();
            String namespace = key.getNamespace();

            if ("qualitytools".equals(namespace) || "quality_equipment".equals(namespace)) {
                if (path.contains("reforg")) {
                    hasQualityEquipment = true;
                    hasCuriosReforger = true;
                }
            } else if ("curios".equals(namespace) || "bountifulbaubles".equals(namespace)) {
                if (path.contains("reforg") || path.contains("anvil") || path.contains("token") || path.contains("tablet")) {
                    hasCuriosReforger = true;
                }
            }
        }
    }

    public static boolean isAllowedStation(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return hasGuiWhenInteracted(stack);
    }

    public static boolean isBuiltInStation(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(Items.CRAFTING_TABLE) || stack.is(Items.FURNACE) || stack.is(Items.BLAST_FURNACE)
                || stack.is(Items.SMOKER) || stack.is(Items.BREWING_STAND) || stack.is(Items.ANVIL)
                || stack.is(Items.CHIPPED_ANVIL) || stack.is(Items.DAMAGED_ANVIL) || stack.is(Items.ENCHANTING_TABLE)
                || stack.is(Items.SMITHING_TABLE) || stack.is(Items.GRINDSTONE) || stack.is(Items.STONECUTTER)
                || stack.is(Items.LOOM) || stack.is(Items.CARTOGRAPHY_TABLE) || stack.is(Items.FLETCHING_TABLE)
                || stack.is(Items.CAULDRON)) {
            return true;
        }

        if (ApotheosisCompat.isApotheosisStation(stack)) {
            return true;
        }

        if (TetraCompat.isModularWorkbench(stack)) {
            return true;
        }

        if (FarmersDelightCompat.isFarmersDelightStation(stack)) {
            return true;
        }

        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        String namespace = key.getNamespace();
        String path = key.getPath();

        if (("curios".equals(namespace) || "bountifulbaubles".equals(namespace) || "qualitytools".equals(namespace) || "quality_equipment".equals(namespace))
                && (path.contains("reforg") || path.contains("anvil") || path.contains("token") || path.contains("station") || path.contains("tablet"))) {
            return true;
        }
        return false;
    }

    public static boolean hasGuiWhenInteracted(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (isBuiltInStation(stack)) return true;

        if (stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
            net.minecraft.world.level.block.Block block = blockItem.getBlock();
            if (isExcludedBlock(block)) return false;

            // 1. Direct MenuProvider on Block
            if (block instanceof net.minecraft.world.MenuProvider) {
                return true;
            }

            // 2. BlockEntity that implements MenuProvider or Container
            if (block instanceof net.minecraft.world.level.block.EntityBlock entityBlock) {
                try {
                    net.minecraft.world.level.block.entity.BlockEntity be = entityBlock.newBlockEntity(
                            net.minecraft.core.BlockPos.ZERO, block.defaultBlockState()
                    );
                    if (be instanceof net.minecraft.world.MenuProvider || be instanceof net.minecraft.world.Container) {
                        return true;
                    }
                    if (be != null) {
                        String name = be.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT);
                        if (name.contains("menu") || name.contains("container") || name.contains("gui")
                                || name.contains("screen") || name.contains("station") || name.contains("crafter")
                                || name.contains("crafting") || name.contains("furnace") || name.contains("smelter")
                                || name.contains("bench") || name.contains("table") || name.contains("machine")) {
                            return true;
                        }
                    }
                } catch (Throwable ignored) {}
            }

            // 3. MenuProvider from default block state
            try {
                if (block.defaultBlockState().getMenuProvider(null, net.minecraft.core.BlockPos.ZERO) != null) {
                    return true;
                }
            } catch (Throwable ignored) {}

            // 4. Overridden interaction method on Block class
            Class<?> bClass = block.getClass();
            while (bClass != null && bClass != net.minecraft.world.level.block.Block.class
                    && bClass != net.minecraft.world.level.block.state.BlockBehaviour.class) {
                for (java.lang.reflect.Method m : bClass.getDeclaredMethods()) {
                    String mName = m.getName();
                    if (mName.equals("use") || mName.equals("useWithoutItem") || mName.equals("useItemOn")
                            || mName.equals("getMenuProvider") || mName.equals("openScreen")) {
                        return true;
                    }
                }
                bClass = bClass.getSuperclass();
            }

            // 5. Heuristic based on registry path or class name
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String path = id.getPath().toLowerCase(java.util.Locale.ROOT);
            if (isStationKeyword(path)) {
                return true;
            }

            String className = block.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT);
            if (isStationKeyword(className)) {
                return true;
            }
        }

        if (stack.getItem() instanceof net.minecraft.world.MenuProvider) {
            return true;
        }

        return false;
    }

    private static boolean isExcludedBlock(net.minecraft.world.level.block.Block block) {
        if (block instanceof net.minecraft.world.level.block.DoorBlock
                || block instanceof net.minecraft.world.level.block.TrapDoorBlock
                || block instanceof net.minecraft.world.level.block.FenceGateBlock
                || block instanceof net.minecraft.world.level.block.ButtonBlock
                || block instanceof net.minecraft.world.level.block.LeverBlock
                || block instanceof net.minecraft.world.level.block.PressurePlateBlock
                || block instanceof net.minecraft.world.level.block.SignBlock
                || block instanceof net.minecraft.world.level.block.BedBlock
                || block instanceof net.minecraft.world.level.block.BellBlock
                || block instanceof net.minecraft.world.level.block.CandleBlock
                || block instanceof net.minecraft.world.level.block.CakeBlock
                || block instanceof net.minecraft.world.level.block.FlowerPotBlock
                || block instanceof net.minecraft.world.level.block.CropBlock
                || block instanceof net.minecraft.world.level.block.SaplingBlock
                || block instanceof net.minecraft.world.level.block.StairBlock
                || block instanceof net.minecraft.world.level.block.SlabBlock
                || block instanceof net.minecraft.world.level.block.WallBlock
                || block instanceof net.minecraft.world.level.block.FenceBlock) {
            return true;
        }
        String name = block.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT);
        return name.contains("door") || name.contains("gate") || name.contains("button")
                || name.contains("lever") || name.contains("pressure") || name.contains("sign")
                || name.contains("bed") || name.contains("slab") || name.contains("stair")
                || name.contains("fence") || name.contains("wall");
    }

    private static boolean isStationKeyword(String str) {
        return str.contains("table") || str.contains("station") || str.contains("bench")
                || str.contains("furnace") || str.contains("smelter") || str.contains("oven")
                || str.contains("kiln") || str.contains("forge") || str.contains("anvil")
                || str.contains("loom") || str.contains("cutter") || str.contains("saw")
                || str.contains("press") || str.contains("crusher") || str.contains("grinder")
                || str.contains("altar") || str.contains("mill") || str.contains("pot")
                || str.contains("board") || str.contains("workbench") || str.contains("workstation")
                || str.contains("machine") || str.contains("crafter") || str.contains("crafting")
                || str.contains("infuser") || str.contains("charger") || str.contains("assembler")
                || str.contains("fabricator") || str.contains("reforg") || str.contains("salvag")
                || str.contains("honing") || str.contains("vat") || str.contains("distiller")
                || str.contains("boiler") || str.contains("pulverizer") || str.contains("generator")
                || str.contains("processor") || str.contains("brewing") || str.contains("cooker")
                || str.contains("kitchen") || str.contains("cauldron");
    }

    public boolean hasCraftingTable() { return hasCraftingTable; }
    public boolean hasFurnace() { return hasFurnace; }
    public boolean hasBlastFurnace() { return hasBlastFurnace; }
    public boolean hasSmoker() { return hasSmoker; }
    public boolean hasBrewingStand() { return hasBrewingStand; }
    public boolean hasAnvil() { return hasAnvil; }
    public boolean hasEnchantingTable() { return hasEnchantingTable; }
    public boolean hasSmithingTable() { return hasSmithingTable; }
    public boolean hasGrindstone() { return hasGrindstone; }
    public boolean hasStonecutter() { return hasStonecutter; }
    public boolean hasLoom() { return hasLoom; }
    public boolean hasCartographyTable() { return hasCartographyTable; }
    public boolean hasFletchingTable() { return hasFletchingTable; }
    public boolean hasCauldron() { return hasCauldron; }
    public boolean hasCuriosReforger() { return hasCuriosReforger; }
    public boolean hasQualityEquipment() { return hasQualityEquipment; }
    public boolean hasApotheosisReforgingTable() { return hasApotheosisReforgingTable; }
    public boolean hasApotheosisSalvagingTable() { return hasApotheosisSalvagingTable; }
    public boolean hasApotheosisGemCuttingTable() { return hasApotheosisGemCuttingTable; }
    public boolean hasTetraModularWorkbench() { return hasTetraModularWorkbench; }
    public boolean hasFarmersDelightCookingPot() { return hasFarmersDelightCookingPot; }
    public boolean hasFarmersDelightCuttingBoard() { return hasFarmersDelightCuttingBoard; }
    public boolean hasFarmersDelightSkillet() { return hasFarmersDelightSkillet; }
    public boolean hasFarmersDelightStove() { return hasFarmersDelightStove; }
}

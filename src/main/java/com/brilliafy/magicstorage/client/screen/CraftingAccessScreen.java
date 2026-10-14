package com.brilliafy.magicstorage.client.screen;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.compat.apotheosis.ApotheosisCompat;
import com.brilliafy.magicstorage.compat.farmersdelight.FarmersDelightCompat;
import com.brilliafy.magicstorage.compat.tetra.TetraCompat;
import com.brilliafy.magicstorage.menu.CraftingAccessMenu;
import com.brilliafy.magicstorage.storage.StationMode;
import com.brilliafy.magicstorage.storage.StationRecipeManager;
//? >= 26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.input.MouseButtonEvent;
*///?} < 26.1 {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CraftingAccessScreen extends AbstractContainerScreen<CraftingAccessMenu> {
    private static final ResourceLocation TEXTURE = Constants.id("textures/gui/request.png");
    private static final ResourceLocation PANEL_TEXTURE = Constants.id("textures/gui/station_panel.png");
    private static final ResourceLocation OVERLAY_TEXTURE = Constants.id("textures/gui/storage_overlay.png");
    private static final ResourceLocation WIDGETS_TEXTURE = Constants.id("textures/gui/station_widgets.png");

    private EditBox searchBox;
    private int stationScrollOffset = 0;
    private int overlayScrollRow = 0;

    public record StationEntry(StationMode mode, ItemStack icon, Component name, int stationSlot) {
        public StationEntry(StationMode mode, ItemStack icon, Component name) {
            this(mode, icon, name, -1);
        }
    }

    //? >= 26.1 {
    /*public CraftingAccessScreen(CraftingAccessMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 206, 256);
    }
    *///?} < 26.1 {
    public CraftingAccessScreen(CraftingAccessMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 206;
        this.imageHeight = 256;
    }
    //?}

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, this.leftPos + 80, this.topPos + 5, 90, 12,
                Component.translatable("gui.magicstorage.search"));
        this.searchBox.setMaxLength(32);
        this.searchBox.setResponder(this::sendSearch);
        this.addRenderableWidget(this.searchBox);
    }

    private void sendSearch(String text) {
        if (this.minecraft == null || this.minecraft.gameMode == null) {
            return;
        }
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.SEARCH_CLEAR);
        String lower = text.toLowerCase(Locale.ROOT);
        for (int i = 0; i < lower.length(); i++) {
            int index = CraftingAccessMenu.SEARCH_ALPHABET.indexOf(lower.charAt(i));
            if (index >= 0) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
            }
        }
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.SEARCH_COMMIT);
    }

    public List<StationEntry> getInstalledStations() {
        List<StationEntry> list = new ArrayList<>();
        list.add(new StationEntry(StationMode.CRAFTING, new ItemStack(Items.CRAFTING_TABLE), Component.literal("Crafting Table")));

        boolean hasFurnace = false;
        boolean hasStonecutter = false;
        boolean hasSmithing = false;
        boolean hasGrindstone = false;
        boolean hasLoom = false;
        boolean hasCartography = false;
        boolean hasAnvil = false;
        boolean hasCookingPot = false;
        boolean hasCuttingBoard = false;
        boolean hasReforging = false;
        boolean hasSalvaging = false;
        boolean hasGemCutting = false;
        boolean hasTetra = false;

        for (int i = 0; i < this.menu.getStationContainer().getContainerSize(); i++) {
            ItemStack s = this.menu.getStationContainer().getItem(i);
            if (s.isEmpty()) continue;

            if (!hasFurnace && (s.is(Items.FURNACE) || s.is(Items.BLAST_FURNACE) || s.is(Items.SMOKER))) {
                hasFurnace = true;
                list.add(new StationEntry(StationMode.FURNACE, s, Component.literal("Virtual Furnace")));
            } else if (!hasStonecutter && s.is(Items.STONECUTTER)) {
                hasStonecutter = true;
                list.add(new StationEntry(StationMode.STONECUTTER, s, Component.literal("Stonecutter")));
            } else if (!hasSmithing && s.is(Items.SMITHING_TABLE)) {
                hasSmithing = true;
                list.add(new StationEntry(StationMode.SMITHING, s, Component.literal("Smithing Table")));
            } else if (!hasGrindstone && s.is(Items.GRINDSTONE)) {
                hasGrindstone = true;
                list.add(new StationEntry(StationMode.GRINDSTONE, s, Component.literal("Grindstone")));
            } else if (!hasLoom && s.is(Items.LOOM)) {
                hasLoom = true;
                list.add(new StationEntry(StationMode.LOOM, s, Component.literal("Loom")));
            } else if (!hasCartography && s.is(Items.CARTOGRAPHY_TABLE)) {
                hasCartography = true;
                list.add(new StationEntry(StationMode.CARTOGRAPHY, s, Component.literal("Cartography Table")));
            } else if (!hasAnvil && (s.is(Items.ANVIL) || s.is(Items.CHIPPED_ANVIL) || s.is(Items.DAMAGED_ANVIL))) {
                hasAnvil = true;
                list.add(new StationEntry(StationMode.ANVIL, s, Component.literal("Anvil")));
            } else if (!hasCookingPot && FarmersDelightCompat.isCookingPot(s)) {
                hasCookingPot = true;
                list.add(new StationEntry(StationMode.FD_COOKING, s, Component.literal("Cooking Pot")));
            } else if (!hasCuttingBoard && FarmersDelightCompat.isCuttingBoard(s)) {
                hasCuttingBoard = true;
                list.add(new StationEntry(StationMode.FD_CUTTING, s, Component.literal("Cutting Board")));
            } else if (!hasReforging && ApotheosisCompat.isReforgingTable(s)) {
                hasReforging = true;
                list.add(new StationEntry(StationMode.APOTH_REFORGING, s, Component.literal("Reforging Table")));
            } else if (!hasSalvaging && ApotheosisCompat.isSalvagingTable(s)) {
                hasSalvaging = true;
                list.add(new StationEntry(StationMode.APOTH_SALVAGING, s, Component.literal("Salvaging Table")));
            } else if (!hasGemCutting && ApotheosisCompat.isGemCuttingTable(s)) {
                hasGemCutting = true;
                list.add(new StationEntry(StationMode.APOTH_GEM_CUTTING, s, Component.literal("Gem Cutting Table")));
            } else if (!hasTetra && TetraCompat.isModularWorkbench(s)) {
                hasTetra = true;
                list.add(new StationEntry(StationMode.TETRA_WORKBENCH, s, Component.literal("Modular Workbench")));
            } else {
                list.add(new StationEntry(StationMode.MODDED, s, s.getHoverName(), i));
            }
        }
        return list;
    }

    //? > 1.20.1 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (handleCustomScroll(mouseX, mouseY, scrollY)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
    //?} <= 1.20.1 {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (handleCustomScroll(mouseX, mouseY, delta)) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    *///?}

    private boolean handleCustomScroll(double mouseX, double mouseY, double delta) {
        if (delta == 0) return false;

        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        if (mouseX >= panelX && mouseX <= panelX + 32 && mouseY >= panelY && mouseY <= panelY + 160) {
            List<StationEntry> stations = getInstalledStations();
            int maxScroll = Math.max(0, stations.size() - 6);
            this.stationScrollOffset = Math.max(0, Math.min(maxScroll, this.stationScrollOffset - (int) Math.signum(delta)));
            return true;
        }

        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        if (mouseX >= overlayX && mouseX <= overlayX + 74 && mouseY >= overlayY && mouseY <= overlayY + 180) {
            int maxRow = Math.max(0, (this.menu.getDisplaySlots() / 3) - 7);
            int newRow = this.overlayScrollRow - (int) Math.signum(delta);
            if (newRow < 0) {
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.PAGE_UP);
                }
                this.overlayScrollRow = 0;
            } else if (newRow > maxRow) {
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.PAGE_DOWN);
                }
                this.overlayScrollRow = maxRow;
            } else {
                this.overlayScrollRow = newRow;
            }
            return true;
        }

        if (this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
                    delta > 0 ? CraftingAccessMenu.PAGE_UP : CraftingAccessMenu.PAGE_DOWN);
            return true;
        }
        return false;
    }

    private boolean handleCustomClick(double mouseX, double mouseY, int button, boolean isShift) {
        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        if (mouseX >= panelX && mouseX <= panelX + 32 && mouseY >= panelY + 4 && mouseY < panelY + 4 + 6 * 25) {
            List<StationEntry> stations = getInstalledStations();
            int clickedSlot = (int) ((mouseY - (panelY + 4)) / 25);
            int idx = clickedSlot + this.stationScrollOffset;
            if (idx >= 0 && idx < stations.size()) {
                StationEntry entry = stations.get(idx);
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    if (entry.mode() == StationMode.MODDED) {
                        int packetId = isShift
                                ? CraftingAccessMenu.OPEN_MODDED_GUI_BASE + entry.stationSlot()
                                : CraftingAccessMenu.MODDED_STATION_SELECT_BASE + entry.stationSlot();
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, packetId);
                    } else {
                        this.minecraft.gameMode.handleInventoryButtonClick(
                                this.menu.containerId, CraftingAccessMenu.STATION_SELECT_BASE + entry.mode().getId()
                        );
                    }
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                }
                return true;
            }
        }

        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        if (mouseX >= overlayX && mouseX <= overlayX + 74 && mouseY >= overlayY && mouseY <= overlayY + 180) {
            if (this.minecraft != null && this.minecraft.gameMode != null) {
                if (!this.menu.getCarried().isEmpty()) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.OVERLAY_DEPOSIT);
                    this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    return true;
                }

                if (mouseX >= overlayX + 4 && mouseX < overlayX + 58 && mouseY >= overlayY + 18 && mouseY < overlayY + 18 + 7 * 19) {
                    int col = (int) ((mouseX - (overlayX + 4)) / 18);
                    int row = (int) ((mouseY - (overlayY + 18)) / 19) + this.overlayScrollRow;
                    int slot = row * 3 + col;
                    if (slot >= 0 && slot < this.menu.getDisplaySlots()) {
                        if (isShift) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.OVERLAY_CLICK_BASE + 100 + slot);
                        } else if (button == 1) {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.OVERLAY_CLICK_BASE + 50 + slot);
                        } else {
                            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.OVERLAY_CLICK_BASE + slot);
                        }
                        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return true;
                    }
                }
            }
        }

        if (this.menu.getActiveMode() == StationMode.STONECUTTER) {
            ItemStack in = this.menu.getCraftSlots().getItem(0);
            List<ItemStack> variants = StationRecipeManager.getStonecutterVariants(in, this.menu.getLevel());
            for (int v = 0; v < Math.min(variants.size(), 8); v++) {
                int c = v % 4;
                int r = v / 4;
                int bx = this.leftPos + 44 + c * 18;
                int by = this.topPos + 124 + r * 19;
                if (mouseX >= bx && mouseX < bx + 16 && mouseY >= by && mouseY < by + 18) {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.RECIPE_SELECT_BASE + v);
                        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    return true;
                }
            }
        }

        if (this.menu.getActiveMode() == StationMode.MODDED) {
            int moddedSlot = this.menu.getSelectedModdedStationSlot();
            if (moddedSlot >= 0 && moddedSlot < this.menu.getStationContainer().getContainerSize()) {
                if (mouseX >= this.leftPos + 8 && mouseX <= this.leftPos + 24 && mouseY >= this.topPos + 92 && mouseY <= this.topPos + 108) {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, CraftingAccessMenu.OPEN_MODDED_GUI_BASE + moddedSlot);
                        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    }
                    return true;
                }
            }
        }

        return false;
    }

    //? >= 26.1 {
    /*@Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (handleCustomClick(event.x(), event.y(), event.button(), event.hasShiftDown())) {
            return true;
        }
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0f, 0.0f,
                this.imageWidth, this.imageHeight, 256, 256);
        drawCustomComponentsModern(graphics, mouseX, mouseY);
    }

    private void drawCustomComponentsModern(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        StationMode mode = this.menu.getActiveMode();

        // 1. Center Station Interface Overlay
        if (mode != StationMode.CRAFTING && mode != StationMode.MODDED) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 7, this.topPos + 105, 0.0f, 0.0f, 150, 64, 256, 256);

            if (mode == StationMode.FURNACE) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 55, this.topPos + 116, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 55, this.topPos + 152, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 115, this.topPos + 134, 160.0f, 0.0f, 26, 26, 256, 256);

                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 57, this.topPos + 137, 0.0f, 70.0f, 14, 14, 256, 256);
                if (this.menu.getFurnaceLitTime() > 0) {
                    int maxLit = Math.max(1, this.menu.getFurnaceLitDuration());
                    int litH = Math.min(14, this.menu.getFurnaceLitTime() * 14 / maxLit);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 57, this.topPos + 137 + (14 - litH), 20.0f, 70.0f + (14 - litH), 14, litH, 256, 256);
                }

                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 80, this.topPos + 134, 40.0f, 70.0f, 24, 17, 256, 256);
                if (this.menu.getFurnaceCookingProgress() > 0) {
                    int maxCook = Math.max(1, this.menu.getFurnaceCookingTotalTime());
                    int progressW = Math.min(24, this.menu.getFurnaceCookingProgress() * 24 / maxCook);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 80, this.topPos + 134, 70.0f, 70.0f, progressW, 17, 256, 256);
                }
            } else if (mode == StationMode.STONECUTTER) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 19, this.topPos + 132, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 142, this.topPos + 132, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 116, this.topPos + 134, 40.0f, 70.0f, 24, 17, 256, 256);

                ItemStack in = this.menu.getCraftSlots().getItem(0);
                List<ItemStack> variants = StationRecipeManager.getStonecutterVariants(in, this.menu.getLevel());
                int selected = this.menu.getSelectedRecipeIndex();
                for (int v = 0; v < Math.min(variants.size(), 8); v++) {
                    int c = v % 4;
                    int r = v / 4;
                    int bx = this.leftPos + 44 + c * 18;
                    int by = this.topPos + 124 + r * 19;
                    float u = (v == selected) ? 140.0f : 100.0f;
                    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, bx, by, u, 70.0f, 16, 18, 256, 256);
                    graphics.item(variants.get(v), bx, by + 1);
                }
            } else if (mode == StationMode.SMITHING) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 17, this.topPos + 129, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 41, this.topPos + 129, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 65, this.topPos + 129, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 123, this.topPos + 129, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 92, this.topPos + 130, 40.0f, 70.0f, 24, 17, 256, 256);
            } else if (mode == StationMode.GRINDSTONE || mode == StationMode.CARTOGRAPHY) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 119, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 141, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 67, this.topPos + 131, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 104, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
            } else if (mode == StationMode.ANVIL || mode == StationMode.FD_CUTTING || mode == StationMode.TETRA_WORKBENCH) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 64, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 91, this.topPos + 131, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 122, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
            } else if (mode == StationMode.LOOM) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 24, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 48, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 72, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 96, this.topPos + 131, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 124, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
            } else if (mode == StationMode.FD_COOKING) {
                for (int c = 0; c < 3; c++) {
                    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 24 + c * 20, this.topPos + 117, 190.0f, 0.0f, 18, 18, 256, 256);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 24 + c * 20, this.topPos + 137, 190.0f, 0.0f, 18, 18, 256, 256);
                }
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 93, this.topPos + 147, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 92, this.topPos + 126, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 123, this.topPos + 125, 190.0f, 0.0f, 18, 18, 256, 256);
            } else if (mode == StationMode.APOTH_REFORGING || mode == StationMode.APOTH_GEM_CUTTING) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 59, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 84, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 104, this.topPos + 131, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 127, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
            } else if (mode == StationMode.APOTH_SALVAGING) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 44, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 75, this.topPos + 131, 40.0f, 70.0f, 24, 17, 256, 256);
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS_TEXTURE, this.leftPos + 114, this.topPos + 130, 190.0f, 0.0f, 18, 18, 256, 256);
            }
        } else if (mode == StationMode.MODDED) {
            int moddedSlot = this.menu.getSelectedModdedStationSlot();
            if (moddedSlot >= 0 && moddedSlot < this.menu.getStationContainer().getContainerSize()) {
                ItemStack moddedStack = this.menu.getStationContainer().getItem(moddedSlot);
                if (!moddedStack.isEmpty()) {
                    graphics.item(moddedStack, this.leftPos + 8, this.topPos + 92);
                    graphics.text(this.font, moddedStack.getHoverName(), this.leftPos + 28, this.topPos + 96, 0x404040, false);
                }
            }
        }

        // 2. Right Station Selector Panel
        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, panelX, panelY, 0.0f, 0.0f, 32, 160, 256, 256);

        List<StationEntry> stations = getInstalledStations();
        int maxStationScroll = Math.max(0, stations.size() - 6);
        for (int i = 0; i < 6; i++) {
            int idx = i + this.stationScrollOffset;
            if (idx >= stations.size()) break;
            StationEntry entry = stations.get(idx);
            int slotY = panelY + 4 + i * 25;
            graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, panelX + 4, slotY, 40.0f, 20.0f, 22, 22, 256, 256);

            graphics.item(entry.icon(), panelX + 7, slotY + 3);

            boolean isActive = (entry.mode() == StationMode.MODDED)
                    ? (mode == StationMode.MODDED && this.menu.getSelectedModdedStationSlot() == entry.stationSlot())
                    : (entry.mode() == mode);
            if (isActive) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, panelX + 4, slotY, 40.0f, 50.0f, 22, 22, 256, 256);
            } else if (mouseX >= panelX + 4 && mouseX <= panelX + 26 && mouseY >= slotY && mouseY <= slotY + 22) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, panelX + 4, slotY, 40.0f, 80.0f, 22, 22, 256, 256);
            }
        }

        if (maxStationScroll > 0) {
            int thumbY = panelY + 4 + (this.stationScrollOffset * (152 - 15) / maxStationScroll);
            graphics.blit(RenderPipelines.GUI_TEXTURED, PANEL_TEXTURE, panelX + 23, thumbY, 40.0f, 0.0f, 6, 15, 256, 256);
        }

        // 3. Left Storage Overlay
        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        graphics.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, overlayX, overlayY, 0.0f, 0.0f, 74, 180, 256, 256);

        int maxRow = Math.max(0, (this.menu.getDisplaySlots() / 3) - 7);
        for (int r = 0; r < 7; r++) {
            int rowIdx = r + this.overlayScrollRow;
            for (int c = 0; c < 3; c++) {
                int slotIdx = rowIdx * 3 + c;
                if (slotIdx >= this.menu.getDisplaySlots()) break;
                ItemStack stack = this.menu.getDisplay().getItem(slotIdx);
                int sx = overlayX + 5 + c * 18;
                int sy = overlayY + 19 + r * 19;
                if (!stack.isEmpty()) {
                    graphics.item(stack, sx, sy);
                    graphics.itemDecorations(this.font, stack, sx, sy);
                }
                if (mouseX >= sx && mouseX < sx + 16 && mouseY >= sy && mouseY < sy + 16) {
                    graphics.fill(sx, sy, sx + 16, sy + 16, 0x50FFFFFF);
                }
            }
        }

        if (maxRow > 0) {
            int thumbY = overlayY + 18 + (this.overlayScrollRow * (152 - 15) / maxRow);
            graphics.blit(RenderPipelines.GUI_TEXTURED, OVERLAY_TEXTURE, overlayX + 62, thumbY, 80.0f, 0.0f, 6, 15, 256, 256);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        // Tooltips for stations on right panel
        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        if (mouseX >= panelX && mouseX <= panelX + 32 && mouseY >= panelY + 4 && mouseY < panelY + 4 + 6 * 25) {
            List<StationEntry> stations = getInstalledStations();
            int clickedSlot = (int) ((mouseY - (panelY + 4)) / 25);
            int idx = clickedSlot + this.stationScrollOffset;
            if (idx >= 0 && idx < stations.size()) {
                StationEntry entry = stations.get(idx);
                if (entry.mode() == StationMode.MODDED) {
                    List<Component> tooltip = List.of(
                            entry.name(),
                            Component.literal("§7Click to select station"),
                            Component.literal("§eShift-Click to open native GUI")
                    );
                    graphics.setTooltipForNextFrame(this.font, tooltip, java.util.Optional.empty(), mouseX, mouseY);
                } else {
                    graphics.setTooltipForNextFrame(entry.name(), mouseX, mouseY);
                }
            }
        }

        // Active modded station badge tooltip
        if (this.menu.getActiveMode() == StationMode.MODDED) {
            int moddedSlot = this.menu.getSelectedModdedStationSlot();
            if (moddedSlot >= 0 && moddedSlot < this.menu.getStationContainer().getContainerSize()) {
                ItemStack moddedStack = this.menu.getStationContainer().getItem(moddedSlot);
                if (!moddedStack.isEmpty()) {
                    if (mouseX >= this.leftPos + 8 && mouseX <= this.leftPos + 24 && mouseY >= this.topPos + 92 && mouseY <= this.topPos + 108) {
                        graphics.setTooltipForNextFrame(this.font, moddedStack, mouseX, mouseY);
                    }
                }
            }
        }

        // Tooltips for left overlay items
        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        if (mouseX >= overlayX + 4 && mouseX < overlayX + 58 && mouseY >= overlayY + 18 && mouseY < overlayY + 18 + 7 * 19) {
            int col = (int) ((mouseX - (overlayX + 4)) / 18);
            int row = (int) ((mouseY - (overlayY + 18)) / 19) + this.overlayScrollRow;
            int slot = row * 3 + col;
            if (slot >= 0 && slot < this.menu.getDisplaySlots()) {
                ItemStack stack = this.menu.getDisplay().getItem(slot);
                if (!stack.isEmpty()) {
                    graphics.setTooltipForNextFrame(this.font, stack, mouseX, mouseY);
                }
            }
        }

        // Furnace status tooltips
        if (this.menu.getActiveMode() == StationMode.FURNACE) {
            if (mouseX >= this.leftPos + 57 && mouseX <= this.leftPos + 71 && mouseY >= this.topPos + 137 && mouseY <= this.topPos + 151) {
                int lit = this.menu.getFurnaceLitTime();
                graphics.setTooltipForNextFrame(Component.literal("Furnace Fuel: " + (lit > 0 ? (lit + " ticks remaining") : "Empty")), mouseX, mouseY);
            } else if (mouseX >= this.leftPos + 80 && mouseX <= this.leftPos + 104 && mouseY >= this.topPos + 134 && mouseY <= this.topPos + 151) {
                int total = Math.max(1, this.menu.getFurnaceCookingTotalTime());
                int pct = this.menu.getFurnaceCookingProgress() * 100 / total;
                graphics.setTooltipForNextFrame(Component.literal("Smelting Progress: " + pct + "%"), mouseX, mouseY);
            }
        }
    }
    *///?} < 26.1 {
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (handleCustomClick(mouseX, mouseY, button, hasShiftDown())) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        drawCustomComponents(graphics, mouseX, mouseY);
    }

    private void drawCustomComponents(GuiGraphics graphics, int mouseX, int mouseY) {
        StationMode mode = this.menu.getActiveMode();

        // 1. Center Station Interface Overlay
        if (mode != StationMode.CRAFTING && mode != StationMode.MODDED) {
            graphics.blit(WIDGETS_TEXTURE, this.leftPos + 7, this.topPos + 105, 0, 0, 150, 64);

            if (mode == StationMode.FURNACE) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 55, this.topPos + 116, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 55, this.topPos + 152, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 115, this.topPos + 134, 160, 0, 26, 26);

                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 57, this.topPos + 137, 0, 70, 14, 14);
                if (this.menu.getFurnaceLitTime() > 0) {
                    int maxLit = Math.max(1, this.menu.getFurnaceLitDuration());
                    int litH = Math.min(14, this.menu.getFurnaceLitTime() * 14 / maxLit);
                    graphics.blit(WIDGETS_TEXTURE, this.leftPos + 57, this.topPos + 137 + (14 - litH), 20, 70 + (14 - litH), 14, litH);
                }

                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 80, this.topPos + 134, 40, 70, 24, 17);
                if (this.menu.getFurnaceCookingProgress() > 0) {
                    int maxCook = Math.max(1, this.menu.getFurnaceCookingTotalTime());
                    int progressW = Math.min(24, this.menu.getFurnaceCookingProgress() * 24 / maxCook);
                    graphics.blit(WIDGETS_TEXTURE, this.leftPos + 80, this.topPos + 134, 70, 70, progressW, 17);
                }
            } else if (mode == StationMode.STONECUTTER) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 19, this.topPos + 132, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 142, this.topPos + 132, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 116, this.topPos + 134, 40, 70, 24, 17);

                ItemStack in = this.menu.getCraftSlots().getItem(0);
                List<ItemStack> variants = StationRecipeManager.getStonecutterVariants(in, this.menu.getLevel());
                int selected = this.menu.getSelectedRecipeIndex();
                for (int v = 0; v < Math.min(variants.size(), 8); v++) {
                    int c = v % 4;
                    int r = v / 4;
                    int bx = this.leftPos + 44 + c * 18;
                    int by = this.topPos + 124 + r * 19;
                    int u = (v == selected) ? 140 : 100;
                    graphics.blit(WIDGETS_TEXTURE, bx, by, u, 70, 16, 18);
                    graphics.renderItem(variants.get(v), bx, by + 1);
                }
            } else if (mode == StationMode.SMITHING) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 17, this.topPos + 129, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 41, this.topPos + 129, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 65, this.topPos + 129, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 123, this.topPos + 129, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 92, this.topPos + 130, 40, 70, 24, 17);
            } else if (mode == StationMode.GRINDSTONE || mode == StationMode.CARTOGRAPHY) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 119, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 141, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 67, this.topPos + 131, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 104, this.topPos + 130, 190, 0, 18, 18);
            } else if (mode == StationMode.ANVIL || mode == StationMode.FD_CUTTING || mode == StationMode.TETRA_WORKBENCH) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 64, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 91, this.topPos + 131, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 122, this.topPos + 130, 190, 0, 18, 18);
            } else if (mode == StationMode.LOOM) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 24, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 48, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 72, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 96, this.topPos + 131, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 124, this.topPos + 130, 190, 0, 18, 18);
            } else if (mode == StationMode.FD_COOKING) {
                for (int c = 0; c < 3; c++) {
                    graphics.blit(WIDGETS_TEXTURE, this.leftPos + 24 + c * 20, this.topPos + 117, 190, 0, 18, 18);
                    graphics.blit(WIDGETS_TEXTURE, this.leftPos + 24 + c * 20, this.topPos + 137, 190, 0, 18, 18);
                }
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 93, this.topPos + 147, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 92, this.topPos + 126, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 123, this.topPos + 125, 190, 0, 18, 18);
            } else if (mode == StationMode.APOTH_REFORGING || mode == StationMode.APOTH_GEM_CUTTING) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 34, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 59, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 84, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 104, this.topPos + 131, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 127, this.topPos + 130, 190, 0, 18, 18);
            } else if (mode == StationMode.APOTH_SALVAGING) {
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 44, this.topPos + 130, 190, 0, 18, 18);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 75, this.topPos + 131, 40, 70, 24, 17);
                graphics.blit(WIDGETS_TEXTURE, this.leftPos + 114, this.topPos + 130, 190, 0, 18, 18);
            }
        } else if (mode == StationMode.MODDED) {
            int moddedSlot = this.menu.getSelectedModdedStationSlot();
            if (moddedSlot >= 0 && moddedSlot < this.menu.getStationContainer().getContainerSize()) {
                ItemStack moddedStack = this.menu.getStationContainer().getItem(moddedSlot);
                if (!moddedStack.isEmpty()) {
                    graphics.renderItem(moddedStack, this.leftPos + 8, this.topPos + 92);
                    graphics.drawString(this.font, moddedStack.getHoverName(), this.leftPos + 28, this.topPos + 96, 0x404040, false);
                }
            }
        }

        // 2. Right Station Selector Panel
        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        graphics.blit(PANEL_TEXTURE, panelX, panelY, 0, 0, 32, 160);

        List<StationEntry> stations = getInstalledStations();
        int maxStationScroll = Math.max(0, stations.size() - 6);
        for (int i = 0; i < 6; i++) {
            int idx = i + this.stationScrollOffset;
            if (idx >= stations.size()) break;
            StationEntry entry = stations.get(idx);
            int slotY = panelY + 4 + i * 25;
            graphics.blit(PANEL_TEXTURE, panelX + 4, slotY, 40, 20, 22, 22);

            graphics.renderItem(entry.icon(), panelX + 7, slotY + 3);

            boolean isActive = (entry.mode() == StationMode.MODDED)
                    ? (mode == StationMode.MODDED && this.menu.getSelectedModdedStationSlot() == entry.stationSlot())
                    : (entry.mode() == mode);
            if (isActive) {
                graphics.blit(PANEL_TEXTURE, panelX + 4, slotY, 40, 50, 22, 22);
            } else if (mouseX >= panelX + 4 && mouseX <= panelX + 26 && mouseY >= slotY && mouseY <= slotY + 22) {
                graphics.blit(PANEL_TEXTURE, panelX + 4, slotY, 40, 80, 22, 22);
            }
        }

        if (maxStationScroll > 0) {
            int thumbY = panelY + 4 + (this.stationScrollOffset * (152 - 15) / maxStationScroll);
            graphics.blit(PANEL_TEXTURE, panelX + 23, thumbY, 40, 0, 6, 15);
        }

        // 3. Left Storage Overlay
        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        graphics.blit(OVERLAY_TEXTURE, overlayX, overlayY, 0, 0, 74, 180);

        int maxRow = Math.max(0, (this.menu.getDisplaySlots() / 3) - 7);
        for (int r = 0; r < 7; r++) {
            int rowIdx = r + this.overlayScrollRow;
            for (int c = 0; c < 3; c++) {
                int slotIdx = rowIdx * 3 + c;
                if (slotIdx >= this.menu.getDisplaySlots()) break;
                ItemStack stack = this.menu.getDisplay().getItem(slotIdx);
                int sx = overlayX + 5 + c * 18;
                int sy = overlayY + 19 + r * 19;
                if (!stack.isEmpty()) {
                    graphics.renderItem(stack, sx, sy);
                    graphics.renderItemDecorations(this.font, stack, sx, sy);
                }
                if (mouseX >= sx && mouseX < sx + 16 && mouseY >= sy && mouseY < sy + 16) {
                    graphics.fill(sx, sy, sx + 16, sy + 16, 0x50FFFFFF);
                }
            }
        }

        if (maxRow > 0) {
            int thumbY = overlayY + 18 + (this.overlayScrollRow * (152 - 15) / maxRow);
            graphics.blit(OVERLAY_TEXTURE, overlayX + 62, thumbY, 80, 0, 6, 15);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.renderTooltip(graphics, mouseX, mouseY);

        // Tooltips for stations on right panel
        int panelX = this.leftPos + this.imageWidth + 4;
        int panelY = this.topPos + 10;
        if (mouseX >= panelX && mouseX <= panelX + 32 && mouseY >= panelY + 4 && mouseY < panelY + 4 + 6 * 25) {
            List<StationEntry> stations = getInstalledStations();
            int clickedSlot = (int) ((mouseY - (panelY + 4)) / 25);
            int idx = clickedSlot + this.stationScrollOffset;
            if (idx >= 0 && idx < stations.size()) {
                StationEntry entry = stations.get(idx);
                if (entry.mode() == StationMode.MODDED) {
                    List<Component> tooltip = List.of(
                            entry.name(),
                            Component.literal("§7Click to select station"),
                            Component.literal("§eShift-Click to open native GUI")
                    );
                    graphics.renderComponentTooltip(this.font, tooltip, mouseX, mouseY);
                } else {
                    graphics.renderTooltip(this.font, entry.name(), mouseX, mouseY);
                }
            }
        }

        // Active modded station badge tooltip
        if (this.menu.getActiveMode() == StationMode.MODDED) {
            int moddedSlot = this.menu.getSelectedModdedStationSlot();
            if (moddedSlot >= 0 && moddedSlot < this.menu.getStationContainer().getContainerSize()) {
                ItemStack moddedStack = this.menu.getStationContainer().getItem(moddedSlot);
                if (!moddedStack.isEmpty()) {
                    if (mouseX >= this.leftPos + 8 && mouseX <= this.leftPos + 24 && mouseY >= this.topPos + 92 && mouseY <= this.topPos + 108) {
                        graphics.renderTooltip(this.font, moddedStack, mouseX, mouseY);
                    }
                }
            }
        }

        // Tooltips for left overlay items
        int overlayX = this.leftPos - 74;
        int overlayY = this.topPos + 10;
        if (mouseX >= overlayX + 4 && mouseX < overlayX + 58 && mouseY >= overlayY + 18 && mouseY < overlayY + 18 + 7 * 19) {
            int col = (int) ((mouseX - (overlayX + 4)) / 18);
            int row = (int) ((mouseY - (overlayY + 18)) / 19) + this.overlayScrollRow;
            int slot = row * 3 + col;
            if (slot >= 0 && slot < this.menu.getDisplaySlots()) {
                ItemStack stack = this.menu.getDisplay().getItem(slot);
                if (!stack.isEmpty()) {
                    graphics.renderTooltip(this.font, stack, mouseX, mouseY);
                }
            }
        }

        // Furnace status tooltips
        if (this.menu.getActiveMode() == StationMode.FURNACE) {
            if (mouseX >= this.leftPos + 57 && mouseX <= this.leftPos + 71 && mouseY >= this.topPos + 137 && mouseY <= this.topPos + 151) {
                int lit = this.menu.getFurnaceLitTime();
                graphics.renderTooltip(this.font, Component.literal("Furnace Fuel: " + (lit > 0 ? (lit + " ticks remaining") : "Empty")), mouseX, mouseY);
            } else if (mouseX >= this.leftPos + 80 && mouseX <= this.leftPos + 104 && mouseY >= this.topPos + 134 && mouseY <= this.topPos + 151) {
                int total = Math.max(1, this.menu.getFurnaceCookingTotalTime());
                int pct = this.menu.getFurnaceCookingProgress() * 100 / total;
                graphics.renderTooltip(this.font, Component.literal("Smelting Progress: " + pct + "%"), mouseX, mouseY);
            }
        }
    }
    //?}
}

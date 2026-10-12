package com.brilliafy.magicstorage.client.screen;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.menu.StorageAccessMenu;
//? >= 26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
*///?} < 26.1 {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Locale;

public class StorageAccessScreen extends AbstractContainerScreen<StorageAccessMenu> {
    private static final ResourceLocation TEXTURE = Constants.id("textures/gui/request_full.png");
    private EditBox searchBox;

    //? >= 26.1 {
    /*public StorageAccessScreen(StorageAccessMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 210, 222);
    }
    *///?} < 26.1 {
    public StorageAccessScreen(StorageAccessMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 210;
        this.imageHeight = 222;
    }
    //?}

    @Override
    protected void init() {
        super.init();
        this.searchBox = new EditBox(this.font, this.leftPos + 80, this.topPos + 6, 90, 12,
                Component.translatable("gui.magicstorage.search"));
        this.searchBox.setMaxLength(32);
        this.searchBox.setResponder(this::sendSearch);
        this.addRenderableWidget(this.searchBox);
    }

    /** Streams the search query to the server using vanilla button-click packets. */
    private void sendSearch(String text) {
        if (this.minecraft == null || this.minecraft.gameMode == null) {
            return;
        }
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, StorageAccessMenu.SEARCH_CLEAR);
        String lower = text.toLowerCase(Locale.ROOT);
        for (int i = 0; i < lower.length(); i++) {
            int index = StorageAccessMenu.SEARCH_ALPHABET.indexOf(lower.charAt(i));
            if (index >= 0) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, index);
            }
        }
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, StorageAccessMenu.SEARCH_COMMIT);
    }

    //? > 1.20.1 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0 && this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
                    scrollY > 0 ? StorageAccessMenu.PAGE_UP : StorageAccessMenu.PAGE_DOWN);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
    //?} <= 1.20.1 {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta != 0 && this.minecraft != null && this.minecraft.gameMode != null) {
            this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
                    delta > 0 ? StorageAccessMenu.PAGE_UP : StorageAccessMenu.PAGE_DOWN);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    *///?}

    //? >= 26.1 {
    /*@Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0f, 0.0f,
                this.imageWidth, this.imageHeight, 256, 256);
    }
    *///?} < 26.1 {
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
    //?}
}

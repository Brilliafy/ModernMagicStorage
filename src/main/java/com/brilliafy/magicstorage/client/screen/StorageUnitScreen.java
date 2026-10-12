package com.brilliafy.magicstorage.client.screen;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.menu.StorageUnitMenu;
//? >= 26.1 {
/*import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
*///?} < 26.1 {
import net.minecraft.client.gui.GuiGraphics;
//?}
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StorageUnitScreen extends AbstractContainerScreen<StorageUnitMenu> {
    private static final ResourceLocation TEXTURE = Constants.id("textures/gui/storage_unit.png");

    //? >= 26.1 {
    /*public StorageUnitScreen(StorageUnitMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }
    *///?} < 26.1 {
    public StorageUnitScreen(StorageUnitMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }
    //?}

    //? >= 26.1 {
    /*@Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractContents(graphics, mouseX, mouseY, partialTick);

        if (menu.getUnit() != null) {
            String info = menu.getUnit().getStoredCount() + " / " + menu.getUnit().getCapacity() + " Items";
            graphics.text(this.font, info, this.leftPos + 40, this.topPos + 35, 0x404040, false);
        }
    }
    *///?} < 26.1 {
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        if (menu.getUnit() != null) {
            String info = menu.getUnit().getStoredCount() + " / " + menu.getUnit().getCapacity() + " Items";
            graphics.drawString(this.font, info, this.leftPos + 40, this.topPos + 35, 0x404040, false);
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }
    //?}
}

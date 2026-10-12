package com.brilliafy.magicstorage.client.screen;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.menu.StorageUnitMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StorageUnitScreen extends AbstractContainerScreen<StorageUnitMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/storage_unit.png");

    public StorageUnitScreen(StorageUnitMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

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
}

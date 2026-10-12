package com.brilliafy.magicstorage.client.screen;

import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.menu.StorageAccessMenu;
import com.brilliafy.magicstorage.storage.StorageNetworkManager;
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
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class StorageAccessScreen extends AbstractContainerScreen<StorageAccessMenu> {
    private static final ResourceLocation TEXTURE = Constants.id("textures/gui/request_full.png");
    private EditBox searchBox;
    private int scrollOffset = 0;

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
        this.searchBox = new EditBox(this.font, this.leftPos + 80, this.topPos + 6, 90, 12, Component.translatable("gui.magicstorage.search"));
        this.searchBox.setResponder(text -> {
            this.menu.setSearchQuery(text);
            this.scrollOffset = 0;
        });
        this.addRenderableWidget(this.searchBox);
    }

    //? > 1.20.1 {
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            int maxOffset = Math.max(0, (menu.getNetworkItems().size() / 9) - 4);
            if (scrollY > 0) {
                scrollOffset = Math.max(0, scrollOffset - 1);
            } else {
                scrollOffset = Math.min(maxOffset, scrollOffset + 1);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
    //?} <= 1.20.1 {
    /*@Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (delta != 0) {
            int maxOffset = Math.max(0, (menu.getNetworkItems().size() / 9) - 4);
            if (delta > 0) {
                scrollOffset = Math.max(0, scrollOffset - 1);
            } else {
                scrollOffset = Math.min(maxOffset, scrollOffset + 1);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }
    *///?}

    //? >= 26.1 {
    /*@Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0f, 0.0f, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractContents(graphics, mouseX, mouseY, partialTick);

        // Render network items grid
        List<ItemStack> items = menu.getNetworkItems();
        int startIndex = scrollOffset * 9;
        int maxSlots = 36; // 4 rows of 9

        for (int i = 0; i < maxSlots; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex < items.size()) {
                ItemStack stack = items.get(itemIndex);
                int col = i % 9;
                int row = i / 9;
                int x = this.leftPos + 8 + col * 18;
                int y = this.topPos + 20 + row * 18;

                graphics.item(stack, x, y);
                graphics.itemDecorations(this.font, stack, x, y, formatCount(stack.getCount()));
            }
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

        // Render network items grid
        List<ItemStack> items = menu.getNetworkItems();
        int startIndex = scrollOffset * 9;
        int maxSlots = 36; // 4 rows of 9

        for (int i = 0; i < maxSlots; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex < items.size()) {
                ItemStack stack = items.get(itemIndex);
                int col = i % 9;
                int row = i / 9;
                int x = this.leftPos + 8 + col * 18;
                int y = this.topPos + 20 + row * 18;

                graphics.renderItem(stack, x, y);
                graphics.renderItemDecorations(this.font, stack, x, y, formatCount(stack.getCount()));
            }
        }

        this.renderTooltip(graphics, mouseX, mouseY);
    }
    //?}

    //? >= 26.1 {
    /*@Override
    public boolean mouseClicked(net.minecraft.client.input.MouseButtonEvent event, boolean bl) {
        double mouseX = event.x();
        double mouseY = event.y();
        int button = event.button();
        List<ItemStack> items = menu.getNetworkItems();
        int startIndex = scrollOffset * 9;

        for (int i = 0; i < 36; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex < items.size()) {
                int col = i % 9;
                int row = i / 9;
                int x = this.leftPos + 8 + col * 18;
                int y = this.topPos + 20 + row * 18;

                if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18) {
                    boolean shift = event.hasShiftDown();
                    boolean rightClick = button == 1;
                    menu.handleNetworkItemClick(items.get(itemIndex), shift, rightClick);
                    return true;
                }
            }
        }

        return super.mouseClicked(event, bl);
    }
    *///?} < 26.1 {
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check clicking network items
        List<ItemStack> items = menu.getNetworkItems();
        int startIndex = scrollOffset * 9;

        for (int i = 0; i < 36; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex < items.size()) {
                int col = i % 9;
                int row = i / 9;
                int x = this.leftPos + 8 + col * 18;
                int y = this.topPos + 20 + row * 18;

                if (mouseX >= x && mouseX < x + 18 && mouseY >= y && mouseY < y + 18) {
                    boolean shift = hasShiftDown();
                    boolean rightClick = button == 1;
                    menu.handleNetworkItemClick(items.get(itemIndex), shift, rightClick);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
    //?}

    public static String formatCount(int count) {
        if (count >= 1_000_000) {
            return String.format("%.1fM", count / 1_000_000.0);
        } else if (count >= 10_000) {
            return String.format("%.1fK", count / 1_000.0);
        }
        return String.valueOf(count);
    }
}

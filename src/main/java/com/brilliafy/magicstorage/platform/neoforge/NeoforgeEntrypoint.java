package com.brilliafy.magicstorage.platform.neoforge;

//? neoforge {
/*import com.brilliafy.magicstorage.Constants;
import com.brilliafy.magicstorage.ModernMagicStorage;
import com.brilliafy.magicstorage.client.ModScreens;
import com.brilliafy.magicstorage.init.ModBlockEntities;
import com.brilliafy.magicstorage.init.ModBlocks;
import com.brilliafy.magicstorage.init.ModCreativeTabs;
import com.brilliafy.magicstorage.init.ModItems;
import com.brilliafy.magicstorage.init.ModMenus;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class NeoforgeEntrypoint {

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, Constants.MOD_ID);
    private static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Constants.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Constants.MOD_ID);
    private static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, Constants.MOD_ID);
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);

    public NeoforgeEntrypoint(IEventBus modEventBus) {
        ModernMagicStorage.onInitialize();

        ModBlocks.BLOCKS.forEach((id, block) -> BLOCKS.register(id.getPath(), () -> block));
        ModItems.ITEMS.forEach((id, item) -> ITEMS.register(id.getPath(), () -> item));
        ModBlockEntities.BLOCK_ENTITY_TYPES.forEach((id, type) -> BLOCK_ENTITIES.register(id.getPath(), () -> type));
        ModMenus.MENU_TYPES.forEach((id, type) -> MENUS.register(id.getPath(), () -> type));
        CREATIVE_TABS.register(ModCreativeTabs.TAB_ID.getPath(), () -> ModCreativeTabs.TAB);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        MENUS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);

        modEventBus.addListener(RegisterMenuScreensEvent.class, ModScreens::registerScreens);
    }
}
*///?}

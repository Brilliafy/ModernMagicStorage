# Crafting Stations and Multi-Station GUI

This guide covers the interactive Crafting Access terminal, its layout components, supported crafting stations, and recipe resolution mechanics.

---

## Table of Contents

- [The Multi-Station Interface](#the-multi-station-interface)
  - [Right Station Selection Panel](#right-station-selection-panel)
  - [Left Storage Network Overlay](#left-storage-network-overlay)
  - [Center Station Workspace](#center-station-workspace)
- [How to Install Stations in the Storage Heart](#how-to-install-stations-in-the-storage-heart)
- [Detailed Station Reference](#detailed-station-reference)
  - [Crafting Table (Mode 0)](#crafting-table-mode-0)
  - [Virtual Furnace (Mode 1)](#virtual-furnace-mode-1)
  - [Stonecutter (Mode 2)](#stonecutter-mode-2)
  - [Smithing Table (Mode 3)](#smithing-table-mode-3)
  - [Grindstone (Mode 4)](#grindstone-mode-4)
  - [Loom (Mode 5)](#loom-mode-5)
  - [Cartography Table (Mode 6)](#cartography-table-mode-6)
  - [Anvil (Mode 7)](#anvil-mode-7)
  - [Farmer's Delight Cooking Pot (Mode 8)](#farmers-delight-cooking-pot-mode-8)
  - [Farmer's Delight Cutting Board (Mode 9)](#farmers-delight-cutting-board-mode-9)
  - [Apotheosis Reforging Table (Mode 10)](#apotheosis-reforging-table-mode-10)
  - [Apotheosis Salvaging Table (Mode 11)](#apotheosis-salvaging-table-mode-11)
  - [Apotheosis Gem Cutting Table (Mode 12)](#apotheosis-gem-cutting-table-mode-12)
  - [Tetra Modular Workbench (Mode 13)](#tetra-modular-workbench-mode-13)
  - [Modded Station Blocks (Mode 14)](#modded-station-blocks-mode-14)
- [Instant Shift-Crafting](#instant-shift-crafting)
- [Safe Item Ejection and Cursor Protection](#safe-item-ejection-and-cursor-protection)
- [Recipe Conflict Prevention](#recipe-conflict-prevention)

---

## The Multi-Station Interface

The Crafting Access terminal (`CraftingAccessScreen`) provides a three-part workspace designed for rapid crafting without opening multiple separate menus.

### Right Station Selection Panel
- **Position**: Attached to the right side of the main terminal window (`x = leftPos + 210`, `y = topPos + 10`).
- **Icons**: Displays 1:1 pixel icons of every station currently installed in the Storage Heart.
- **Scrollbar**: When more than 6 stations are installed, a procedural vertical scrollbar activates. Scroll with the mouse wheel while hovering over the panel.
- **Visual Feedback**:
  - The currently active station is highlighted with a gold border.
  - Hovering over unselected stations displays a silver highlight.
- **Interaction**:
  - **Left-Click**: Switches the center workspace to that station mode.
  - **Shift-Click (Modded Stations)**: Opens the native GUI of the modded station block directly.

### Left Storage Network Overlay
- **Position**: Attached to the left side of the main terminal window (`x = leftPos - 74`, `y = topPos + 10`).
- **Grid Layout**: 3 columns by 7 rows displaying 21 network items at a glance.
- **Live Sync**: Mirrors items directly from the connected Storage Heart in real time.
- **Controls**:
  - **Mouse Wheel**: Scrolls vertically through the entire network inventory.
  - **Left-Click Item**: Withdraws a full stack into your hand or cursor.
  - **Right-Click Item**: Withdraws half of the stack.
  - **Shift-Click Item**: Transfers the stack directly into your player inventory.
  - **Click with Carried Item**: Deposits your cursor stack into the Magic Storage network.

### Center Station Workspace
- Dynamically adapts its slot layout, background widget art, flame animation, progress indicators, and recipe buttons based on the station selected on the right panel.

---

## How to Install Stations in the Storage Heart

1. Craft or collect any supported station block (for example, Crafting Table, Furnace, Stonecutter, Cooking Pot, etc.).
2. Walk up to your placed **Storage Heart**.
3. Hold the station block in your main hand and **Shift-Right-Click** the Storage Heart.
4. The station is consumed and installed into the Heart. A confirmation message appears in your action bar.
5. The station icon immediately appears on the right panel of all connected Crafting Access terminals.
6. To remove a station, **Shift-Right-Click** the Storage Heart with an empty hand. The last installed station will be returned to you.

The Storage Heart holds up to **20 stations** simultaneously.

---

## Detailed Station Reference

### Crafting Table (Mode 0)
- **Slots**: Standard 3x3 grid (9 input slots) plus 1 large result slot.
- **Features**: Resolves shaped and shapeless 3x3 recipes. Integrates with JEI recipe transfer.

### Virtual Furnace (Mode 1)
- **Slots**: Input Slot (`x=56, y=117`), Fuel Slot (`x=56, y=153`), Output Slot (`x=120, y=139`).
- **Continuous Background Smelting**: Smelts on the server 20 times per second even when the menu is closed.
- **Chunk Catch-Up**: If you travel far away and the chunk unloads, the Storage Heart records elapsed game ticks and automatically completes missed smelting ticks when the chunk reloads.
- **Furnace Variants**: Installing a Blast Furnace doubles ore smelting speed. Installing a Smoker doubles food cooking speed.
- **Fuel Remainders**: Safely returns containers (such as empty buckets from lava fuel) to the fuel slot or drops them if full.

### Stonecutter (Mode 2)
- **Slots**: Input Block Slot (`x=20, y=133`), Result Slot (`x=143, y=133`).
- **Clickable Variant Buttons**: When an input block is placed, up to 8 available stonecutting recipe variants appear as clickable icon buttons in the UI. Click any variant to select it.
- **Grid Variant Sampling**: Placing a target sample block in slot 1 locks the stonecutter to that output. The sample block is reusable and preserved.

### Smithing Table (Mode 3)
- **Slots**: Template Slot (`x=18, y=130`), Base Item Slot (`x=42, y=130`), Addition Material Slot (`x=66, y=130`), Result Slot (`x=124, y=130`).
- **Features**: Supports netherite upgrades, armor trims, and modded smithing recipes.

### Grindstone (Mode 4)
- **Slots**: Top Slot (`x=35, y=120`), Bottom Slot (`x=35, y=142`), Result Slot (`x=105, y=131`).
- **Repair Function**: Combines two damaged items of the same type. Durabilities are summed with an extra **5% max-durability bonus**. Preserves curse enchantments while removing normal enchantments.
- **Disenchant Function**: Placing a single enchanted item or book removes all non-curse enchantments, resets the anvil prior work penalty, and converts enchanted books back to ordinary books.

### Loom (Mode 5)
- **Slots**: Banner Slot (`x=25, y=131`), Dye Slot (`x=49, y=131`), Banner Pattern Slot (`x=73, y=131`), Result Slot (`x=125, y=131`).
- **Pattern Preservation**: The banner pattern item is reusable and remains in the slot after crafting.

### Cartography Table (Mode 6)
- **Slots**: Map Slot (`x=35, y=120`), Modifier Slot (`x=35, y=142`), Result Slot (`x=105, y=131`).
- **Operations**:
  - Map + Paper: Expands map zoom level.
  - Map + Glass Pane: Locks map preventing further exploration updates.
  - Map + Empty Map: Clones the map.

### Anvil (Mode 7)
- **Slots**: Target Slot (`x=35, y=131`), Sacrifice/Material Slot (`x=65, y=131`), Result Slot (`x=123, y=131`).
- **Operations**: Combines enchantments, applies enchanted books, and repairs equipment using raw materials.

### Farmer's Delight Cooking Pot (Mode 8)
- **Slots**: 6 Meal Input Slots (2 rows of 3: `y=118` and `y=138`), Serving Container Slot (`x=94, y=148`), Result Slot (`x=124, y=126`).
- **Container Validation**: Validates whether the recipe requires a specific serving container (Bowl, Glass Bottle). If required, the container must be present in slot 6.
- **Instant Preparation**: Supports instant shift-crafting for bulk meal preparation directly from storage.

### Farmer's Delight Cutting Board (Mode 9)
- **Slots**: Input Item Slot (`x=35, y=131`), Cutting Tool Slot (`x=65, y=131`), Result Slot (`x=123, y=131`).
- **Tool Durability Damage**: Deals 1 point of durability damage to the cutting tool (Knife, Axe, Shears) per craft.
- **Tool Retention**: The cutting tool remains in its slot after crafting.
- **Secondary Drops and Audio**: Automatically awards secondary and chance drops (such as Tree Bark from stripped logs or Straw from crops) and plays cutting sound effects.

### Apotheosis Reforging Table (Mode 10)
- **Slots**: Equipment Slot (`x=35, y=131`), Gem Dust Slot (`x=60, y=131`), Material Addition Slot (`x=85, y=131`), Result Slot (`x=128, y=131`).
- **Affix Re-Rolling**: Re-rolls equipment affix rarities and modifiers using gem dust.

### Apotheosis Salvaging Table (Mode 11)
- **Slots**: Gear Input Slot (`x=45, y=131`), Result Scrap Slot (`x=115, y=131`).
- **Scrap Recovery**: Breaks down unwanted affix gear into scrap materials.

### Apotheosis Gem Cutting Table (Mode 12)
- **Slots**: Gem Slot (`x=35, y=131`), Dust Slot (`x=60, y=131`), Upgrade Substrate Slot (`x=85, y=131`), Result Slot (`x=128, y=131`).
- **Gem Upgrading**: Enhances socketable affix gems to higher rarity tiers.

### Tetra Modular Workbench (Mode 13)
- **Slots**: Modular Tool Slot (`x=35, y=131`), Material Slot (`x=65, y=131`), Result Slot (`x=123, y=131`).
- **Modular Maintenance**: Detects modular tool components and honing materials. Repairs tools and advances hone progress while strictly preserving modular schemas and socketed upgrades.

### Modded Station Blocks (Mode 14)
- **Dynamic Compatibility**: Any third-party mod block that opens a container GUI or implements `MenuProvider` can be installed in the Storage Heart.
- **Interaction**:
  - **Left-Click on Panel**: Selects the modded station mode in the terminal.
  - **Shift-Click on Panel**: Opens the native GUI of the modded block directly.
- **Remote Distance Bypass**: Uses virtual player positioning so modded distance checks (`player.distanceToSqr <= 64.0`) succeed even when opening the station from thousands of blocks away via a wireless remote.

---

## Instant Shift-Crafting

Shift-clicking the crafting result slot executes bulk crafting in a single action:

1. Calculates how many items can fit into your player inventory.
2. Checks available ingredient quantities in both the crafting grid and the Magic Storage network.
3. Automatically withdraws necessary ingredients from the network.
4. Executes the craft, updates tool durability, grants secondary drops (such as Tree Bark), and delivers finished items directly into your inventory.
5. If inventory fills up, leftover crafted items are deposited directly back into the Magic Storage network.

---

## Safe Item Ejection and Cursor Protection

Items left in the terminal are never lost or deleted:

- **Switching Station Modes**: If you have items in the grid and switch stations on the right panel, transient items are automatically deposited into the Magic Storage network or returned to your inventory.
- **Closing the Terminal**: Closing the screen automatically empties the crafting grid into storage.
- **Cursor Item Protection**: Any item currently held on your mouse cursor when pressing Escape or closing the menu is automatically deposited into the network instead of dropping onto the ground.

---

## Recipe Conflict Prevention

To prevent station recipes from overriding standard crafting:

1. **Standard Recipes First**: When items are placed in the grid, the server evaluates normal 3x3 crafting recipes first.
2. **Grindstone Priority Check**: Vanilla's wildcard `RepairItemRecipe` is only allowed to defer to the Grindstone when a Grindstone is installed in the Storage Heart and two damaged items of the same type are present. Specific modded recipes that take two tools are crafted as intended without being intercepted.
3. **Dedicated Station Modes**: Station-specific recipes (such as cutting board slicing or cooking pot meals) only compute when the terminal is switched to that dedicated station mode or when no normal crafting recipe matches.

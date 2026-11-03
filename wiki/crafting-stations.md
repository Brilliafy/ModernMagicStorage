[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

# Crafting Stations and Mechanics

Inserting crafting station blocks into the 20-slot station inventory of the **Storage Heart** enables custom crafting modes within the **Crafting Access**.

---

## 1. Station Index and Requirements

| Station Name | Item / Block in Heart | Workspace Layout | Special Mechanics |
| :--- | :--- | :--- | :--- |
| **Crafting Table** | `minecraft:crafting_table` | Standard 3x3 Matrix | Shaped and shapeless crafting. Required to operate the terminal. |
| **Virtual Furnace** | `minecraft:furnace` | Input (top), Fuel (bottom), Output (right) | Continuous server-side smelting, catch-up logic, animated fire and arrow. |
| **Blast Furnace** | `minecraft:blast_furnace` | Input (top), Fuel (bottom), Output (right) | Doubles smelting speed for ores and raw metals. |
| **Smoker** | `minecraft:smoker` | Input (top), Fuel (bottom), Output (right) | Doubles cooking speed for food and meats. |
| **Stonecutter** | `minecraft:stonecutter` | Input (left), Output (right) | 8 clickable variant buttons directly in the UI. Supports grid sampling. |
| **Smithing Table** | `minecraft:smithing_table` | Template, Base, Addition, Output | Modern 3-slot smithing upgrades and armor trims. |
| **Grindstone** | `minecraft:grindstone` | 2 Input Slots, 1 Output Slot | Combines durability with 5% bonus; preserves curses; strips non-curses. |
| **Loom** | `minecraft:loom` | Banner, Dye, Pattern, Output | Banner pattern crafting. Pattern items are reusable and preserved. |
| **Cartography Table** | `minecraft:cartography_table` | Map, Modifier, Output | Map zooming (+Paper), locking (+Glass Pane), and cloning (+Empty Map). |
| **Anvil** | `minecraft:anvil` | Target, Sacrifice, Output | Equipment repair, enchantment combining, and renaming. |
| **Cooking Pot** | `farmersdelight:cooking_pot` | 6 Food Inputs, Container, Output | 1 to 6 shapeless meal ingredients with serving container check. |
| **Cutting Board** | `farmersdelight:cutting_board` | Input Item, Cutting Tool, Output | Deals 1 durability to tool, preserves tool in grid, drops secondary drops. |
| **Reforging Table** | `apotheosis:reforging_table` | Gear, Gem Dust, Material, Output | Re-rolls affix rarities on gear using gem dust. |
| **Salvaging Table** | `apotheosis:salvaging_table` | Gear Input, Output Scrap | Breaks down affix equipment into scrap materials. |
| **Gem Cutting Table** | `apotheosis:gem_cutting_table` | Gem, Dust, Substrate, Output | Upgrades socketable affix gems to higher rarity tiers. |
| **Modular Workbench** | `tetra:modular_workbench` | Modular Tool, Material, Output | Detects modular tool components and advances hone progress. |
| **Modded Station** | Any block with GUI / Menu | Dynamic Mode + Native GUI | Left-click selects station mode; shift-click opens native GUI remotely. |

---

## 2. The Interactive Multi-Station GUI

The Crafting Access terminal features three coordinated interface components:

### Right Station Selection Panel
* Located on the right edge of the terminal.
* Displays 1:1 pixel icons of every crafting station installed in the Storage Heart.
* Features a smooth vertical scrollbar when more than 6 stations are installed.
* **Left-Click**: Switches the center workspace to that station mode.
* **Shift-Click (Modded Stations)**: Opens the native GUI of the modded station directly, bypassing distance checks.

### Left Storage Network Overlay
* Located on the left edge of the terminal.
* Live 3-column scrollable inventory viewport mirroring items in the Storage Heart.
* Allows you to withdraw full stacks, half stacks, or quick-move items into inventory without leaving your crafting station workspace.
* Clicking the overlay with a carried item deposits it directly back into network storage.

### Center Station Workspace
* Adapts slot positions, background textures, progress arrows, animated flames, and recipe buttons based on the active station.

---

## 3. Detailed Station Mechanics

### Virtual Server-Side Furnace
* **Continuous Smelting**: Smelts items continuously on the server 20 times per second, even when players close the terminal or log off.
* **Catch-Up Formula**: If the chunk unloads while smelting is active, the Storage Heart records `LastTickGameTime`. Upon chunk reload or terminal access, it simulates elapsed ticks:
  $$\text{Missed Ticks} = \text{Current Game Time} - \text{Last Tick Game Time} - 1$$
  Smelting progress catches up automatically so your ores are ready when you return.
* **Fuel Handling**: Fuel burn times match vanilla standards. Remainder items (such as empty buckets from lava fuel) are returned to the fuel slot or dropped if full.

### Stonecutter Block Cutting
* When an input block is placed into slot 0, up to 8 available cutting variants appear as clickable icon buttons in the center workspace.
* Clicking any variant button instantly selects that recipe.
* Placing a target block into slot 1 serves as a sample template, locking the recipe to that variant. The sample block is preserved and not consumed.

### Grindstone Repair and Disenchanting
* **2-Tool Repair**: Placing two damaged tools of the same type combines their durabilities and adds an extra **5% max-durability bonus**. Curses are preserved while non-curse enchantments are stripped.
* **1-Item Disenchanting**: Placing a single enchanted item strips all non-curse enchantments, converts enchanted books back into normal books, and resets the prior work penalty.

### Farmer's Delight Culinary Integration
* **Cooking Pot**: Supports 1 to 6 food ingredients in the grid. If the meal requires a serving container (Bowl, Glass Bottle), it validates container presence in slot 6.
* **Cutting Board**: Input item plus cutting tool (Knife, Axe, Shears). Deals 1 durability point to the tool, keeps the tool in the grid, consumes the input item, outputs the primary cut, and grants secondary drops (such as Tree Bark from stripped logs or Straw from wheat) with authentic sound effects.

### Apotheosis and Tetra
* **Apotheosis Stations**: Supports affix gear re-rolling, scrap salvaging, and affix gem cutting.
* **Tetra Modular Workbench**: Detects modular tool components and honing materials, repairing tools and advancing hone progress while preserving modular schemas.

### Modded Station Blocks
* Any third-party mod block that provides a container GUI can be placed into the Storage Heart.
* Left-clicking its icon on the right panel selects the station mode.
* Shift-clicking opens its native GUI. The Storage Heart uses virtual player positioning (`player.blockPosition()`) so distance checks (`dist <= 64.0`) succeed even when accessing stations remotely.

---

## 4. Instant Shift-Crafting

Shift-clicking the crafting result slot executes bulk crafting in a single action:

1. Calculates total available inventory space.
2. Checks available ingredient quantities in both the grid and the Magic Storage network.
3. Automatically withdraws required ingredients from network stock.
4. Updates tool durability and grants secondary drops (such as Tree Bark).
5. Transfers crafted items into player inventory. If inventory fills up, leftover crafted items are deposited directly back into network storage.

---

## 5. Safe Item Ejection and Cursor Protection

* **Station Switching**: Switching station tabs on the right panel automatically returns transient grid items into the Magic Storage network or player inventory.
* **Menu Close**: Closing the screen empties the grid into network storage.
* **Cursor Stack**: Any item held on the mouse cursor when closing the terminal is automatically returned to network storage instead of dropping on the ground.

---

[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

[![Modrinth](https://img.shields.io/badge/Modrinth-3SFNKJXS-00AF5C?style=for-the-badge&logo=modrinth&logoColor=white)](https://modrinth.com/mod/magic-storage-network)
[![CurseForge](https://img.shields.io/badge/CurseForge-1629983-F44336?style=for-the-badge&logo=curseforge&logoColor=white)](https://www.curseforge.com/minecraft/mc-mods/magic-storage-network)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

# Modern Magic Storage

A modern, multi-loader, multi-version modular storage network mod for Minecraft inspired by Terraria's Magic Storage mod.

Modern Magic Storage replaces cluttered chest rooms with an expandable, centralized network powered by a **Storage Heart**. Store hundreds of thousands of items across 8 storage unit tiers, access your network from anywhere with wireless remotes, and craft with dozens of crafting stations in a single unified interface.

---

## Table of Contents

- [Supported Versions and Loaders](#supported-versions-and-loaders)
- [Quick Start Guide](#quick-start-guide)
- [Core Blocks](#core-blocks)
  - [Storage Heart](#storage-heart)
  - [Storage Units (8 Tiers)](#storage-units-8-tiers)
  - [Storage Access](#storage-access)
  - [Crafting Access](#crafting-access)
  - [Remote Access](#remote-access)
- [The Interactive Multi-Station GUI](#the-interactive-multi-station-gui)
  - [Right Station Selection Panel](#right-station-selection-panel)
  - [Left Storage Network Overlay](#left-storage-network-overlay)
  - [Virtual Server-Side Furnace](#virtual-server-side-furnace)
  - [Instant Shift-Crafting](#instant-shift-crafting)
  - [Safe Item Ejection](#safe-item-ejection)
- [Supported Crafting Stations](#supported-crafting-stations)
  - [Vanilla Stations](#vanilla-stations)
  - [Farmer's Delight Integration](#farmers-delight-integration)
  - [Apotheosis Integration](#apotheosis-integration)
  - [Tetra Integration](#tetra-integration)
  - [Modded Stations Support](#modded-stations-support)
- [Wireless Portable Remotes](#wireless-portable-remotes)
- [Materials, Ores, and Progression](#materials-ores-and-progression)
- [Search and Sorting Syntax](#search-and-sorting-syntax)
- [Third-Party Mod Integrations](#third-party-mod-integrations)
- [Local Testing and Building](#local-testing-and-building)
- [Wiki Documentation](#wiki-documentation)
- [Credits and License](#credits-and-license)

---

## Supported Versions and Loaders

Modern Magic Storage is built on Jared's MultiLoader-Template architecture and Stonecutter (`dev.kikugie.stonecutter`). Every release targets 7 distinct loader and Minecraft version platforms:

| Minecraft Version | Fabric | NeoForge | Forge | Output File |
| :---: | :---: | :---: | :---: | :--- |
| **26.3** | `✓` | `✓` | N/A | `magicstorage-2.0.0-fabric+26.3.jar` / `magicstorage-2.0.0-neoforge+26.3.jar` |
| **1.21.1** | `✓` | `✓` | N/A | `magicstorage-2.0.0-fabric+1.21.1.jar` / `magicstorage-2.0.0-neoforge+1.21.1.jar` |
| **1.20.1** | `✓` | `✓` | `✓` | `magicstorage-2.0.0-fabric+1.20.1.jar` / `magicstorage-2.0.0-forge+1.20.1.jar` / `magicstorage-2.0.0-neoforge+1.20.1.jar` |

---

## Quick Start Guide

1. **Craft a Storage Heart**: The central brain of your storage network.
2. **Craft at least one Storage Unit**: Basic Storage Units hold 36 item slots each.
3. **Place the Blocks Adjacent to Each Other**:
   - The Storage Heart must connect to Storage Units, Storage Access, or Crafting Access blocks directly or via a chain of adjacent network blocks.
   - Up to 1024 connected network blocks are automatically discovered using breadth-first search (BFS).
4. **Access Your Items**:
   - Right-click the Storage Heart or an adjacent **Storage Access** block to open the terminal.
   - Search, sort, deposit, and withdraw items.
5. **Craft Directly from Storage**:
   - Place a **Crafting Access** block adjacent to the network.
   - Shift-right-click the Storage Heart while holding crafting stations (Crafting Table, Furnace, Grindstone, etc.) to install them into the Heart.
   - Open the Crafting Access block to see your installed stations on the right panel and craft directly using items stored in the network.

---

## Core Blocks

### Storage Heart
- **Registry ID**: `magicstorage:storage_heart`
- **Hardness**: 3.0 | **Resistance**: 6.0 | **Light Level**: 15
- **Role**: The central multiblock controller. Coordinates all connected Storage Units, routes item insertion and extraction, maintains the virtual furnace, and holds up to 20 installed crafting stations.
- **Interactions**:
  - **Right-Click**: Opens the Storage Access network terminal directly from the Heart.
  - **Shift-Right-Click with a Station**: Installs the held station into the Heart (holds up to 20 stations).
  - **Shift-Right-Click with Empty Hand**: Removes the most recently installed station and returns it to your inventory.
- **Safety**: When broken, the Storage Heart safely drops all installed station items and furnace contents into the world and notifies connected access terminals.

### Storage Units (8 Tiers)
- **Role**: Modular item vaults that expand network capacity.
- **In-Place Upgrades**: You never need to break an existing unit to upgrade it. Shift-right-click any placed Storage Unit with a higher-tier upgrade item to instantly increase its capacity. All stored items are retained with zero risk of loss.
- **Individual Access**: Right-clicking a Storage Unit opens its direct inventory view.

| Tier | Block Registry ID | Upgrade Item ID | Slot Capacity | Notes |
| :--- | :--- | :--- | :---: | :--- |
| **Basic** | `magicstorage:storage_unit_basic` | N/A (Base Block) | 36 slots | Equivalent to a single chest plus hotbar. |
| **Crimtane** | `magicstorage:storage_unit_crimtane` | `magicstorage:upgrade_crimtane` | 54 slots | Equivalent to a double chest. |
| **Demonite** | `magicstorage:storage_unit_demonite` | `magicstorage:upgrade_demonite` | 54 slots | Alternate evil ore tier. |
| **Hellstone** | `magicstorage:storage_unit_hellstone` | `magicstorage:upgrade_hellstone` | 90 slots | Crafted with Hellstone Bars from the Nether. |
| **Hallowed** | `magicstorage:storage_unit_hallowed` | `magicstorage:upgrade_hallowed` | 135 slots | Mid-game high capacity storage. |
| **Blue Chlorophyte** | `magicstorage:storage_unit_blue_chlorophyte` | `magicstorage:upgrade_blue_chlorophyte` | 180 slots | Advanced biological storage. |
| **Luminite** | `magicstorage:storage_unit_luminite` | `magicstorage:upgrade_luminite` | 252 slots | End-game tier crafted with Luminite Bars. |
| **Terra** | `magicstorage:storage_unit_terra` | `magicstorage:upgrade_terra` | 360 slots | Ultimate storage vault holding 360 item stacks. |

### Storage Access
- **Registry ID**: `magicstorage:storage_access`
- **Role**: Dedicated network inventory terminal. Place anywhere adjacent to the network to search, filter, sort, deposit, and extract stored items.

### Crafting Access
- **Registry ID**: `magicstorage:crafting_access`
- **Role**: Unified crafting terminal connected directly to your storage network and all stations installed in the Storage Heart.

### Remote Access
- **Registry ID**: `magicstorage:remote_access`
- **Role**: Acts as a remote relay point for wireless access tools. Shift-right-clicking a Remote Access block with a Portable Access remote binds the remote to the connected Storage Heart.

---

## The Interactive Multi-Station GUI

The Crafting Access terminal features a custom, procedurally textured multi-station interface with seamless network integration.

### Right Station Selection Panel
- Located on the right side of the Crafting Access GUI.
- Displays 1:1 pixel icons of every crafting station installed in the Storage Heart.
- Features a smooth vertical scrollbar when more than 6 stations are installed.
- Highlights the currently active station with a gold border.
- Hovering over any station icon displays its name and interaction hints.
- Left-clicking any station icon switches the terminal interface to that station.

### Left Storage Network Overlay
- Located on the left side of the terminal interface.
- A live 3-column scrollable viewport into items currently stored in the Magic Storage network.
- **Left-Click an Item**: Withdraws a full stack into your hand or cursor.
- **Right-Click an Item**: Withdraws half of the stack.
- **Shift-Click an Item**: Quick-moves the stack directly into your player inventory.
- **Click with Carried Item**: Deposits your cursor item directly into the network.

### Virtual Server-Side Furnace
- When a Furnace, Blast Furnace, or Smoker is installed in the Storage Heart, the Heart activates a dedicated virtual furnace.
- Smelting happens continuously on the server, even after you close the GUI or leave the chunk.
- **Catch-Up Smelting**: If the chunk unloads while smelting is in progress, the Storage Heart calculates elapsed game ticks upon reload and catches up smelting progress automatically.
- Dedicated slots: Input slot (top), Fuel slot (bottom), and Output slot (right).
- Features animated burning flame indicator and cook progress arrow.
- Preserves fuel remainders (for example, empty buckets when using lava buckets as fuel).

### Instant Shift-Crafting
- Shift-clicking the crafting result slot crafts as many items as possible in a single click (up to player inventory capacity).
- Missing ingredients are automatically pulled from the Magic Storage network.
- Secondary drops (such as Tree Bark from cutting logs on a Cutting Board) are scaled by the craft count and awarded to the player.

### Safe Item Ejection
- Transient ingredients left in the crafting grid or station slots are never voided.
- Closing the GUI or switching station modes automatically returns leftover items into the Magic Storage network first, or to your inventory/feet if the network is full.
- Any item held on the cursor when closing the GUI is safely returned to network storage.

---

## Supported Crafting Stations

Install stations by shift-right-clicking the Storage Heart with the station item in hand. Up to 20 stations can be installed simultaneously.

### Vanilla Stations
- **Crafting Table**: Standard 3x3 shaped and shapeless crafting matrix.
- **Virtual Furnace / Blast Furnace / Smoker**: Server-side continuous smelting, ore blasting, and food smoking with animated fire and cook progress.
- **Stonecutter**: Single-item block cutting directly in the grid. Clickable variant buttons appear in the UI allowing you to pick your desired cut block. Supports variant sampling in the crafting grid.
- **Smithing Table**: Modern 3-slot smithing interface supporting Smithing Template + Base Item + Material Addition.
- **Grindstone**: 2-slot tool repair (combines durability with a 5% bonus and preserves curses) and 1-slot disenchanting (resets prior work penalty, strips non-curses, converts enchanted books back to normal books).
- **Loom**: Banner pattern crafting with Banner, Dye, and optional Banner Pattern item. Banner patterns are reusable and preserved.
- **Cartography Table**: Map zooming (Map + Paper), map locking (Map + Glass Pane), and map cloning (Map + Empty Map).
- **Anvil**: Repairing tools, combining enchantments, and renaming items.
- **Enchanting Table, Brewing Stand, Fletching Table, Cauldron**: Fully recognized as valid Heart stations.

### Farmer's Delight Integration
- **Cooking Pot**: 1 to 6 shapeless meal ingredients plus required serving container (Bowl, Glass Bottle). Validates recipes and outputs hot dishes.
- **Cutting Board**: Input block/item + cutting tool (Knife, Axe, Shears). Deals 1 point of durability damage to the tool, preserves the tool in the grid, consumes the ingredient, produces the primary cut, and grants secondary drops (such as Tree Bark or Straw) with sound effects.
- **Stove and Skillet**: Recognized as valid cooking and heating stations.

### Apotheosis Integration
- **Reforging Table**: Re-rolling affix rarities on gear using gem dust and materials.
- **Salvaging Table**: Breaking down affix equipment into scrap materials.
- **Gem Cutting Table**: Upgrading affix gems to higher rarity tiers.

### Tetra Integration
- **Modular Workbench**: Detects modular tool components and honing materials. Repairs modular tools and advances hone progress while strictly preserving modular schemas and socketed upgrades.

### Modded Stations Support
- Any modded station block that opens a GUI or implements `MenuProvider` can be placed into the Storage Heart.
- Modded stations appear on the right panel with 1:1 block icons.
- **Left-Click**: Selects the modded station mode in the terminal.
- **Shift-Click**: Opens the native GUI of the modded station. Remote distance checks are bypassed so you can use modded stations from anywhere in the world.

---

## Wireless Portable Remotes

Portable remotes grant access to your storage network from anywhere in your world.

### How to Bind a Remote
1. Hold the Portable Access remote in your hand.
2. Shift-right-click on a **Storage Heart** or **Remote Access** block.
3. A confirmation message will appear in your action bar confirming the bound coordinates and dimension.
4. Right-click in the air at any time to open your storage terminal remotely.

### Remote Tiers

| Remote | Item Registry ID | Range | Dimensional Scope |
| :--- | :--- | :---: | :--- |
| **Portable Access (Pre-HM)** | `magicstorage:portable_access_prehm` | 200 blocks | Same dimension only. |
| **Portable Access (HM)** | `magicstorage:portable_access_hm` | Unlimited | Same dimension only. |
| **Portable Access (Ultimate)** | `magicstorage:portable_access_ultimate` | Unlimited | Cross-dimensional (Overworld, Nether, End, modded dimensions). |
| **Portable Crafting Access (Pre-HM)** | `magicstorage:portable_crafting_access_prehm` | 200 blocks | Same dimension only. Full crafting and station access. |
| **Portable Crafting Access (HM)** | `magicstorage:portable_crafting_access_hm` | Unlimited | Same dimension only. Full crafting and station access. |
| **Portable Crafting Access (Ultimate)** | `magicstorage:portable_crafting_access_ultimate` | Unlimited | Cross-dimensional. Full crafting and station access. |

---

## Materials, Ores, and Progression

### Ores and Generation
- **Hellstone Ore** (`magicstorage:hellstone_ore`):
  - Spawns in the Nether between Y 5 and Y 31 in veins of up to 12 blocks.
  - Requires an iron-tier pickaxe or better to mine.
  - Smelted into **Hellstone Fragments** and combined with obsidian to forge **Hellstone Bars**.
- **Luminite Ore** (`magicstorage:luminite_ore`):
  - Spawns in the End between Y 10 and Y 65 embedded in End Stone in veins of up to 8 blocks.
  - Requires a diamond-tier pickaxe or better to mine.
  - Smelted into **Luminite Fragments** and forged into **Luminite Bars**.

### Crafting Materials
- **Storage Component** (`magicstorage:storage_component`): The fundamental building block crafted from wood, iron ingots, and chests. Used to construct Storage Hearts, Units, and Access terminals.
- **Hell Brick** (`magicstorage:hell_brick`): Fire-resistant building brick crafted from nether bricks and magma blocks.
- **Shadow Diamond** (`magicstorage:shadow_diamond`): Crafted from a Diamond, 2 Obsidian, a Demonite Ingot, and a Crimtane Ingot. Used for Pre-Hardmode remotes.
- **Radiant Jewel** (`magicstorage:radiant_jewel`): Crafted from a Nether Star, 2 Luminite Bars, Dragon's Breath, and an End Crystal. Used for high-tier upgrades.
- **Terra Singularity** (`magicstorage:terra_singularity`): End-game catalyst forged from Demonite, Crimtane, Hellstone, Hallowed, Chlorophyte, and Luminite bars combined with a Nether Star, Dragon's Breath, and End Crystal. Used to craft the Tier 8 Terra Storage Unit.

---

## Search and Sorting Syntax

The search bar at the top of the Storage Access and Crafting Access terminals supports real-time text matching and special search prefixes:

- **Plain Text**: Matches any item whose display name contains the query (for example, `iron` matches Iron Ingot, Iron Sword, etc.).
- **`@mod`**: Filters items by their mod namespace (for example, `@farmersdelight` shows only Farmer's Delight items; `@minecraft` shows vanilla items).
- **`#tag`**: Filters items by item tags (for example, `#logs` shows all wood logs; `#forge:ingots` shows all ingots).

### Sort Buttons
- **Sort by Quantity**: Orders items by total count in the network.
- **Sort by Name**: Orders items alphabetically by display name.
- **Sort by Mod**: Groups items by mod origin, then alphabetically.
- **Sort Direction Toggle**: Toggles ascending or descending order.

---

## Third-Party Mod Integrations

All integrations are soft dependencies. If a mod is not present, Modern Magic Storage runs normally without issues:

| Mod | Integration Details |
| :--- | :--- |
| **[Just Enough Items (JEI)](https://github.com/mezz/JustEnoughItems)** | Full recipe transfer handler (`+` icon) for Crafting, Stonecutting, Smithing, Smelting, Blasting, Smoking, Farmer's Delight Cooking, and Farmer's Delight Cutting. Pulls items from network storage and player inventory. |
| **[Farmer's Delight](https://github.com/vectorwing/FarmersDelight)** | Full station integration: Cooking Pot (1 to 6 meal ingredients with container matching) and Cutting Board (input + cutting tool durability damage with secondary drops). |
| **[Apotheosis](https://github.com/Shadows-of-Fire/Apotheosis)** | Station support for Reforging Table (affix re-rolls), Salvaging Table (scrap breakdown), and Gem Cutting Table (gem upgrades). |
| **[Tetra](https://github.com/mickelus/tetra)** | Modular Workbench support: detects modular tools, hone progress, and tool components. |
| **[Curios API](https://github.com/TheIllusiveC4/Curios)** | Accessory reforging anvils are recognized as valid Storage Heart stations. |
| **[Fair Experience](https://github.com/Brilliafy/ModernFairExperience)** | Calculates fair linear raw XP costs for anvil and enchanting operations. |
| **[Quality Equipment](https://www.curseforge.com/minecraft/mc-mods/quality-equipment)** | Recognizes reforging stations and anvils. |
| **[Reskillable Reimagined](https://www.curseforge.com/minecraft/mc-mods/reskillable-reimagined)** | Enforces player skill requirements before allowing interaction with locked items. |

---

## Local Testing and Building

To build test jars locally without publishing to the internet:

```bash
# Compile and copy all 6 mod jars to debug-jars/
./gradlew collectDebugJars
```

Testing jars will be placed into the `debug-jars/` directory:
- `magicstorage-2.0.0-fabric+1.20.1-SNAPSHOT.jar`
- `magicstorage-2.0.0-forge+1.20.1-SNAPSHOT.jar`
- `magicstorage-2.0.0-fabric+1.21.1-SNAPSHOT.jar`
- `magicstorage-2.0.0-neoforge+1.21.1-SNAPSHOT.jar`
- `magicstorage-2.0.0-fabric+26.3-SNAPSHOT.jar`
- `magicstorage-2.0.0-neoforge+26.3-SNAPSHOT.jar`

Copy the jar for your target Minecraft version and loader into your `.minecraft/mods` folder.

---

## Wiki Documentation

For in-depth guides, check the detailed wiki documentation in the [`docs/`](docs/) directory:

- [Getting Started and Multiblock Mechanics](docs/getting-started.md): Network building, BFS traversal rules, and chunk boundary management.
- [Blocks and Storage Units](docs/blocks-and-storage-units.md): Technical specs, 8 storage unit tiers, in-place upgrade mechanics, and drop protection.
- [Crafting Stations and Multi-Station GUI](docs/crafting-stations-and-gui.md): Deep-dive into every supported station, recipes, virtual furnace, and modded block integration.
- [Remotes, Materials, and Progression](docs/remotes-and-materials.md): Wireless remote binding, ores, world generation, and progression materials.
- [Third-Party Mod Integrations](docs/mod-integrations.md): JEI recipe transfer, Farmer's Delight, Apotheosis, Tetra, and accessory reforging.

---

## Credits and License

- **[Magic Storage (Terraria)](https://github.com/blushiemagic/MagicStorage)** by blushiemagic: Original game concept and inspiration.
- **[Jared's MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)**: Multi-platform project scaffolding.
- **[Stonecutter](https://github.com/kikugie/stonecutter)** by kikugie: Multi-version active controller and preprocessor.
- **License**: Modern Magic Storage is licensed under the [MIT License](LICENSE.txt).

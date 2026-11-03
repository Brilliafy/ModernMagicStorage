# Remotes, Materials, and Progression

This guide covers wireless remote tools, ores, world generation, crafting ingredients, and game progression in Modern Magic Storage.

---

## Table of Contents

- [Wireless Portable Remotes](#wireless-portable-remotes)
  - [Remote Types](#remote-types)
  - [Range and Scope Comparison](#range-and-scope-comparison)
  - [How to Bind a Remote](#how-to-bind-a-remote)
  - [Remote Access Relay Block](#remote-access-relay-block)
- [Ores and World Generation](#ores-and-world-generation)
  - [Hellstone Ore](#hellstone-ore)
  - [Luminite Ore](#luminite-ore)
- [Crafting Materials and Progression](#crafting-materials-and-progression)
  - [Storage Component](#storage-component)
  - [Hell Brick](#hell-brick)
  - [Ingots and Bars](#ingots-and-bars)
  - [Shadow Diamond](#shadow-diamond)
  - [Radiant Jewel](#radiant-jewel)
  - [Terra Singularity](#terra-singularity)
- [Storage Upgrade Items](#storage-upgrade-items)

---

## Wireless Portable Remotes

Portable Remotes allow you to interact with your Magic Storage network from anywhere without returning to your base.

### Remote Types

Modern Magic Storage provides two tool lines across three progression tiers:

1. **Portable Storage Access**: Opens the storage inspection terminal to deposit, withdraw, and search items.
2. **Portable Crafting Access**: Opens the complete multi-station crafting terminal, including the 3x3 crafting grid, right station selection panel, and left storage overlay.

### Range and Scope Comparison

| Remote Item | Registry ID | Max Range | Dimensional Scope | Rarity |
| :--- | :--- | :---: | :--- | :--- |
| **Portable Access (Pre-HM)** | `magicstorage:portable_access_prehm` | 200 blocks | Same dimension only | Common |
| **Portable Access (HM)** | `magicstorage:portable_access_hm` | Unlimited | Same dimension only | Uncommon |
| **Portable Access (Ultimate)** | `magicstorage:portable_access_ultimate` | Unlimited | Cross-dimensional | Epic |
| **Portable Crafting Access (Pre-HM)** | `magicstorage:portable_crafting_access_prehm` | 200 blocks | Same dimension only | Common |
| **Portable Crafting Access (HM)** | `magicstorage:portable_crafting_access_hm` | Unlimited | Same dimension only | Uncommon |
| **Portable Crafting Access (Ultimate)** | `magicstorage:portable_crafting_access_ultimate` | Unlimited | Cross-dimensional | Epic |

### How to Bind a Remote

1. Hold the Portable Access or Portable Crafting Access remote in your hand.
2. Approach your placed **Storage Heart** or a connected **Remote Access** block.
3. **Shift-Right-Click** the block.
4. An action bar message will confirm that the remote has been bound to the network's coordinates and dimension.
5. Right-click the remote in the air at any time to open the terminal.

### Remote Access Relay Block

The **Remote Access** block (`magicstorage:remote_access`) acts as an external antenna and relay point:
- Connect it adjacent to your main network.
- When binding remotes, players can shift-right-click the Remote Access block instead of navigating into deep storage vaults.
- The remote automatically discovers the Storage Heart linked through the network.

---

## Ores and World Generation

### Hellstone Ore
- **Registry ID**: `magicstorage:hellstone_ore`
- **Dimension**: The Nether.
- **Y-Level Range**: Spawns between **Y 5 and Y 31**.
- **Vein Size**: Up to 12 blocks per vein (12 placement count).
- **Mining Requirement**: Iron Pickaxe or better.
- **Processing**:
  - Smelting in a furnace produces **Hellstone Fragments** (`magicstorage:fragment_hellstone`).
  - Combining Hellstone Fragments with Obsidian produces **Hellstone Bars** (`magicstorage:bar_hellstone`).

### Luminite Ore
- **Registry ID**: `magicstorage:luminite_ore`
- **Dimension**: The End.
- **Y-Level Range**: Spawns embedded in End Stone between **Y 10 and Y 65**.
- **Vein Size**: Up to 8 blocks per vein.
- **Mining Requirement**: Diamond Pickaxe or better.
- **Processing**:
  - Smelting produces **Luminite Fragments** (`magicstorage:fragment_luminite`).
  - Forging Luminite Fragments produces **Luminite Bars** (`magicstorage:bar_luminite`).

---

## Crafting Materials and Progression

### Storage Component
- **Registry ID**: `magicstorage:storage_component`
- **Recipe**: 6 Wood Planks (any wood tag) + 2 Iron Ingots + 1 Wooden Chest.
- **Usage**: Scaffolding component required for crafting Storage Hearts, Storage Units, and Access terminals.

### Hell Brick
- **Registry ID**: `magicstorage:hell_brick`
- **Recipe**: 4 Nether Bricks (`minecraft:nether_brick`) + 5 Magma Blocks (`minecraft:magma_block`) in a checkerboard pattern. Yields 4 Hell Bricks.
- **Usage**: Heat-resistant brick used for Hellstone storage upgrades and decorative building.

### Ingots and Bars
- **Crimtane Ingot** (`magicstorage:ingot_crimtane`): Crafted from redstone, iron, and raw meat or found in dungeon loot. Used for Tier 1 Crimtane upgrades.
- **Demonite Ingot** (`magicstorage:ingot_demonite`): Crafted from lapis lazuli, iron, and rotten flesh. Used for Tier 2 Demonite upgrades.
- **Hellstone Bar** (`magicstorage:bar_hellstone`): Forged from Hellstone Fragments and Obsidian. Used for Tier 3 upgrades.
- **Hallowed Ingot** (`magicstorage:ingot_hallowed`): Forged from gold ingots, iron, and glowstone. Used for Tier 4 upgrades.
- **Chlorophyte Bar** (`magicstorage:bar_chlorophyte`): High-tier botanical alloy. Used for Tier 5 upgrades.
- **Luminite Bar** (`magicstorage:bar_luminite`): End-game stellar alloy smelted from Luminite Ore. Used for Tier 6 upgrades.

### Shadow Diamond
- **Registry ID**: `magicstorage:shadow_diamond`
- **Recipe (Shapeless)**: 1 Diamond + 2 Obsidian + 1 Demonite Ingot + 1 Crimtane Ingot.
- **Usage**: Core focusing gem used to craft Pre-Hardmode portable remotes.

### Radiant Jewel
- **Registry ID**: `magicstorage:radiant_jewel`
- **Recipe (Shapeless)**: 1 Nether Star + 2 Luminite Bars + 1 Dragon's Breath + 1 End Crystal.
- **Usage**: Concentrated celestial energy used to craft Tier 6 Luminite upgrades and Ultimate remotes.

### Terra Singularity
- **Registry ID**: `magicstorage:terra_singularity`
- **Recipe (Shapeless)**:
  - 1 Demonite Ingot
  - 1 Crimtane Ingot
  - 1 Hellstone Bar
  - 1 Hallowed Ingot
  - 1 Chlorophyte Bar
  - 1 Luminite Bar
  - 1 Nether Star
  - 1 Dragon's Breath
  - 1 End Crystal
- **Usage**: The culmination of all storage technology. Used to craft the Tier 7 Terra Storage Upgrade.

---

## Storage Upgrade Items

Hold the upgrade item and shift-right-click a placed Storage Unit to upgrade it in place:

| Upgrade Item | Registry ID | Source Tier | Target Tier | Target Capacity |
| :--- | :--- | :---: | :---: | :---: |
| **Crimtane Upgrade** | `magicstorage:upgrade_crimtane` | Basic (0) | Crimtane (1) | 54 slots |
| **Demonite Upgrade** | `magicstorage:upgrade_demonite` | Basic (0) | Demonite (2) | 54 slots |
| **Hellstone Upgrade** | `magicstorage:upgrade_hellstone` | Crimtane/Demonite | Hellstone (3) | 90 slots |
| **Hallowed Upgrade** | `magicstorage:upgrade_hallowed` | Hellstone | Hallowed (4) | 135 slots |
| **Blue Chlorophyte Upgrade** | `magicstorage:upgrade_blue_chlorophyte` | Hallowed | Chlorophyte (5) | 180 slots |
| **Luminite Upgrade** | `magicstorage:upgrade_luminite` | Chlorophyte | Luminite (6) | 252 slots |
| **Terra Upgrade** | `magicstorage:upgrade_terra` | Luminite | Terra (7) | 360 slots |

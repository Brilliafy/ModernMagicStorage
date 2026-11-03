[← Back to Wiki Hub](../README.md) | [Main Documentation](../../README.md)

# Magic Storage Recipe Catalog

This catalog provides exact crafting recipes, ingredients, yields, and station formulas for all items and blocks in **Modern Magic Storage**.

---

## Table of Contents

- [1. Core Components and Network Blocks](#1-core-components-and-network-blocks)
  - [Storage Component](#storage-component)
  - [Storage Heart](#storage-heart)
  - [Storage Access Terminal](#storage-access-terminal)
  - [Crafting Access Terminal](#crafting-access-terminal)
  - [Remote Access Relay Block](#remote-access-relay-block)
  - [Hell Bricks](#hell-bricks)
- [2. Storage Units (8 Tiers)](#2-storage-units-8-tiers)
- [3. Storage Upgrades](#3-storage-upgrades)
- [4. Progression Materials, Ores, and Jewels](#4-progression-materials-ores-and-jewels)
  - [Crimtane and Demonite Ingots](#crimtane-and-demonite-ingots)
  - [Hellstone Bars](#hellstone-bars)
  - [Hallowed Ingots](#hallowed-ingots)
  - [Chlorophyte Bars](#chlorophyte-bars)
  - [Luminite Bars](#luminite-bars)
  - [Shadow Diamond](#shadow-diamond)
  - [Radiant Jewel](#radiant-jewel)
  - [Terra Singularity](#terra-singularity)
- [5. Portable Storage Remotes](#5-portable-storage-remotes)
- [6. Portable Crafting Remotes](#6-portable-crafting-remotes)

---

## 1. Core Components and Network Blocks

### Storage Component
The foundational scaffolding material used to craft all storage blocks and upgrade items.

* **Pattern**:
  ```
  [Iron Ingot]   [Chest]      [Iron Ingot]
  [Chest]        [Redstone]   [Chest]
  [Iron Ingot]   [Chest]      [Iron Ingot]
  ```
* **Yield**: 1x Storage Component (`magicstorage:storage_component`)

---

### Storage Heart
The master controller of your storage network.

* **Pattern**:
  ```
  [Diamond]        [Sea Lantern]        [Diamond]
  [Emerald]        [Storage Component]  [Emerald]
  [Redstone Block] [Redstone Block]     [Redstone Block]
  ```
* **Yield**: 1x Storage Heart (`magicstorage:storage_heart`)

---

### Storage Access Terminal
The access terminal for browsing, searching, sorting, depositing, and withdrawing network items.

* **Pattern**:
  ```
  [Iron Ingot]   [Diamond]            [Iron Ingot]
  [Iron Ingot]   [Storage Component]  [Iron Ingot]
  [Iron Ingot]   [Chest]              [Iron Ingot]
  ```
* **Yield**: 1x Storage Access (`magicstorage:storage_access`)

---

### Crafting Access Terminal
The crafting terminal connected to your network with multi-station interfaces.

* **Pattern**:
  ```
  [Diamond]      [Clock]              [Diamond]
  [Diamond]      [Storage Component]  [Diamond]
  [Lapis Block]  [Lapis Block]        [Lapis Block]
  ```
* **Yield**: 1x Crafting Access (`magicstorage:crafting_access`)

---

### Remote Access Relay Block
The physical transceiver required to link wireless portable remotes to your Storage Heart.

* **Pattern**:
  ```
  [Obsidian]     [Ender Pearl]        [Obsidian]
  [Ender Pearl]  [Storage Component]  [Ender Pearl]
  [Obsidian]     [Diamond]            [Obsidian]
  ```
* **Yield**: 1x Remote Access (`magicstorage:remote_access`)

---

### Hell Bricks
A fire-resistant decorative building block and crafting ingredient for Hellstone upgrades.

* **Pattern**:
  ```
  [Nether Brick] [Nether Brick] [Nether Brick]
  [Nether Brick] [Magma Block]  [Nether Brick]
  [Nether Brick] [Nether Brick] [Nether Brick]
  ```
* **Yield**: 8x Hell Brick (`magicstorage:hell_brick`)

---

## 2. Storage Units (8 Tiers)

### Basic Storage Unit
The starting storage unit providing 36 item slots.

* **Pattern**:
  ```
  [Chest]   [Chest]              [Chest]
  [Chest]   [Storage Component]  [Chest]
  [Chest]   [Chest]              [Chest]
  ```
* **Yield**: 1x Basic Storage Unit (`magicstorage:storage_unit_basic`)

### Direct Tier Crafting
Higher tier units can be crafted directly by combining a Basic Storage Unit with the tier upgrade item:

```
[ Tier Upgrade Item ]
[ Basic Storage Unit ]
[      (Empty)       ]
```

* Upgrading a Basic Unit with `upgrade_crimtane` yields 1x Crimtane Storage Unit (54 slots).
* Upgrading a Basic Unit with `upgrade_demonite` yields 1x Demonite Storage Unit (54 slots).
* Upgrading a Basic Unit with `upgrade_hellstone` yields 1x Hellstone Storage Unit (90 slots).
* Upgrading a Basic Unit with `upgrade_hallowed` yields 1x Hallowed Storage Unit (135 slots).
* Upgrading a Basic Unit with `upgrade_blue_chlorophyte` yields 1x Blue Chlorophyte Storage Unit (180 slots).
* Upgrading a Basic Unit with `upgrade_luminite` yields 1x Luminite Storage Unit (252 slots).
* Upgrading a Basic Unit with `upgrade_terra` yields 1x Terra Storage Unit (360 slots).

---

## 3. Storage Upgrades

Upgrade items allow in-place upgrading of placed storage units via Shift-Right-Click:

### Crimtane Upgrade
* **Pattern**:
  ```
  [Crimtane Ingot] [Iron Block]         [Crimtane Ingot]
  [Redstone Block] [Storage Component]  [Redstone Block]
  [Crimtane Ingot] [Iron Block]         [Crimtane Ingot]
  ```
* **Yield**: 1x Crimtane Storage Upgrade (`magicstorage:upgrade_crimtane`)

### Demonite Upgrade
* **Pattern**:
  ```
  [Demonite Ingot] [Iron Block]         [Demonite Ingot]
  [Obsidian]       [Storage Component]  [Obsidian]
  [Demonite Ingot] [Iron Block]         [Demonite Ingot]
  ```
* **Yield**: 1x Demonite Storage Upgrade (`magicstorage:upgrade_demonite`)

### Hellstone Upgrade
* **Pattern**:
  ```
  [Hellstone Bar] [Blaze Rod]          [Hellstone Bar]
  [Hell Brick]    [Storage Component]  [Hell Brick]
  [Hellstone Bar] [Blaze Rod]          [Hellstone Bar]
  ```
* **Yield**: 1x Hellstone Storage Upgrade (`magicstorage:upgrade_hellstone`)

### Hallowed Upgrade
* **Pattern**:
  ```
  [Hallowed Ingot] [Diamond Block]      [Hallowed Ingot]
  [Ghast Tear]     [Storage Component]  [Ghast Tear]
  [Hallowed Ingot] [Diamond Block]      [Hallowed Ingot]
  ```
* **Yield**: 1x Hallowed Storage Upgrade (`magicstorage:upgrade_hallowed`)

### Blue Chlorophyte Upgrade
* **Pattern**:
  ```
  [Chlorophyte Bar] [Lapis Block]        [Chlorophyte Bar]
  [Sea Lantern]     [Storage Component]  [Sea Lantern]
  [Chlorophyte Bar] [Lapis Block]        [Chlorophyte Bar]
  ```
* **Yield**: 1x Blue Chlorophyte Storage Upgrade (`magicstorage:upgrade_blue_chlorophyte`)

### Luminite Upgrade
* **Pattern**:
  ```
  [Luminite Bar]  [Radiant Jewel]      [Luminite Bar]
  [Luminite Bar]  [Storage Component]  [Luminite Bar]
  [Luminite Bar]  [Shulker Shell]      [Luminite Bar]
  ```
* **Yield**: 1x Luminite Storage Upgrade (`magicstorage:upgrade_luminite`)

### Terra Upgrade
* **Pattern**:
  ```
  [Radiant Jewel] [Terra Singularity]  [Radiant Jewel]
  [Luminite Bar]  [Storage Component]  [Luminite Bar]
  [Nether Star]   [End Crystal]        [Nether Star]
  ```
* **Yield**: 1x Terra Storage Upgrade (`magicstorage:upgrade_terra`)

---

## 4. Progression Materials, Ores, and Jewels

### Crimtane and Demonite Ingots
* **Crimtane Ingot (Shapeless)**: 2x Iron Ingot + 1x Nether Wart + 1x Redstone Dust yields 2x Crimtane Ingot (`magicstorage:ingot_crimtane`).
* **Demonite Ingot (Shapeless)**: 2x Iron Ingot + 1x Obsidian + 1x Ender Pearl yields 2x Demonite Ingot (`magicstorage:ingot_demonite`).

### Hellstone Bars
* Mining Hellstone Ore in the Nether (Y 5 to Y 31) yields Hellstone Fragments.
* **Forging Pattern**:
  ```
  [Hellstone Fragment] [Hellstone Fragment] [Hellstone Fragment]
  [Hellstone Fragment] [Obsidian]           [Hellstone Fragment]
  [Hellstone Fragment] [Hellstone Fragment] [Hellstone Fragment]
  ```
* **Yield**: 1x Hellstone Bar (`magicstorage:bar_hellstone`).

### Hallowed Ingots
* **Pattern**:
  ```
  [Gold Ingot] [Diamond]         [Nether Quartz]
  [Gold Ingot] [Glowstone Dust]  [Nether Quartz]
  ```
* **Yield**: 2x Hallowed Ingot (`magicstorage:ingot_hallowed`).

### Chlorophyte Bars
* **Pattern**:
  ```
  [Gold Ingot] [Gold Ingot]   [Gold Ingot]
  [Emerald]    [Lapis Block]  [Emerald]
  [Gold Ingot] [Gold Ingot]   [Gold Ingot]
  ```
* **Yield**: 1x Chlorophyte Bar (`magicstorage:bar_chlorophyte`).

### Luminite Bars
* Mining Luminite Ore in the End (Y 10 to Y 65) yields Luminite Fragments.
* **Forging Pattern (2x2)**:
  ```
  [Luminite Fragment] [Luminite Fragment]
  [Luminite Fragment] [Luminite Fragment]
  ```
* **Yield**: 1x Luminite Bar (`magicstorage:bar_luminite`).

### Shadow Diamond
* **Crafting (Shapeless)**: 1x Diamond + 2x Obsidian + 1x Demonite Ingot + 1x Crimtane Ingot.
* **Yield**: 1x Shadow Diamond (`magicstorage:shadow_diamond`).

### Radiant Jewel
* **Crafting (Shapeless)**: 1x Nether Star + 2x Luminite Bar + 1x Dragon's Breath + 1x End Crystal.
* **Yield**: 1x Radiant Jewel (`magicstorage:radiant_jewel`).

### Terra Singularity
* **Crafting (Shapeless)**:
  - 1x Demonite Ingot
  - 1x Crimtane Ingot
  - 1x Hellstone Bar
  - 1x Hallowed Ingot
  - 1x Chlorophyte Bar
  - 1x Luminite Bar
  - 1x Nether Star
  - 1x Dragon's Breath
  - 1x End Crystal
* **Yield**: 1x Terra Singularity (`magicstorage:terra_singularity`).

---

## 5. Portable Storage Remotes

### Basic Portable Storage Access (Pre-HM)
* **Range**: 200 blocks (same dimension).
* **Pattern**:
  ```
  [Gold Ingot]   [Shadow Diamond]  [Gold Ingot]
  [Ender Pearl]  [Remote Access]   [Ender Pearl]
  [Diamond]      [Storage Access]  [Diamond]
  ```
* **Yield**: 1x Portable Storage Access (`magicstorage:portable_access_prehm`).

### Advanced Portable Storage Access (HM)
* **Range**: Unlimited (same dimension).
* **Pattern**:
  ```
  [Shulker Shell]  [Hallowed Ingot]           [Shulker Shell]
  [Popped Chorus]  [Portable Access Pre-HM]  [Popped Chorus]
  [Diamond Block]  [Eye of Ender]            [Diamond Block]
  ```
* **Yield**: 1x Portable Storage Access HM (`magicstorage:portable_access_hm`).

### Ultimate Portable Storage Access
* **Range**: Unlimited across all dimensions.
* **Pattern**:
  ```
  [Dragon's Breath] [Radiant Jewel]        [Dragon's Breath]
  [Luminite Bar]    [Portable Access HM]   [Luminite Bar]
  [End Crystal]     [Terra Singularity]    [End Crystal]
  ```
* **Yield**: 1x Ultimate Portable Storage Access (`magicstorage:portable_access_ultimate`).

---

## 6. Portable Crafting Remotes

### Basic Portable Crafting Access (Pre-HM)
* **Range**: 200 blocks (same dimension).
* **Direct Pattern**:
  ```
  [Gold Ingot]   [Shadow Diamond]   [Gold Ingot]
  [Ender Pearl]  [Remote Access]    [Ender Pearl]
  [Diamond]      [Crafting Access]  [Diamond]
  ```
* **Conversion Recipe (Shapeless)**: 1x Portable Access Pre-HM + 1x Crafting Table.
* **Yield**: 1x Portable Crafting Access (`magicstorage:portable_crafting_access_prehm`).

### Advanced Portable Crafting Access (HM)
* **Range**: Unlimited (same dimension).
* **Direct Pattern**:
  ```
  [Shulker Shell]  [Hallowed Ingot]                    [Shulker Shell]
  [Popped Chorus]  [Portable Crafting Access Pre-HM]   [Popped Chorus]
  [Diamond Block]  [Eye of Ender]                     [Diamond Block]
  ```
* **Conversion Recipe (Shapeless)**: 1x Portable Access HM + 1x Crafting Table.
* **Yield**: 1x Portable Crafting Access HM (`magicstorage:portable_crafting_access_hm`).

### Ultimate Portable Crafting Access
* **Range**: Unlimited across all dimensions.
* **Direct Pattern**:
  ```
  [Dragon's Breath] [Radiant Jewel]                [Dragon's Breath]
  [Luminite Bar]    [Portable Crafting Access HM]  [Luminite Bar]
  [End Crystal]     [Terra Singularity]            [End Crystal]
  ```
* **Conversion Recipe (Shapeless)**: 1x Portable Access Ultimate + 1x Crafting Table.
* **Yield**: 1x Ultimate Portable Crafting Access (`magicstorage:portable_crafting_access_ultimate`).

---

[← Back to Wiki Hub](../README.md) | [Main Documentation](../../README.md)

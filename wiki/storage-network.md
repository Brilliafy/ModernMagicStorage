[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

# Storage Network Architecture

This document covers network mechanics, storage unit tiers, in-place upgrades, custom world generation ores, materials, and wireless access remotes.

---

## 1. Network Components

### Storage Heart (`magicstorage:storage_heart`)
The central controller of the storage network.
* **Master Controller**: Coordinates all connected Storage Units, routes item insertion and extraction, and manages multi-block traversals.
* **Station Receptacle**: Contains a 20-slot inventory for installed crafting stations.
* **Virtual Furnace**: Houses a dedicated 3-slot virtual furnace (Input, Fuel, Output) that smelts continuously on the server.
* **Light Emission**: Emits light at **Light Level 15**.
* **Safety**: Breaking the Storage Heart safely drops all 20 installed station items and furnace contents. Stored items inside connected Storage Units remain completely intact.

### Storage Access (`magicstorage:storage_access`)
A dedicated item retrieval and deposit terminal. Provides full inventory browsing, search filtering by mod namespace and tags, and sorting controls.

### Crafting Access (`magicstorage:crafting_access`)
An integrated terminal combining network storage access with a 3x3 crafting matrix, multi-station processing, the right station selection panel, and the left storage network overlay.

### Remote Access (`magicstorage:remote_access`)
A physical transceiver block placed adjacent to the network. Acts as a binding target for wireless portable remotes so players do not need to navigate into deep storage vaults to rebind tools.

### Hell Bricks (`magicstorage:hell_brick`)
A fire-resistant decorative building block and crafting ingredient for Hellstone storage upgrades. Crafted from nether bricks and magma blocks.

---

## 2. Storage Unit Tiers and Capacity

Storage Units expand the total item capacity of the network. Right-clicking a storage unit directly opens its individual container view.

| Tier | Block Registry Name | Stack Slot Capacity | Rows (9 Slots/Row) | Direct Crafting Material |
| :---: | :--- | :---: | :---: | :--- |
| **0** | `magicstorage:storage_unit_basic` | **36** | 4 | Chests + Storage Component |
| **1** | `magicstorage:storage_unit_crimtane` | **54** | 6 | Basic Unit + Crimtane Upgrade |
| **2** | `magicstorage:storage_unit_demonite` | **54** | 6 | Basic Unit + Demonite Upgrade |
| **3** | `magicstorage:storage_unit_hellstone` | **90** | 10 | Basic Unit + Hellstone Upgrade |
| **4** | `magicstorage:storage_unit_hallowed` | **135** | 15 | Basic Unit + Hallowed Upgrade |
| **5** | `magicstorage:storage_unit_blue_chlorophyte` | **180** | 20 | Basic Unit + Blue Chlorophyte Upgrade |
| **6** | `magicstorage:storage_unit_luminite` | **252** | 28 | Basic Unit + Luminite Upgrade |
| **7** | `magicstorage:storage_unit_terra` | **360** | 40 | Basic Unit + Terra Upgrade |

---

## 3. In-Place Upgrades

Holding an upgrade item and Shift-Right-Clicking a placed storage unit in the world upgrades the block in-place. All items stored within the unit are preserved during the upgrade process with zero risk of item loss. Any lower-tier unit can be directly upgraded to a higher tier.

| Upgrade Item | Target Tier | Capacity | Key Ingredients |
| :--- | :---: | :---: | :--- |
| `magicstorage:upgrade_crimtane` | Crimtane | **54 Slots** | 4x Crimtane Ingots, 2x Iron Blocks, 2x Redstone Blocks |
| `magicstorage:upgrade_demonite` | Demonite | **54 Slots** | 4x Demonite Ingots, 2x Iron Blocks, 2x Obsidian |
| `magicstorage:upgrade_hellstone` | Hellstone | **90 Slots** | 4x Hellstone Bars, 2x Blaze Rods, 2x Hell Bricks |
| `magicstorage:upgrade_hallowed` | Hallowed | **135 Slots** | 4x Hallowed Ingots, 2x Diamond Blocks, 2x Ghast Tears |
| `magicstorage:upgrade_blue_chlorophyte` | Blue Chlorophyte | **180 Slots** | 4x Chlorophyte Bars, 2x Lapis Blocks, 2x Sea Lanterns |
| `magicstorage:upgrade_luminite` | Luminite | **252 Slots** | 4x Luminite Bars, 1x Radiant Jewel, 1x Shulker Shell |
| `magicstorage:upgrade_terra` | Terra | **360 Slots** | 1x Terra Singularity, 2x Radiant Jewels, 2x Luminite Bars, 1x Nether Star, 2x End Crystals |

---

## 4. Custom Materials, Jewels, and World Generation Ores

Modern Magic Storage introduces custom materials and ores to govern balanced tier progression:

### Hellstone Ore (`magicstorage:hellstone_ore`) and Hellstone Bar
* **Where to Find**: Spawns in the **Nether** deep beneath the lava ocean level (**strictly Y 5 to Y 31**) embedded in Netherrack.
* **Mining Mechanics**: Requires an Iron Pickaxe or better. Drops **Hellstone Fragments** (`magicstorage:fragment_hellstone`). The quantity dropped is multiplied by the Fortune enchantment. Silk Touch yields the ore block directly.
* **Smelting**: Raw ore blocks smelt into Hellstone Fragments.
* **Hellstone Bars**: Forged from 3x Hellstone Fragments and 1x Obsidian inside a crafting grid.

### Luminite Ore (`magicstorage:luminite_ore`) and Luminite Bar
* **Where to Find**: Spawns in the **End** embedded in End Stone (**Y 10 to Y 65**) in veins of up to 8 blocks.
* **Mining Mechanics**: Requires a Diamond Pickaxe or better. Drops **Luminite Fragments** (`magicstorage:fragment_luminite`). Fortune enchantment scales fragment drops. Silk Touch yields the ore block.
* **Smelting**: Ore blocks smelt into Luminite Fragments.
* **Luminite Bars**: Forged from 4x Luminite Fragments.

### Shadow Diamond (`magicstorage:shadow_diamond`)
An abyssal diamond infused with Demonite and Crimtane dark magic.
* **Crafting (Shapeless)**: 1x Diamond, 2x Obsidian, 1x Demonite Ingot, 1x Crimtane Ingot.
* **Uses**: Required to craft the **Pre-Hardmode Portable Storage Access** and **Pre-Hardmode Portable Crafting Access** remotes.

### Radiant Jewel (`magicstorage:radiant_jewel`)
A prism radiating celestial stellar energy.
* **Crafting (Shapeless)**: 1x Nether Star, 2x Luminite Bars, 1x Dragon's Breath, 1x End Crystal.
* **Uses**: Required to craft the **Ultimate Portable Storage Access**, **Ultimate Portable Crafting Access**, **Luminite Storage Upgrade**, and **Terra Storage Upgrade**.

### Terra Singularity (`magicstorage:terra_singularity`)
The cosmic synthesis of all six metal tiers and endgame boss drops.
* **Crafting (Shapeless)**: 1x Demonite Ingot, 1x Crimtane Ingot, 1x Hellstone Bar, 1x Hallowed Ingot, 1x Chlorophyte Bar, 1x Luminite Bar, 1x Nether Star, 1x Dragon's Breath, 1x End Crystal.
* **Uses**: Required for the **Terra Storage Upgrade** (360 slots) and **Ultimate Portable Crafting Access** remote.

---

## 5. Wireless Portable Remotes

Portable remotes provide wireless access to your storage network from anywhere in your world.

### How to Bind a Remote
1. Hold the remote in your main hand.
2. Approach your placed **Storage Heart** or connected **Remote Access** block.
3. **Shift-Right-Click** the block.
4. An action bar message will confirm that the remote has been bound to the network coordinates and dimension.
5. Right-click the remote in the air to open the terminal.

```
[Portable Remote] ---> Wireless Signal ---> [Remote Access / Heart] <---> [Storage Units]
```

| Remote Item | Registry Name | Terminal Type | Operating Range | Cross-Dimensional |
| :--- | :--- | :---: | :---: | :---: |
| **Basic Portable Storage Access** | `magicstorage:portable_access_prehm` | Storage Access | 200 Blocks | No |
| **Advanced Portable Storage Access** | `magicstorage:portable_access_hm` | Storage Access | Unlimited | No |
| **Ultimate Portable Storage Access** | `magicstorage:portable_access_ultimate` | Storage Access | Unlimited | **Yes** |
| **Basic Portable Crafting Access** | `magicstorage:portable_crafting_access_prehm` | Crafting Access | 200 Blocks | No |
| **Advanced Portable Crafting Access** | `magicstorage:portable_crafting_access_hm` | Crafting Access | Unlimited | No |
| **Ultimate Portable Crafting Access** | `magicstorage:portable_crafting_access_ultimate` | Crafting Access | Unlimited | **Yes** |

---

[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

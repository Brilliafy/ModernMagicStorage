# Blocks and Storage Units

This guide provides technical specifications, capacity stats, and operational details for every block in Modern Magic Storage.

---

## Table of Contents

- [Storage Heart](#storage-heart)
- [Storage Units and Capacity Tiers](#storage-units-and-capacity-tiers)
  - [In-Place Upgrades](#in-place-upgrades)
  - [Tier Breakdown Table](#tier-breakdown-table)
  - [Tier Specifications](#tier-specifications)
- [Storage Access](#storage-access)
- [Crafting Access](#crafting-access)
- [Remote Access](#remote-access)
- [Decorative and Ore Blocks](#decorative-and-ore-blocks)
  - [Hell Brick](#hell-brick)
  - [Hellstone Ore](#hellstone-ore)
  - [Luminite Ore](#luminite-ore)

---

## Storage Heart

- **Registry ID**: `magicstorage:storage_heart`
- **Block Class**: `BlockStorageHeart`
- **Block Entity**: `StorageHeartBlockEntity`
- **Properties**:
  - Hardness: 3.0
  - Blast Resistance: 6.0
  - Sound: Metal
  - Light Emission: 15 (bright light source)
  - Tool Required: Pickaxe

### Capabilities and Internal Slots
The Storage Heart coordinates the entire network and contains **23 internal container slots**:
- **Slots 0 to 19 (Station Inventory)**: Holds up to 20 installed crafting stations.
- **Slot 20 (Furnace Input)**: Item to be smelted in the virtual furnace.
- **Slot 21 (Furnace Fuel)**: Fuel used to power the virtual furnace.
- **Slot 22 (Furnace Output)**: Finished smelted items.

### Player Interactions
- **Right-Click**: Opens the primary Storage Access network interface.
- **Shift-Right-Click with a Station Item**: Installs the station into the first available station slot (0 to 19). Displays a confirmation message in the action bar.
- **Shift-Right-Click with an Empty Hand**: Removes the most recently installed station and returns it to your inventory or drops it if inventory is full.

### Destruction and Safety
When the Storage Heart is broken:
- All 20 installed station items and all virtual furnace contents (inputs, fuels, outputs) drop safely into the world.
- The block entity calls `NetworkLocator.markNetworkDirty(level, pos)` to inform any connected terminals that the network has been disconnected.

---

## Storage Units and Capacity Tiers

Storage Units are the physical storage containers of the network.

- **Block Class**: `BlockStorageUnit`
- **Block Entity**: `StorageUnitBlockEntity`
- **Tiers**: 8 progressive tiers ranging from 36 to 360 slots.

### In-Place Upgrades
Instead of breaking a storage unit and spilling hundreds of items on the floor:
1. Craft the appropriate Storage Upgrade item (for example, `upgrade_hellstone`).
2. Shift-right-click the placed Storage Unit while holding the upgrade item.
3. The block state updates to the new tier, its internal capacity increases, and all stored items are preserved without dropping.
4. An anvil sound effect confirms the successful upgrade.

### Tier Breakdown Table

| Tier | Block Name | Registry ID | Upgrade Item | Capacity (Slots) | Double Chest Equiv. |
| :---: | :--- | :--- | :--- | :---: | :---: |
| **0** | Basic Storage Unit | `magicstorage:storage_unit_basic` | N/A (Base Block) | 36 slots | ~0.67 chests |
| **1** | Crimtane Storage Unit | `magicstorage:storage_unit_crimtane` | `magicstorage:upgrade_crimtane` | 54 slots | 1.0 double chest |
| **2** | Demonite Storage Unit | `magicstorage:storage_unit_demonite` | `magicstorage:upgrade_demonite` | 54 slots | 1.0 double chest |
| **3** | Hellstone Storage Unit | `magicstorage:storage_unit_hellstone` | `magicstorage:upgrade_hellstone` | 90 slots | ~1.67 double chests |
| **4** | Hallowed Storage Unit | `magicstorage:storage_unit_hallowed` | `magicstorage:upgrade_hallowed` | 135 slots | 2.5 double chests |
| **5** | Blue Chlorophyte Storage Unit | `magicstorage:storage_unit_blue_chlorophyte` | `magicstorage:upgrade_blue_chlorophyte` | 180 slots | ~3.33 double chests |
| **6** | Luminite Storage Unit | `magicstorage:storage_unit_luminite` | `magicstorage:upgrade_luminite` | 252 slots | ~4.67 double chests |
| **7** | Terra Storage Unit | `magicstorage:storage_unit_terra` | `magicstorage:upgrade_terra` | 360 slots | ~6.67 double chests |

### Tier Specifications

#### Tier 0: Basic Storage Unit
- **Capacity**: 36 item slots.
- **Crafting Recipe**: 6 Oak Wood (or any wood tag), 1 Iron Ingot, 1 Chest, 1 Storage Component.
- **Role**: Early-game starter unit.

#### Tier 1: Crimtane Storage Unit & Tier 2: Demonite Storage Unit
- **Capacity**: 54 item slots.
- **Crafting Upgrade**: Basic unit upgraded with Crimtane or Demonite Ingots.
- **Role**: Early-game expansion matching vanilla double chest capacity.

#### Tier 3: Hellstone Storage Unit
- **Capacity**: 90 item slots.
- **Crafting Upgrade**: Upgraded with Hellstone Bars smelted from Nether Hellstone Ore.
- **Role**: Mid-game fire-forged capacity.

#### Tier 4: Hallowed Storage Unit
- **Capacity**: 135 item slots.
- **Crafting Upgrade**: Upgraded with Hallowed Ingots.
- **Role**: Mid-game expansion providing over 2.5 double chests of storage in a single block.

#### Tier 5: Blue Chlorophyte Storage Unit
- **Capacity**: 180 item slots.
- **Crafting Upgrade**: Upgraded with Chlorophyte Bars.
- **Role**: Advanced high-capacity biological vault.

#### Tier 6: Luminite Storage Unit
- **Capacity**: 252 item slots.
- **Crafting Upgrade**: Upgraded with Luminite Bars from the End.
- **Role**: End-game storage unit holding up to 16,128 items (if stacked to 64).

#### Tier 7: Terra Storage Unit
- **Capacity**: 360 item slots.
- **Crafting Upgrade**: Upgraded with the Terra Singularity.
- **Role**: The ultimate storage block. A single Terra Storage Unit holds 360 item stacks (over 23,000 items).

---

## Storage Access

- **Registry ID**: `magicstorage:storage_access`
- **Properties**: Hardness: 3.0 | Blast Resistance: 6.0 | Sound: Metal
- **Role**: Dedicated storage inspection terminal.
- **Interface Features**:
  - **Search Bar**: Real-time filtering with `@mod` and `#tag` support.
  - **Sorting Controls**: Sort by Quantity, Name, or Mod; toggle Ascending or Descending.
  - **Deposit All Button**: Transfers compatible items from player inventory into network storage.
  - **Restock Button**: Refills incomplete item stacks in player inventory from network stock.
  - **Mirrored Network Slots**: Displays 36 items per page. Left-click to take full stack, right-click to take half stack, shift-click to move to inventory.

---

## Crafting Access

- **Registry ID**: `magicstorage:crafting_access`
- **Properties**: Hardness: 3.0 | Blast Resistance: 6.0 | Sound: Metal
- **Role**: Centralized crafting hub.
- **Interface Features**:
  - Full 3x3 crafting matrix connected to network stock.
  - **Right Station Selection Panel**: 1:1 icons of all stations installed in the Storage Heart.
  - **Left Storage Network Overlay**: Live 3-column scrollable inventory viewport.
  - **Instant Craft All**: Shift-clicking result crafts maximum possible items pulling directly from the network.
  - **Safe Ejection**: Transient grid ingredients are safely returned to network storage when closing the terminal.

---

## Remote Access

- **Registry ID**: `magicstorage:remote_access`
- **Properties**: Hardness: 3.0 | Blast Resistance: 6.0 | Sound: Metal
- **Role**: Relay terminal for wireless remotes.
- **Usage**:
  - Connects to an existing storage network via normal adjacency.
  - Shift-right-click a placed Remote Access block with a Portable Access remote to bind that remote to the network's Storage Heart.

---

## Decorative and Ore Blocks

### Hell Brick
- **Registry ID**: `magicstorage:hell_brick`
- **Properties**: Hardness: 2.0 | Blast Resistance: 6.0 | Sound: Stone
- **Crafting**: 4 Nether Bricks + 5 Magma Blocks in a checkerboard pattern yields 4 Hell Bricks.
- **Role**: Fire-resistant decorative building block and crafting ingredient for Hellstone tier items.

### Hellstone Ore
- **Registry ID**: `magicstorage:hellstone_ore`
- **Properties**: Hardness: 3.5 | Blast Resistance: 6.0 | Tool: Iron Pickaxe or better
- **World Generation**: Spawns in the Nether between Y 5 and Y 31 in veins of up to 12 blocks.
- **Smelting**: Smelts into **Hellstone Fragments** (or Hellstone Bars when forged with obsidian).

### Luminite Ore
- **Registry ID**: `magicstorage:luminite_ore`
- **Properties**: Hardness: 4.0 | Blast Resistance: 9.0 | Tool: Diamond Pickaxe or better
- **World Generation**: Spawns in the End embedded in End Stone between Y 10 and Y 65 in veins of up to 8 blocks.
- **Smelting**: Smelts into **Luminite Fragments** and forged into **Luminite Bars**.

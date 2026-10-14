[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

# Getting Started with Modern Magic Storage

Modern Magic Storage centralizes inventory management, item searching, multi-station crafting, and wireless access into a modular connected network inspired by Terraria's Magic Storage mod.

---

## 1. Minimal Network Setup

A functioning network requires three blocks placed directly adjacent to one another:

1. **Storage Heart**: The master controller, station receptacle, and virtual furnace.
2. **Storage Unit**: The modular block that provides item storage slots.
3. **Storage Access** or **Crafting Access**: The terminal interface used to deposit, search, withdraw, and craft items.

```
+------------------+    +------------------+    +------------------+
|   Storage Unit   | <-> |  Storage Heart   | <-> | Crafting Access  |
|  (Holds Items)   |    |  (Master Brain)  |    | (Grid & Terminal)|
+------------------+    +------------------+    +------------------+
```

### Quick Build Steps
1. Place down the **Storage Heart** (`magicstorage:storage_heart`). It emits Light Level 15 to illuminate your work area.
2. Place a **Basic Storage Unit** (`magicstorage:storage_unit_basic`) touching any face of the Storage Heart. Each Basic unit adds 36 slots.
3. Place a **Crafting Access** (`magicstorage:crafting_access`) touching the Storage Heart or the Storage Unit.
4. Right-click the Crafting Access block to begin storing and crafting.

---

## 2. Network Layout Rules

* **Direct Face Adjacency**: Network blocks must share a direct face (North, South, East, West, Up, Down). Diagonal placement does not connect.
* **Chaining Allowed**: You can chain Storage Units and Access terminals in any physical arrangement (lines, walls, floors, cubes). Each block connects through its neighbor.
* **1024 Connected Nodes**: The Storage Heart uses an iterative Breadth-First Search (BFS) to explore up to **1024 connected network blocks**.
* **Dynamic Network Discovery**: Placing or breaking any network block immediately triggers `NetworkLocator.markNetworkDirty(level, pos)`. The Storage Heart automatically rescans and updates network capacity on its next tick without requiring world reloads.

---

## 3. Chunk Boundary Handling and Liveness

Modern Magic Storage handles multi-chunk networks cleanly:

* **Chunk Boundary Traversal**: When scanning connected units, the Storage Heart verifies whether adjacent blocks lie across chunk boundaries. On dedicated servers and integrated singleplayer servers, adjacent chunks are automatically loaded so connected units are never dropped from the inventory.
* **Menu Liveness**: While any player has a terminal GUI open (including wireless access from thousands of blocks away), the Storage Heart and all connected units are kept loaded in memory.
* **Anti-Fragmentation Insertion**: When items are deposited, the network executes a two-pass insertion:
  1. **Pass 1 (Merge)**: Scans all connected units to find existing partial stacks of that item, filling them to maximum capacity.
  2. **Pass 2 (Empty Slots)**: Places any remaining items into available empty slots across the network.
  3. This ensures your items are tightly packed and slots are never wasted.

---

## 4. Terminal Interface and Search Controls

Opening a Storage Access or Crafting Access terminal presents the network inventory grid:

| Control | Action | Function |
| :--- | :--- | :--- |
| **Search Bar** | Type text | Real-time text filtering matching item display names. |
| **`@mod` Prefix** | Type `@modname` | Filters items by mod namespace (for example, `@farmersdelight` or `@minecraft`). |
| **`#tag` Prefix** | Type `#tagname` | Filters items by tag (for example, `#logs` or `#ingots`). |
| **Sort by Quantity** | Click button | Sorts inventory by total item count in descending or ascending order. |
| **Sort by Name** | Click button | Sorts inventory alphabetically by display name. |
| **Sort by Mod** | Click button | Groups items by mod origin, then alphabetically. |
| **Sort Direction** | Click button | Toggles ascending or descending sort direction. |
| **Deposit All** | Click button | Deposits all matching items from player inventory into the network. |
| **Restock** | Click button | Refills incomplete item stacks in player inventory from network stock. |

---

## 5. Keybindings and Shortcuts

| Shortcut | Context | Behavior |
| :--- | :--- | :--- |
| **Shift + Right-Click** | Storage Heart (Holding Station) | Installs the held crafting station into the Storage Heart (holds up to 20 stations). |
| **Shift + Right-Click** | Storage Heart (Empty Hand) | Ejects the most recently installed crafting station. |
| **Shift + Right-Click** | Storage Unit (Holding Upgrade) | Upgrades the Storage Unit in-place without dropping stored items. |
| **Right-Click** | Storage Unit (Empty Hand) | Opens the individual storage unit container directly. |
| **Left-Click** | Item in Terminal / Overlay | Withdraws 1 full stack into cursor or hand. |
| **Right-Click** | Item in Terminal / Overlay | Withdraws half of the stack into cursor or hand. |
| **Shift + Left-Click** | Item in Terminal / Overlay | Quick-moves the stack directly into player inventory. |
| **Click with Carried Item** | Storage Overlay | Deposits the carried stack directly into network storage. |
| **Shift + Left-Click** | Crafting Result Slot | Crafts the maximum possible count in a single click, pulling ingredients from network stock. |

---

## 6. Progression Overview

Modern Magic Storage features an 8-tier progression spanning all dimensions:

1. **Overworld / Early Game**: Craft Basic Storage Units (36 slots). Upgrade with **Crimtane** or **Demonite** Upgrades (54 slots) forged from evil biome metals, iron blocks, and redstone/obsidian.
2. **The Nether (Hellstone Tier - 90 slots)**: Mine **Hellstone Ore** in the deep Nether (**strictly Y 5 to Y 31**). Smelt into Hellstone Fragments and forge into **Hellstone Bars** with Obsidian.
3. **Mid Game (Hallowed Tier - 135 slots)**: Forge **Hallowed Ingots** from gold, diamonds, glowstone, and quartz to expand capacity past two double chests per block.
4. **Late Game (Blue Chlorophyte Tier - 180 slots)**: Synthesize **Chlorophyte Bars** with lapis and sea lanterns for advanced botanical storage.
5. **The End (Luminite Tier - 252 slots)**: Mine celestial **Luminite Ore** in the outer End islands (**Y 10 to Y 65**). Smelt into fragments and forge **Luminite Bars**.
6. **End Game (Terra Tier - 360 slots)**: Synthesize all 6 metal tiers with endgame boss drops (Nether Star, Dragon's Breath, End Crystal) to forge the **Terra Singularity** for the ultimate 360-slot storage vault.

---

[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

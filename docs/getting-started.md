# Getting Started and Multiblock Mechanics

This guide explains how to construct, expand, and maintain a Modern Magic Storage network.

---

## Table of Contents

- [Core Architecture](#core-architecture)
- [Building Your First Network](#building-your-first-network)
- [Multiblock Adjacency Rules](#multiblock-adjacency-rules)
- [Network Node Traversal (BFS)](#network-node-traversal-bfs)
- [Dynamic Network Updates](#dynamic-network-updates)
- [Chunk Boundary Handling and Safety](#chunk-boundary-handling-and-safety)
- [Best Practices and Tips](#best-practices-and-tips)

---

## Core Architecture

Modern Magic Storage uses a decentralized multiblock architecture where blocks connect by physical adjacency. Unlike pipe or cable networks that require complex routing logic, Modern Magic Storage blocks communicate through direct block contact:

1. **Storage Heart**: The central brain that indexes all items, runs the virtual furnace, and holds crafting stations.
2. **Storage Units**: Modular vaults that store items. Each placed unit expands total network capacity.
3. **Access Blocks**: Terminals that provide user interfaces for depositing, withdrawing, searching, and crafting.

Any network block placed directly adjacent (touching any of the 6 faces) to another network block becomes part of the same network.

---

## Building Your First Network

To build your first functional network, you will need:

1. **1 Storage Heart** (`magicstorage:storage_heart`)
2. **At least 1 Storage Unit** (`magicstorage:storage_unit_basic`)
3. **1 Storage Access** (`magicstorage:storage_access`) or **1 Crafting Access** (`magicstorage:crafting_access`)

### Step-by-Step Setup

1. **Place the Storage Heart**:
   - Choose a central location. The Storage Heart emits Light Level 15, illuminating your room.
2. **Place Storage Units Adjacent to the Heart**:
   - Place your Storage Units directly touching the Storage Heart, or touching other already connected Storage Units.
   - Each Basic Storage Unit provides 36 item slots (equal to a chest plus hotbar).
3. **Place an Access Terminal**:
   - Place a **Storage Access** or **Crafting Access** block touching the Heart, a Storage Unit, or any other connected network block.
4. **Open the Terminal**:
   - Right-click the Storage Access or Crafting Access block.
   - You can also right-click the Storage Heart directly to open the Storage Access interface.

---

## Multiblock Adjacency Rules

- **Valid Network Blocks**:
  - `BlockStorageHeart`: Network controller (each network typically has 1 Heart).
  - `BlockStorageUnit`: Storage capacity provider (Basic through Terra tiers).
  - `BlockStorageAccess`: Item search and withdrawal terminal.
  - `BlockCraftingAccess`: Crafting terminal with multi-station interface.
  - `BlockRemoteAccess`: Wireless relay block for remote tools.
- **Connection Rule**: Blocks must share a direct face (North, South, East, West, Up, or Down). Diagonal connections are not valid.
- **Chaining**: Storage Units and Access blocks can act as bridges. For example, a Crafting Access 10 blocks away will connect to the Storage Heart as long as there is an unbroken chain of Storage Units connecting them.

---

## Network Node Traversal (BFS)

When a network recalculates:

1. The Storage Heart performs a Breadth-First Search (BFS) starting at its own coordinates.
2. The search inspects all 6 neighboring blocks.
3. If a neighbor is a Storage Unit, it is registered into the network's unit list and added to the search queue.
4. If a neighbor is an Access block, it is registered into the network's terminal list and added to the search queue.
5. Traversal explores up to **1024 connected nodes**. This allows massive multiblock structures holding tens of thousands of item slots.

---

## Dynamic Network Updates

You do not need to restart your game or re-link terminals when modifying your network:

- **Placing a Block**: Placing a new Storage Unit or Access block immediately calls `NetworkLocator.markNetworkDirty(level, pos)`. The Storage Heart detects the new block and updates total network capacity on its next tick.
- **Breaking a Block**: Breaking a Storage Unit or Access block triggers `markNetworkDirty`. The network immediately recalculates its connected blocks so broken units are removed from the item registry.
- **In-Place Upgrades**: Upgrading a Storage Unit with an upgrade item does not trigger a network disconnection. Stored items are preserved in place without interruption.

---

## Chunk Boundary Handling and Safety

Large storage networks often span across Minecraft chunk boundaries. Modern Magic Storage includes built-in safeguards to prevent item loss or phantom missing items:

1. **Traversal Chunkloading**: When the Storage Heart runs its network refresh or queries valid units, it verifies if neighboring coordinates reside in unloaded chunks. On dedicated servers and integrated singleplayer servers, unloaded chunks are loaded automatically.
2. **Menu Liveness**: While any player has a Storage Access or Crafting Access menu open (including wireless access from far away), the Storage Heart and all connected units remain loaded in memory.
3. **Two-Pass Item Insertion**:
   - **Pass 1 (Merge)**: The network scans all connected units to find existing partial stacks of the inserted item, filling them to maximum stack size.
   - **Pass 2 (Empty Slots)**: Any remaining items are placed into available empty slots across the network.
   - This two-pass system completely prevents slot fragmentation across your storage multiblock.

---

## Best Practices and Tips

- **Expand Horizontally or Vertically**: You can build your storage units into decorative walls, floors, or pillars. They look sleek and keep your base organized.
- **Upgrade in Place**: Do not break filled storage units to upgrade them. Simply hold an upgrade item (Crimtane, Demonite, Hellstone, etc.) and shift-right-click the placed unit.
- **Install Crafting Stations Early**: Shift-right-click your Storage Heart with a Crafting Table and Furnace as soon as you craft them. This lets you craft and smelt directly from your network inventory.

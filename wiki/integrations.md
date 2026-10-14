[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

# Mod Integrations and Compatibility

Modern Magic Storage features native integration hooks, dynamic station compatibility, and verified interactions with prominent modern Minecraft mods. All integrations are implemented as optional soft dependencies.

---

## 1. Just Enough Items (JEI)

> [!IMPORTANT]
> Installing **JEI** is strongly recommended for full dynamic recipe visibility across all station modes.

* **Recipe Transfer Handlers (`+` button)**: 1-click recipe population across all standard crafting, stonecutting, smithing upgrades, furnace smelting, blast furnace, smoker, cooking pot meals, and cutting board recipes.
* **Network Inventory Pull**: Ingredients can be transferred directly from the Magic Storage network into the crafting grid.
* **Plugin Architecture**: Modern Magic Storage provides an annotated `@JeiPlugin` implementation (`MagicStorageJeiPlugin`) registered with unique identifier `magicstorage:jei_plugin`.

---

## 2. Farmer's Delight

* **Cooking Pot**: Supports 1 to 6 shapeless meal ingredients with serving container verification. Hot meals can be prepared in bulk via instant shift-crafting.
* **Cutting Board**: Processes item slicing using knives, axes, or shears. Tool durability is decremented by 1 per craft, the tool remains in the grid, and secondary drops (such as Tree Bark or Straw) are granted with audio effects.
* **Stove and Skillet**: Recognized by the Storage Heart as valid heating and cooking stations.

---

## 3. Apotheosis

* **Reforging Table**: Supported inside the Crafting Access terminal for re-rolling affix rarities on gear using gem dust and material additions.
* **Salvaging Table**: Supported for breaking down unwanted affix gear into scrap and crafting materials.
* **Gem Cutting Table**: Supported for upgrading affix gems to higher rarity tiers.

---

## 4. Tetra

* **Modular Workbench**: Recognized as a valid station within the Storage Heart.
* **Tool Maintenance**: Detects modular tool components and honing materials, repairing tools and advancing hone progress while strictly preserving modular schemas and socketed upgrades.

---

## 5. Curios API and Accessory Mods

* **Accessory Reforging**: Accessory reforging anvils, reforging tablets, and modification stations from Curios, Bountiful Baubles, and related accessory mods are recognized as valid Storage Heart stations.

---

## 6. Fair Experience

* **Balanced XP Economy**: Integrates with Fair Experience to compute fair raw XP values for anvil operations and enchanting station interactions, replacing vanilla's exponential level penalties with a balanced, linear XP cost.

---

## 7. Quality Equipment

* **Equipment Reforging**: Quality Equipment reforging stations are automatically recognized as valid Heart stations, enabling equipment attribute re-rolling directly from the Crafting Access terminal.

---

## 8. Reskillable Reimagined

* **Skill Requirement Verification**: Validates player skill requirements before allowing crafting or withdrawal of restricted equipment. If a player lacks the necessary skill level, interaction is prevented and a detailed notification is displayed.

---

## 9. Generic Modded Stations

* **Dynamic MenuProvider Discovery**: Any modded block that opens a container GUI or implements `MenuProvider` can be installed into the Storage Heart.
* **1:1 Station Icons**: The station appears on the right panel with its native item icon.
* **Remote Distance Bypass**: When opening a modded station via shift-click, the Storage Heart supplies `player.blockPosition()` as the virtual interaction position. This bypasses vanilla and modded distance checks (`player.distanceToSqr <= 64.0`), allowing players to use their favorite modded crafting stations through wireless portable remotes from anywhere in the world.

---

[← Back to Wiki Hub](README.md) | [Main Documentation](../README.md)

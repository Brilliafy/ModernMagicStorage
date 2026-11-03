# Third-Party Mod Integrations

Modern Magic Storage features comprehensive, soft-dependency integrations with popular modern mods. If any integrated mod is absent from your modpack, Modern Magic Storage gracefully ignores it with zero crashes or errors.

---

## Table of Contents

- [Just Enough Items (JEI)](#just-enough-items-jei)
- [Farmer's Delight](#farmers-delight)
- [Apotheosis](#apotheosis)
- [Tetra](#tetra)
- [Curios API and Accessory Mods](#curios-api-and-accessory-mods)
- [Fair Experience](#fair-experience)
- [Quality Equipment](#quality-equipment)
- [Reskillable Reimagined](#reskillable-reimagined)
- [Custom and Generic Modded Stations](#custom-and-generic-modded-stations)

---

## Just Enough Items (JEI)

Modern Magic Storage includes a native JEI plugin (`MagicStorageJeiPlugin`) annotated with `@JeiPlugin`.

### Recipe Transfer Handlers (`+` Icon)
When browsing recipes in JEI while inside the Crafting Access terminal, clicking the **`+`** icon transfers required ingredients into the crafting matrix. Transfer handlers are registered for the following recipe types:

1. **Crafting** (`RecipeTypes.CRAFTING`): 9-slot crafting matrix.
2. **Stonecutting** (`RecipeTypes.STONECUTTING`): Single input block transfer.
3. **Smithing** (`RecipeTypes.SMITHING`): Template, base item, and addition item transfer.
4. **Smelting** (`RecipeTypes.SMELTING`): Ore/input item transfer into the furnace input slot.
5. **Blasting** (`RecipeTypes.BLASTING`): Fast blast furnace recipe transfer.
6. **Smoking** (`RecipeTypes.SMOKING`): Food smoking recipe transfer.
7. **Cooking Pot** (`farmersdelight:cooking`): 6 meal ingredients plus serving container.
8. **Cutting Board** (`farmersdelight:cutting`): Input item plus cutting tool.

---

## Farmer's Delight

- **Mod ID**: `farmersdelight`
- **Class**: `FarmersDelightCompat`

### 1. Cooking Pot
- **Interaction**: Install a Cooking Pot into the Storage Heart by shift-right-clicking the Heart.
- **Recipe Matching**: Supports 1 to 6 shapeless meal ingredients.
- **Container Requirement**: If the meal recipe requires a serving container (for example, a wooden bowl or glass bottle), the recipe only resolves when the proper container is present in the container slot (`x=94, y=148`).
- **Shift-Crafting**: Full support for instant bulk cooking directly pulling ingredients from network storage.

### 2. Cutting Board
- **Interaction**: Install a Cutting Board into the Storage Heart.
- **Tool Durability**: Matches cutting board recipes using any valid tool (Knife, Axe, Shears). Deals 1 point of durability damage to the tool per craft.
- **Tool Retention**: The cutting tool is preserved in its slot and not consumed.
- **Secondary Drops**: Awards primary outputs plus all configured secondary and chance drops (such as Tree Bark from stripped logs or Straw from wheat).
- **Sound Effects**: Plays authentic shearing and wood stripping audio effects on craft.

### 3. Stove and Skillet
- Recognized by the Storage Heart as valid heating and cooking stations.

---

## Apotheosis

- **Mod ID**: `apotheosis`
- **Class**: `ApotheosisCompat`

### 1. Reforging Table
- Recognized when installed in the Storage Heart.
- Supports affix re-rolling on equipment using gem dust and material additions.

### 2. Salvaging Table
- Recognized when installed in the Storage Heart.
- Breaks down unwanted affix gear into scrap and crafting materials.

### 3. Gem Cutting Table
- Upgrades affix gems to higher rarity tiers.

---

## Tetra

- **Mod ID**: `tetra`
- **Class**: `TetraCompat`

### Modular Workbench
- Recognized when installed in the Storage Heart.
- Detects modular tool components and honing materials.
- Repairs modular tools and advances hone progress while strictly preserving modular schemas and socketed upgrades.

---

## Curios API and Accessory Mods

- **Mod IDs**: `curios`, `bountifulbaubles`
- Accessory reforging anvils, reforging tablets, and modification stations are recognized as valid Storage Heart stations and categorized under reforging capabilities.

---

## Fair Experience

- **Mod ID**: `fairexperience`
- **Class**: `FairExperienceCompat`
- Modern Magic Storage seamlessly integrates with Fair Experience to compute fair raw XP values for anvil operations and enchanting station interactions, replacing vanilla's exponential level curve with a balanced cost.

---

## Quality Equipment

- **Mod IDs**: `quality_equipment`, `qualitytools`
- **Class**: `QualityEquipmentCompat`
- Equipment quality reforging stations are automatically recognized as valid Heart stations and registered into the reforging station pool.

---

## Reskillable Reimagined

- **Mod IDs**: `reskillable_reimagined`, `reskillable`
- **Class**: `ReskillableCompat`
- Validates player skill and stat requirements before allowing crafting or withdrawal of restricted equipment. If a player lacks the necessary skill level, interaction is prevented and a notification is displayed.

---

## Custom and Generic Modded Stations

Modern Magic Storage includes dynamic reflection logic to support modded crafting stations:

1. **Automatic GUI Detection**:
   - Inspects placed block items for `MenuProvider` implementations or `EntityBlock` classes with custom menus.
   - Heuristically detects workstation keywords (such as `workbench`, `furnace`, `smelter`, `distiller`, `press`, `crusher`, `infuser`, `kitchen`, etc.).
2. **Station Panel Registration**:
   - Any accepted station appears on the right panel with its native 1:1 item icon.
3. **Remote Distance Bypass**:
   - When opening a modded station via shift-click, the Storage Heart supplies `player.blockPosition()` as the virtual interaction position.
   - This bypasses vanilla and modded distance checks (`player.distanceToSqr <= 64.0`), allowing players to use their favorite modded crafting stations through wireless portable remotes from anywhere in the world.

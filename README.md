[![Modrinth](https://img.shields.io/badge/Modrinth-3SFNKJXS-00AF5C?style=for-the-badge&logo=modrinth&logoColor=white)](https://modrinth.com/mod/magic-storage-network)
[![CurseForge](https://img.shields.io/badge/CurseForge-1629983-F44336?style=for-the-badge&logo=curseforge&logoColor=white)](https://www.curseforge.com/minecraft/mc-mods/magic-storage-network)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

# Modern Magic Storage

A modern, multi-loader, multi-version modular storage network mod for Minecraft inspired by Terraria's *Magic Storage*.

Centralizes chest rooms into an expandable, unified **Storage Heart** network with 8 modular storage unit tiers, wireless portable remotes, and connected **Crafting Access** terminals featuring dynamic station detection and instant recipe crafting.

---

## Supported Versions & Loaders

Modern Magic Storage is built on **Jared's MultiLoader-Template** architecture, **Stonecutter** (`dev.kikugie.stonecutter`), **MixinExtras**, and **Parchment Mappings**. Every release targets 6 distinct loader and Minecraft version platforms:

| Minecraft Version | Fabric | NeoForge | Forge | Output File |
| :---: | :---: | :---: | :---: | :--- |
| **26.3** | `✓` | `✓` | — | `magicstorage-2.0.0-fabric+26.3.jar` / `magicstorage-2.0.0-neoforge+26.3.jar` |
| **1.21.1** | `✓` | `✓` | — | `magicstorage-2.0.0-fabric+1.21.1.jar` / `magicstorage-2.0.0-neoforge+1.21.1.jar` |
| **1.20.1** | `✓` | — | `✓` | `magicstorage-2.0.0-fabric+1.20.1.jar` / `magicstorage-2.0.0-forge+1.20.1.jar` |

---

## Core Features

- **Storage Heart**: Central network brain. Emits Light Level 15 and holds 20 crafting station items.
- **8 Storage Unit Tiers**:
  1. **Basic**: 40 slots
  2. **Crimtane**: 80 slots
  3. **Demonite**: 80 slots
  4. **Hellstone**: 120 slots
  5. **Hallowed**: 160 slots
  6. **Blue Chlorophyte**: 220 slots
  7. **Luminite**: 300 slots
  8. **Terra**: 600 slots
- **In-Place Upgrades**: Shift-right-click an existing storage unit with an upgrade item to expand its capacity without dropping stored items.
- **Unified Terminals**:
  - **Storage Access**: Search, sort, deposit, and extract network items.
  - **Crafting Access**: 3x3 crafting matrix connected directly to network inventory with instant shift-click crafting.
- **Wireless Portable Remotes**:
  - **Basic Remote**: 200 block radius.
  - **Advanced Remote**: Unlimited range in the same dimension.
  - **Ultimate Remote**: Unlimited range across all dimensions.
- **New Materials & Ores**:
  - **Hellstone**: Spawns in the Nether below Y 31.
  - **Luminite Ore**: Rare end-game ore.
  - **Shadow Diamond** & **Radiant Jewel**: Rare components used for high-tier storage upgrades.

---

## Modern Mod Integrations

All third-party integrations are soft/optional dependencies. If a mod is not present, Magic Storage runs normally without crashing:

| Mod | Modern Role & Compatibility |
| :--- | :--- |
| **[Curios API](https://github.com/TheIllusiveC4/Curios)** | Modern replacement for Baubles. Recognizes accessory reforging anvils as valid Heart stations. |
| **[Apotheosis](https://github.com/Shadows-of-Fire/Apotheosis)** | Modern replacement for Disenchanters. Supports **Tome of Scrapping** and **Tome of Extraction** in the station slots. |
| **[JEI (Just Enough Items)](https://github.com/mezz/JustEnoughItems)** | Dynamic `+` recipe transfer into Crafting Access, automatically pulling ingredients from network storage and inventory. |
| **[Quality Equipment](https://www.curseforge.com/minecraft/mc-mods/quality-equipment)** | Supports reforging station detection. |
| **[Reskillable Reimagined](https://www.curseforge.com/minecraft/mc-mods/reskillable-reimagined)** | Validates player skill requirements before allowing access to restricted gear. |

---

## Local Testing Profile (`debugBuild`)

To test new builds locally **without publishing anything to the internet**:

### 1. Build and Collect Testing Jars
Run the following Gradle command in the project root:
```bash
./gradlew collectDebugJars
```
*(Or run `./gradlew debugBuild` to execute a full sequential recompile of all target versions first).*

### 2. Grab Your Jars
The task compiles and copies all 6 playable mod jars directly into the [`debug-jars/`](debug-jars) folder (sources and javadocs are automatically filtered out, and `debug-jars/` is git-ignored):

```
debug-jars/
├── magicstorage-2.0.0-fabric+1.20.1-SNAPSHOT.jar
├── magicstorage-2.0.0-forge+1.20.1-SNAPSHOT.jar
├── magicstorage-2.0.0-fabric+1.21.1-SNAPSHOT.jar
├── magicstorage-2.0.0-neoforge+1.21.1-SNAPSHOT.jar
├── magicstorage-2.0.0-fabric+26.3-SNAPSHOT.jar
└── magicstorage-2.0.0-neoforge+26.3-SNAPSHOT.jar
```

Copy the `.jar` for your target version and loader directly into your `.minecraft/mods` directory to test.

> [!TIP]
> **Sequential Compilation & Low Memory:** Gradle is preconfigured with `org.gradle.parallel=false` and `-Xmx1536M`. Version targets are compiled strictly sequentially so that memory and swap usage remain minimal during builds.

---

## Manual Release & Publishing Automation

Publishing to GitHub Releases, Modrinth, and CurseForge is **100% manual**. No commit, local build, or tag push will ever publish to the world automatically.

### Automated Publishing Workflow (`.github/workflows/release.yml`)

When you are ready to publish an official release:

1. Push your changes to GitHub:
   ```bash
   git push origin main
   ```
2. Open your repository on GitHub.
3. Click the **Actions** tab $\rightarrow$ select the **Release** workflow on the left sidebar.
4. Click **Run workflow** on the right side:
   - **Branch**: `main`
   - **Publish to Modrinth & CurseForge**: Checked (`true`)
5. Click the green **Run workflow** button.

### What Happens During Release:
- **`git-cliff`** parses conventional commits (`feat:`, `fix:`, `perf:`, etc.) since the previous release and formats [`CHANGELOG.md`](CHANGELOG.md).
- **`me.modmuss50.mod-publish-plugin`** builds all 6 jars, attaches the generated changelog, and uploads:
  - To **GitHub Releases** under the version tag (e.g. `v2.0.0`).
  - To **Modrinth** under Project ID `3SFNKJXS`.
  - To **CurseForge** under Project ID `1629983`.

---

## Configuration & Credentials

The publishing pipeline uses the following settings configured in GitHub repository settings:

### Secrets (`Settings -> Secrets and variables -> Actions -> Secrets`)
- `PUB_CURSEFORGE_TOKEN`: Your CurseForge API token.
- `PUB_MODRINTH_TOKEN`: Your Modrinth API token.
- `GITHUB_TOKEN`: Provided automatically by GitHub Actions.

### Variables (`Settings -> Secrets and variables -> Actions -> Variables`)
- `PUB_CURSEFORGE_PROJECT_ID`: Default `1629983` (built-in fallback).
- `PUB_MODRINTH_PROJECT_ID`: Default `3SFNKJXS` (built-in fallback).
- `PUB_GITHUB_ENABLE`: `true`

---

## Credits & License

- **[Magic Storage (Terraria)](https://github.com/blushiemagic/MagicStorage)** by blushiemagic: Original game concept and inspiration.
- **[Jared's MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template)**: Multi-platform project scaffolding.
- **[Stonecutter](https://github.com/kikugie/stonecutter)** by kikugie: Active version controller and preprocessor.
- **License**: Modern Magic Storage is licensed under the [MIT License](LICENSE).

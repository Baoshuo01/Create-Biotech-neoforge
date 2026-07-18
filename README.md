# Create: Biotech — NeoForge 1.21.1 Port

[English](README.md) | [简体中文](README.zh-CN.md) | [Player Guide](docs/INTRODUCTION.md) | [玩家指南](docs/INTRODUCTION.zh-CN.md)

A community-maintained port of [Create: Biotech](https://github.com/Nobodiiiii/Create-Biotech) for **Minecraft 1.21.1**, **NeoForge**, and **Create 6.0.10 or newer**.

Create: Biotech connects mobs, biological materials, fluids, logistics, processing, and contraptions with the systems provided by [Create](https://www.curseforge.com/minecraft/mc-mods/create).

> [!IMPORTANT]
> This repository is a port of the original project, not a claim of ownership over the original mod or its assets. The original project was created by **Nobodiiiii**. The NeoForge 1.21.1 port is maintained by **Baoshuo01**.

## Port status

| Field | Value |
| --- | --- |
| Minecraft | `1.21.1` |
| Mod loader | NeoForge `21.1.219` or newer |
| Java | `21` |
| Create | `6.0.10` or newer |
| Mod id | `create_biotech` |
| Port version | `1.1.0` |
| Required runtime libraries | Flywheel, Vanillin, Ponder |
| Optional integrations | JEI, Jade |
| Mappings | Parchment `2024.11.17` for Minecraft 1.21.1 |

Dependency versions and accepted ranges are defined in [gradle.properties](gradle.properties). The minimum supported Create version is intentionally fixed at **6.0.10**.

## Scope of the port

This repository migrates the remaining Create: Biotech systems to the Minecraft 1.21.1 NeoForge APIs, including registration, networking, capabilities, rendering, recipes, tags, data formats, JEI, Jade, and Ponder integration.

The port is intended to preserve the behavior of the retained systems while removing obsolete compatibility code, outdated loader-specific code, unused resources, and features that are no longer part of this edition.

## Intentionally removed features

The following former features have been completely removed from this port:

### Butter Cat module

The complete Butter Cat integration has been removed, including:

- Butter Cat Engine and related blocks;
- Butter Cat items, foods, fluids, effects, and rotations;
- client renderers and Flywheel visuals belonging to that module;
- Butter Cat Ponder scenes and registrations;
- related recipes, tags, localization, models, textures, data, and configuration.

No Butter Cat or CreateButterCat-derived code or assets are intended to be distributed by this repository.

### Ghast-driving module

The ghast-driving gameplay integration has also been removed, including:

- the ghast helmet and ghast control behavior;
- the hot-air-balloon assembly station and related assembly content;
- driving interactions, registrations, recipes, localization, models, textures, and data belonging to that feature.

This removal concerns the former **rideable/controllable ghast gameplay system**. Ordinary vanilla Minecraft content, such as a ghast tear used as a recipe ingredient, is not part of that removed module.

> [!WARNING]
> Worlds created with an older version may contain blocks, items, entities, fluids, or configuration entries from the removed modules. Back up the world before upgrading. This port does not provide a compatibility shim or automatic data migration for removed registry entries.

Create Phantom-derived logistics features that remain under `com.yision.phantom` are separate from the deleted ghast-driving module.

## Retained major systems

- Slime, magma, and power belts, including Create funnel and tunnel interaction.
- Entity capture and biological processing in basins and custom machines.
- Buffer pads and contraption collision behavior.
- Spider Assembly Table.
- Squid Printer.
- Evoker Enchanting Chamber.
- Creeper Blast Chamber.
- Bio Packager.
- Experience fluid, experience pump, buds, clusters, and tank rendering.
- Shulker Teleporter.
- Create Phantom-derived logistics features.
- Universal Joint, Bone Ratchet, Slime Clutch, and Fixed Carrot Fishing Rod.
- Explosion-Proof Item Vault and Cardboard Boxes.
- JEI recipe displays, Jade tooltips, and Ponder scenes for retained content.

## Installation

1. Install Minecraft 1.21.1 and a compatible NeoForge 21.1.x version.
2. Install Create 6.0.10 or newer and its required runtime libraries.
3. Place the Create: Biotech JAR in the instance's `mods` directory.
4. Optionally install compatible JEI and Jade versions.
5. Remove older Create: Biotech builds before starting the game.

Always back up existing worlds before changing mod versions.

## Building from source

Requirements:

- JDK 21;
- a working Gradle environment or the included Gradle wrapper;
- dependencies available through the repositories configured in [build.gradle](build.gradle).

Windows PowerShell or Command Prompt:

```powershell
.\gradlew.bat build
.\gradlew.bat runClient
.\gradlew.bat runServer
```

Linux or macOS:

```bash
./gradlew build
./gradlew runClient
./gradlew runServer
```

Build outputs are written to `build/libs/`.

## Repository layout

```text
src/main/java/com/nobodiiiii/createbiotech/
  client/          Client registration, rendering, particles, and GUI hooks
  compat/          JEI and Jade integration
  content/         Feature implementations
  event/           Event handlers
  foundation/      Shared utilities
  infrastructure/ Shared registration and Ponder wiring
  mixin/           Create and vanilla mixins
  network/         Custom payload registration and handlers
  ponder/          Ponder scenes and generated support
  registry/        Blocks, items, fluids, entities, menus, recipes, and configs

src/main/java/com/yision/phantom/
  Create Phantom-derived logistics code retained under its upstream license

src/main/resources/
  assets/create_biotech/  Models, textures, localization, particles, and Ponder data
  data/                   Recipes, tags, loot tables, and advancements
  META-INF/neoforge.mods.toml
```

## Compatibility and issue reports

When reporting a crash or rendering problem, include:

- the complete crash report;
- `latest.log` when available;
- the exact NeoForge, Create, and Create: Biotech versions;
- the complete mod list;
- reproduction steps and whether the issue also occurs in a clean instance containing only required dependencies.

Do not report issues from this port to the original project unless the problem is also reproducible in the original upstream version.

## License and attribution

This repository does not claim ownership of upstream Create: Biotech material or any other third-party material.

- All upstream code, documentation, assets, names, and other material remain the property of their respective copyright holders and remain subject to their upstream licenses and permissions.
- Only original, copyrightable code changes authored by Baoshuo01 specifically for this NeoForge 1.21.1 port are claimed by Baoshuo01 and offered under the MIT License, and only to the extent that Baoshuo01 owns those changes.
- Moving, renaming, converting, reformatting, or mechanically adapting upstream material does not create a new ownership claim over that material.
- Create Phantom-derived and other third-party portions retain the notices reproduced in [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

See [LICENSE.md](LICENSE.md), [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md), and [docs/PERMISSIONS.md](docs/PERMISSIONS.md) before redistributing source code, binaries, or assets.
## Credits

- **Nobodiiiii** — original Create: Biotech author and original project assets.
- **Baoshuo01** — Minecraft 1.21.1 NeoForge port and maintenance.
- **Yison** — Create Phantom material retained under the BSD-3-Clause License.
- **Tim Heidler** — upstream Create Mobile Packages portions retained under the MIT License.
- **Create team and contributors** — Create and its public APIs.
- Translators, testers, dependency authors, and community contributors.

This repository is not affiliated with or endorsed by Microsoft, Mojang Studios, the Create team, or the upstream authors unless they explicitly state otherwise.
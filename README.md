# Mekanism Upgrade Caps

Configurable Mekanism speed and energy upgrade caps for Forge and NeoForge modpacks.

This add-on lets Mekanism machines accept more than the default 8 speed and energy upgrades. Expanded upgrade counts affect machine processing speed, energy usage, energy capacity, saved upgrade counts, and Mekanism's upgrade tooltip display.

## Supported Versions

| Minecraft | Loader | Mekanism target | Java |
| --- | --- | --- | --- |
| 1.21.1 | NeoForge 21.1.203+ | 10.7.15+ | 21 |
| 1.20.1 | Forge 47+ | 10.4.15+ | 17 |
| 1.19.2 | Forge 43+ | 10.3.9+ | 17 |
| 1.18.2 | Forge 40+ | 10.2.5+ | 17 |

## Release Labels

Use these labels when publishing files:

| File | Modloader tag | Release tag |
| --- | --- | --- |
| `mekanism-upgrade-caps-neoforge-1.21.1-1.0.0.jar` | NeoForge | Release |
| `mekanism-upgrade-caps-forge-1.20.1-1.0.6.jar` | Forge | Beta |
| `mekanism-upgrade-caps-forge-1.19.2-1.0.6.jar` | Forge | Beta |
| `mekanism-upgrade-caps-forge-1.18.2-1.0.6.jar` | Forge | Beta |

The NeoForge 1.21.1 build has been tested in-game. The Forge builds compile against their matching Forge, Minecraft, and Mekanism branches and should be treated as beta until tested in those packs.

## Commands

Commands require permission level 2.

```text
/mekupgradecaps get
/mekupgradecaps set speed 64
/mekupgradecaps set energy 64
```

## Config

The mod creates this file on first run:

```text
config/mekanism-upgrade-caps.properties
```

Default values:

```properties
speedMax=16
energyMax=16
```

Command changes are saved immediately. If you edit the file manually, restarting the game or server is recommended.

## Scaling

Speed and energy effects use a linear multiplier:

```text
installed upgrades * 2
```

For example, 64 installed speed upgrades gives a 128x speed effect.

Energy use scales with speed and energy upgrades:

```text
speedMultiplier * speedMultiplier / energyMultiplier
```

Energy capacity scales with the energy upgrade multiplier.

## Building

Run the public build script from the repository root:

```powershell
.\build-all.ps1
```

The script writes jars to `release/` and temporary compile output to `build/`. It downloads Mekanism compile jars into `deps/` when needed. Those folders are intentionally ignored by git.

Optional environment variables:

```powershell
$env:MC_INSTANCE_DIR = "path\to\your\1.21.1 NeoForge instance"
$env:CURSEFORGE_MINECRAFT_LIBRARIES = "path\to\minecraft\Install\libraries"
$env:JAVAC_EXE = "path\to\javac.exe"
$env:JAR_EXE = "path\to\jar.exe"
```

The 1.21.1 build currently uses the local NeoForge/Minecraft/Mekanism jars from the configured instance. The legacy Forge builds use the local CurseForge library cache plus each target pack's Mekanism jar so command and mixin signatures match the tested runtime.

## Distribution

Upload the generated jar that matches the target Minecraft and loader version. This mod is MIT licensed.

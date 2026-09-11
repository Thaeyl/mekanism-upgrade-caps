# Mekanism Upgrade Caps

Configurable Mekanism speed and energy upgrade caps for Minecraft 1.21.1 on NeoForge.

This add-on lets machines accept more than the default 8 speed and energy upgrades. The expanded upgrade counts also affect machine processing speed, energy usage, energy capacity, and the upgrade tooltip display.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.203 or newer
- Mekanism 10.7.15 or newer

## Commands

Requires permission level 2.

```text
/mekupgradecaps get
/mekupgradecaps set speed 64
/mekupgradecaps set energy 64
```

## Config

The mod creates:

```text
config/mekanism-upgrade-caps.properties
```

Default values:

```properties
speedMax=16
energyMax=16
```

Restarting the game is recommended after changing the file manually. Command changes are saved immediately.

## Scaling

Speed and energy effects use a linear display/behavior multiplier:

```text
installed upgrades * 2
```

For example, 64 installed speed upgrades gives a 128x speed effect.

## Building

The included `build-release.ps1` script expects a local CurseForge/Minecraft install with NeoForge, Minecraft 1.21.1, Mekanism, Brigadier, and Mixin jars available.

Optional environment variables:

```powershell
$env:MC_INSTANCE_DIR = "path\to\your\minecraft\instance"
$env:CURSEFORGE_MINECRAFT_LIBRARIES = "path\to\minecraft\Install\libraries"
.\build-release.ps1
```

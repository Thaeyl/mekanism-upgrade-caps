# Mekanism Upgrade Caps

Mekanism Upgrade Caps is a small Forge/NeoForge add-on that raises Mekanism's speed and energy upgrade limits beyond the default 8 upgrades.

It is intended for large modpacks and late-game automation setups where Mekanism machines are useful, but the vanilla upgrade ceiling becomes too low.

## Features

- Configurable speed and energy upgrade caps.
- Linear speed and energy effect scaling.
- Correct upgrade effect tooltip display.
- Command-based config changes.
- Preserves saved upgrade counts above Mekanism's vanilla cap when this mod is updated.
- Supports Forge 1.18.2, Forge 1.19.2, Forge 1.20.1, and NeoForge 1.21.1.

## Supported Versions

| Minecraft | Loader | Tested pack | Mekanism target | Java |
| --- | --- | --- | --- | --- |
| 1.21.1 | NeoForge 21.1.203+ | ATM10 | 10.7.15+ | 21 |
| 1.20.1 | Forge 47+ | ATM9 | 10.4.15+ | 17 |
| 1.19.2 | Forge 43+ | ATM8 | 10.3.9+ | 17 |
| 1.18.2 | Forge 40+ | ATM7 | 10.2.5+ | 17 |

## Downloads

Use the jar that matches your Minecraft version and mod loader:

| File | Loader | Minecraft |
| --- | --- | --- |
| `MekanismUpgradeCaps-NeoForge-1.21.1-1.0.0.jar` | NeoForge | 1.21.1 |
| `MekanismUpgradeCaps-Forge-1.20.1-1.0.0.jar` | Forge | 1.20.1 |
| `MekanismUpgradeCaps-Forge-1.19.2-1.0.0.jar` | Forge | 1.19.2 |
| `MekanismUpgradeCaps-Forge-1.18.2-1.0.0.jar` | Forge | 1.18.2 |

## Configuration

The mod creates this file on first run:

```text
config/mekanism-upgrade-caps.properties
```

Defaults:

```properties
speedMax=16
energyMax=16
```

Manual config edits are loaded on restart. Command changes are saved immediately.

### Upgrade Count Warning

If you lower a cap below the number of upgrades already installed in a machine, those extra upgrades may remain in that machine until the world or client is reloaded. After a reload, Mekanism may enforce the new lower cap. Remove extra upgrades before lowering caps if you want to avoid losing or trimming installed upgrades.

## Commands

Commands require permission level 2.

```text
/mekupgradecaps get
/mekupgradecaps set speed <value>
/mekupgradecaps set energy <value>
```

Examples:

```text
/mekupgradecaps get
/mekupgradecaps set speed 64
/mekupgradecaps set energy 64
```

## Scaling

Upgrade effects scale linearly:

```text
installed upgrades * 2
```

Examples:

| Installed upgrades | Effect |
| ---: | ---: |
| 8 | 16x |
| 16 | 32x |
| 32 | 64x |
| 64 | 128x |

Energy usage scales based on both speed and energy upgrades:

```text
speedMultiplier * speedMultiplier / energyMultiplier
```

Energy capacity scales with the energy upgrade multiplier.

## Building

Run the build script from the repository root:

```powershell
.\build-all.ps1
```

The script writes jars to `release/` and temporary compile output to `build/`.

Optional environment variables:

```powershell
$env:MC_INSTANCE_DIR = "path\to\your\1.21.1 NeoForge instance"
$env:CURSEFORGE_MINECRAFT_LIBRARIES = "path\to\minecraft\Install\libraries"
$env:MEKANISM_1_20_1_JAR = "path\to\Mekanism-1.20.1 jar"
$env:MEKANISM_1_19_2_JAR = "path\to\Mekanism-1.19.2 jar"
$env:MEKANISM_1_18_2_JAR = "path\to\Mekanism-1.18.2 jar"
$env:JAVAC_EXE = "path\to\javac.exe"
$env:JAR_EXE = "path\to\jar.exe"
```

The NeoForge build uses the configured 1.21.1 instance. The Forge builds compile against the configured Minecraft library cache plus each target version's Mekanism jar so command and mixin signatures match the tested runtimes.

## License

MIT

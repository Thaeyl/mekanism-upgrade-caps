# Mekanism Upgrade Caps

Mekanism Upgrade Caps lets Mekanism machines accept more than the default 8 Speed and Energy Upgrades.

It is built for large modpacks and late-game factories where the normal Mekanism upgrade limit starts to feel too restrictive. Raise the caps, install more upgrades, and keep scaling your machines further into endgame automation.

## What It Does

- Raises the maximum Speed Upgrade count.
- Raises the maximum Energy Upgrade count.
- Updates Mekanism's upgrade UI so the displayed effect matches the new scaling.
- Preserves installed upgrade counts above the vanilla Mekanism cap across mod updates.
- Includes in-game commands for checking and changing caps.

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

Energy usage scales with both Speed and Energy Upgrades:

```text
speedMultiplier * speedMultiplier / energyMultiplier
```

## Configuration

The config file is created here:

```text
config/mekanism-upgrade-caps.properties
```

Defaults:

```properties
speedMax=16
energyMax=16
```

Commands:

```text
/mekupgradecaps get
/mekupgradecaps set speed <value>
/mekupgradecaps set energy <value>
```

Commands require permission level 2.

## Important Warning

If you lower a cap below the number of upgrades already installed in a machine, those extra upgrades may remain in that machine until the world or client is reloaded. After a reload, Mekanism may enforce the new lower cap. Remove extra upgrades before lowering caps if you want to avoid losing or trimming installed upgrades.

## Supported Versions

- Minecraft 1.21.1, NeoForge, Mekanism 10.7.15+
- Minecraft 1.20.1, Forge, Mekanism 10.4.15+
- Minecraft 1.19.2, Forge, Mekanism 10.3.9+
- Minecraft 1.18.2, Forge, Mekanism 10.2.5+

Tested in ATM10, ATM9, ATM8, and ATM7.

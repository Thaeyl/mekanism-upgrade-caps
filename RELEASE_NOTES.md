# Mekanism Upgrade Caps 1.0.0

Initial full release.

## Highlights

- Configurable Mekanism Speed and Energy Upgrade caps.
- Linear upgrade scaling: installed upgrades times 2.
- Correct Mekanism upgrade tooltip display.
- Commands for reading and changing caps.
- Preserves saved upgrade counts above Mekanism's vanilla cap across mod updates.
- Builds for NeoForge 1.21.1 and Forge 1.20.1, 1.19.2, and 1.18.2.

## Files To Upload

| File name | CurseForge release type | Environment | Mod loader | Minecraft version |
| --- | --- | --- | --- | --- |
| `MekanismUpgradeCaps-NeoForge-1.21.1-1.0.0.jar` | Release | Client and Server | NeoForge | 1.21.1 |
| `MekanismUpgradeCaps-Forge-1.20.1-1.0.0.jar` | Release | Client and Server | Forge | 1.20.1 |
| `MekanismUpgradeCaps-Forge-1.19.2-1.0.0.jar` | Release | Client and Server | Forge | 1.19.2 |
| `MekanismUpgradeCaps-Forge-1.18.2-1.0.0.jar` | Release | Client and Server | Forge | 1.18.2 |

## Per-File Changelogs

### MekanismUpgradeCaps-NeoForge-1.21.1-1.0.0.jar

- Initial NeoForge release for Minecraft 1.21.1.
- Tested with Mekanism 10.7.15+ in ATM10.
- Adds configurable Speed and Energy Upgrade caps.
- Adds linear speed/energy scaling and corrected tooltip display.
- Adds `/mekupgradecaps` commands.

### MekanismUpgradeCaps-Forge-1.20.1-1.0.0.jar

- Initial Forge release for Minecraft 1.20.1.
- Tested with Mekanism 10.4.15 in ATM9.
- Adds configurable Speed and Energy Upgrade caps.
- Adds linear speed/energy scaling and corrected tooltip display.
- Adds `/mekupgradecaps` commands using Forge/Minecraft 1.20.1-compatible command feedback handling.
- Preserves saved upgrade counts above Mekanism's vanilla cap across mod updates.

### MekanismUpgradeCaps-Forge-1.19.2-1.0.0.jar

- Initial Forge release for Minecraft 1.19.2.
- Tested with Mekanism 10.3.9 in ATM8.
- Adds configurable Speed and Energy Upgrade caps.
- Adds linear speed/energy scaling and corrected tooltip display.
- Adds `/mekupgradecaps` commands using Forge/Minecraft 1.19.2-compatible command feedback handling.
- Preserves saved upgrade counts above Mekanism's vanilla cap across mod updates.

### MekanismUpgradeCaps-Forge-1.18.2-1.0.0.jar

- Initial Forge release for Minecraft 1.18.2.
- Tested with Mekanism 10.2.5 in ATM7.
- Adds configurable Speed and Energy Upgrade caps.
- Adds linear speed/energy scaling and corrected tooltip display.
- Adds `/mekupgradecaps` commands using Forge/Minecraft 1.18.2-compatible command feedback handling.
- Preserves saved upgrade counts above Mekanism's vanilla cap across mod updates.
- Known pack note: ATM7 may report exit code `-1073740940` on client close after a normal save/shutdown. Latest inspected logs did not implicate Mekanism Upgrade Caps.

## Shared Warning

If a cap is lowered below the number of upgrades already installed in a machine, the extra upgrades may remain visible until the world or client is reloaded. After reload, Mekanism may enforce the new lower cap. Remove extra upgrades before lowering caps if you want to avoid losing or trimming installed upgrades.

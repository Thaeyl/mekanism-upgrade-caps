$ErrorActionPreference = 'Stop'

$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
$DepsDir = Join-Path $Root 'deps'
$BuildDir = Join-Path $Root 'build'
$ReleaseDir = Join-Path $Root 'release'
$CurseForgeRoot = Join-Path $env:USERPROFILE 'curseforge\minecraft'
$Libraries = if ($env:CURSEFORGE_MINECRAFT_LIBRARIES) {
    $env:CURSEFORGE_MINECRAFT_LIBRARIES
} else {
    Join-Path $CurseForgeRoot 'Install\libraries'
}
$Instance = if ($env:MC_INSTANCE_DIR) {
    $env:MC_INSTANCE_DIR
} else {
    Join-Path $CurseForgeRoot 'Instances\All the Mods 10 - ATM10'
}
$Javac = if ($env:JAVAC_EXE) {
    $env:JAVAC_EXE
} else {
    'C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe'
}
$JarTool = if ($env:JAR_EXE) {
    $env:JAR_EXE
} else {
    'C:\Program Files\Java\jdk-22\bin\jar.exe'
}

New-Item -ItemType Directory -Force -Path $DepsDir, $BuildDir, $ReleaseDir | Out-Null

function Invoke-Checked {
    param([string[]] $Command)
    & $Command[0] @($Command[1..($Command.Length - 1)])
    if ($LASTEXITCODE -ne 0) {
        throw "Command failed: $($Command -join ' ')"
    }
}

function Write-Utf8NoBom {
    param([string] $Path, [string] $Text)
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $Path) | Out-Null
    $encoding = [System.Text.UTF8Encoding]::new($false)
    [System.IO.File]::WriteAllText($Path, $Text, $encoding)
}

function Copy-JavaSource {
    param([string] $Source, [string] $Destination)
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $Destination) | Out-Null
    Copy-Item -LiteralPath $Source -Destination $Destination -Force
}

function Resolve-FirstFile {
    param([string] $Path, [string] $Filter)
    $file = Get-ChildItem -Recurse $Path -Filter $Filter -File | Sort-Object FullName -Descending | Select-Object -First 1
    if (!$file) {
        throw "Could not find $Filter under $Path"
    }
    $file.FullName
}

function Download-IfMissing {
    param([string] $Url, [string] $Output)
    if (!(Test-Path -LiteralPath $Output)) {
        Write-Host "Downloading $Url"
        Invoke-WebRequest -Uri $Url -OutFile $Output
    }
}

function Expand-Template {
    param([string] $Text, [hashtable] $Values)
    foreach ($key in $Values.Keys) {
        $Text = $Text.Replace('${' + $key + '}', [string] $Values[$key])
    }
    $Text
}

function Add-JarContent {
    param([string] $JarPath, [string] $ContentDir)
    Push-Location $ContentDir
    try {
        Invoke-Checked @($JarTool, 'uf', $JarPath, '.')
    } finally {
        Pop-Location
    }
}

function New-ForgeStubs {
    param([string] $StubDir)
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraftforge\fml\common\Mod.java') 'package net.minecraftforge.fml.common; import java.lang.annotation.*; @Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) public @interface Mod { String value(); }'
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraftforge\common\MinecraftForge.java') 'package net.minecraftforge.common; public class MinecraftForge { public static final EventBus EVENT_BUS = new EventBus(); public static class EventBus { public <T> void addListener(java.util.function.Consumer<T> listener) {} } }'
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraftforge\event\RegisterCommandsEvent.java') 'package net.minecraftforge.event; import com.mojang.brigadier.CommandDispatcher; import net.minecraft.commands.CommandSourceStack; public class RegisterCommandsEvent { public CommandDispatcher<CommandSourceStack> getDispatcher(){ return null; } }'
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraft\commands\CommandSourceStack.java') 'package net.minecraft.commands; import net.minecraft.network.chat.Component; public class CommandSourceStack { public boolean hasPermission(int level){ return false; } public void sendFailure(Component component) {} }'
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraft\commands\Commands.java') 'package net.minecraft.commands; import com.mojang.brigadier.arguments.ArgumentType; import com.mojang.brigadier.builder.LiteralArgumentBuilder; import com.mojang.brigadier.builder.RequiredArgumentBuilder; public class Commands { public static LiteralArgumentBuilder<CommandSourceStack> literal(String name){ return LiteralArgumentBuilder.literal(name); } public static <T> RequiredArgumentBuilder<CommandSourceStack,T> argument(String name, ArgumentType<T> type){ return RequiredArgumentBuilder.argument(name, type); } }'
    Write-Utf8NoBom (Join-Path $StubDir 'net\minecraft\network\chat\Component.java') 'package net.minecraft.network.chat; public interface Component { static Component literal(String value){ return null; } }'
}

function Build-NeoForge1211 {
    $buildClasses = Join-Path $BuildDir 'classes-1.21.1-neoforge'
    $outputJar = Join-Path $ReleaseDir 'mekanism-upgrade-caps-neoforge-1.21.1-1.0.0.jar'
    Remove-Item -LiteralPath $buildClasses -Recurse -Force -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Force -Path $buildClasses | Out-Null

    $mixin = Resolve-FirstFile (Join-Path $Libraries 'net\fabricmc\sponge-mixin') 'sponge-mixin-*.jar'
    $classpath = @(
        (Join-Path $Libraries 'net\neoforged\fancymodloader\loader\4.0.42\loader-4.0.42.jar'),
        (Join-Path $Libraries 'net\neoforged\neoforge\21.1.203\neoforge-21.1.203-universal.jar'),
        (Join-Path $Libraries 'net\neoforged\bus\8.0.5\bus-8.0.5.jar'),
        (Join-Path $Libraries 'net\neoforged\mergetool\2.0.0\mergetool-2.0.0-api.jar'),
        (Join-Path $Libraries 'net\neoforged\neoforge\21.1.203\neoforge-21.1.203-client.jar'),
        (Join-Path $Libraries 'net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-extra.jar'),
        (Join-Path $Libraries 'net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-slim.jar'),
        (Join-Path $Libraries 'net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-srg.jar'),
        (Join-Path $Libraries 'com\mojang\brigadier\1.3.10\brigadier-1.3.10.jar'),
        $mixin,
        (Join-Path $Instance 'mods\Mekanism-1.21.1-10.7.15.81.jar')
    ) -join ';'

    Invoke-Checked (@($Javac, '--release', '21', '-proc:none', '-cp', $classpath, '-d', $buildClasses) +
        @(Get-ChildItem -Recurse (Join-Path $Root 'src\main\java') -Filter '*.java' | ForEach-Object FullName))

    Remove-Item -LiteralPath $outputJar -Force -ErrorAction SilentlyContinue
    Push-Location (Join-Path $Root 'src\main\resources')
    try {
        Invoke-Checked @($JarTool, 'cfm', $outputJar, 'META-INF\MANIFEST.MF', '.')
    } finally {
        Pop-Location
    }
    Add-JarContent $outputJar $buildClasses
    Write-Host "Built $outputJar"
}

function Build-LegacyForge {
    param(
        [string] $MinecraftVersion,
        [string] $MekanismCoordinateVersion,
        [string] $ForgeLoaderVersion,
        [int] $PackFormat
    )

    $work = Join-Path $BuildDir "forge-$MinecraftVersion"
    $sourceDir = Join-Path $work 'src'
    $stubSourceDir = Join-Path $work 'stubs-src'
    $stubClassesDir = Join-Path $work 'stubs-classes'
    $classesDir = Join-Path $work 'classes'
    $resourcesDir = Join-Path $work 'resources'
    Remove-Item -LiteralPath $work -Recurse -Force -ErrorAction SilentlyContinue
    New-Item -ItemType Directory -Force -Path $sourceDir, $stubSourceDir, $stubClassesDir, $classesDir, $resourcesDir | Out-Null

    $mixin = Resolve-FirstFile (Join-Path $Libraries 'net\fabricmc\sponge-mixin') 'sponge-mixin-*.jar'
    $brigadier = Resolve-FirstFile (Join-Path $Libraries 'com\mojang\brigadier') 'brigadier-*.jar'
    $mekanismJar = Join-Path $DepsDir "Mekanism-$MekanismCoordinateVersion.jar"
    Download-IfMissing "https://modmaven.dev/mekanism/Mekanism/$MekanismCoordinateVersion/Mekanism-$MekanismCoordinateVersion.jar" $mekanismJar

    Copy-JavaSource (Join-Path $Root 'src\main\java\dev\mekupgradecaps\UpgradeCapConfig.java') (Join-Path $sourceDir 'dev\mekupgradecaps\UpgradeCapConfig.java')
    Copy-JavaSource (Join-Path $Root 'src\main\java\dev\mekupgradecaps\MekanismUpgradeMath.java') (Join-Path $sourceDir 'dev\mekupgradecaps\MekanismUpgradeMath.java')
    Copy-JavaSource (Join-Path $Root 'src\main\java\dev\mekupgradecaps\mixin\UpgradeMixin.java') (Join-Path $sourceDir 'dev\mekupgradecaps\mixin\UpgradeMixin.java')
    Copy-Item -LiteralPath (Join-Path $Root 'src\legacy\java\dev') -Destination $sourceDir -Recurse -Force
    Copy-Item -Path (Join-Path $Root 'src\legacy\resources\*') -Destination $resourcesDir -Recurse -Force

    $templateValues = @{
        modVersion = '1.0.0'
        minecraftVersion = $MinecraftVersion
        mekanismVersion = $MekanismCoordinateVersion.Substring($MekanismCoordinateVersion.IndexOf('-') + 1)
        loaderVersion = $ForgeLoaderVersion
        packFormat = $PackFormat
    }
    $modsToml = Expand-Template ([IO.File]::ReadAllText((Join-Path $Root 'src\legacy\resources\META-INF\mods.toml.template'))) $templateValues
    $packMcmeta = Expand-Template ([IO.File]::ReadAllText((Join-Path $Root 'src\legacy\resources\pack.mcmeta.template'))) $templateValues
    Write-Utf8NoBom (Join-Path $resourcesDir 'META-INF\mods.toml') $modsToml
    Write-Utf8NoBom (Join-Path $resourcesDir 'pack.mcmeta') $packMcmeta
    Remove-Item -LiteralPath (Join-Path $resourcesDir 'META-INF\mods.toml.template'), (Join-Path $resourcesDir 'pack.mcmeta.template') -Force

    New-ForgeStubs $stubSourceDir
    Invoke-Checked (@($Javac, '--release', '17', '-cp', $brigadier, '-d', $stubClassesDir) +
        @(Get-ChildItem -Recurse $stubSourceDir -Filter '*.java' | ForEach-Object FullName))
    Invoke-Checked (@($Javac, '--release', '17', '-proc:none', '-cp', (@($stubClassesDir, $brigadier, $mixin, $mekanismJar) -join ';'), '-d', $classesDir) +
        @(Get-ChildItem -Recurse $sourceDir -Filter '*.java' | ForEach-Object FullName))

    $outputJar = Join-Path $ReleaseDir "mekanism-upgrade-caps-forge-$MinecraftVersion-1.0.0.jar"
    Remove-Item -LiteralPath $outputJar -Force -ErrorAction SilentlyContinue
    Push-Location $resourcesDir
    try {
        Invoke-Checked @($JarTool, 'cfm', $outputJar, 'META-INF\MANIFEST.MF', '.')
    } finally {
        Pop-Location
    }
    Add-JarContent $outputJar $classesDir
    Write-Host "Built $outputJar"
}

Build-NeoForge1211
Build-LegacyForge '1.20.1' '1.20.1-10.4.16.80' '[47,)' 15
Build-LegacyForge '1.19.2' '1.19.2-10.3.9.13' '[43.2.8,)' 10
Build-LegacyForge '1.18.2' '1.18.2-10.2.5.465' '[40,)' 9

Get-ChildItem -LiteralPath $ReleaseDir -Filter '*.jar' | Sort-Object Name | Select-Object Name, Length, LastWriteTime

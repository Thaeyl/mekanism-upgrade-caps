$ErrorActionPreference = 'Stop'

$Root = Split-Path -Parent $MyInvocation.MyCommand.Path
$Instance = if ($env:MC_INSTANCE_DIR) { $env:MC_INSTANCE_DIR } else { Split-Path -Parent $Root }
$Libraries = if ($env:CURSEFORGE_MINECRAFT_LIBRARIES) {
    $env:CURSEFORGE_MINECRAFT_LIBRARIES
} else {
    Join-Path (Split-Path -Parent (Split-Path -Parent $Instance)) 'Install\libraries'
}
$JarName = 'mekanism-upgrade-caps-neoforge-1.21.1-1.0.0.jar'
$BuildClasses = Join-Path $Root 'build\classes'
$ReleaseDir = Join-Path $Root 'release'
$OutputJar = Join-Path $ReleaseDir $JarName

$mixin = Get-ChildItem -Recurse "$Libraries\net\fabricmc\sponge-mixin" -Filter 'sponge-mixin-0.16.5*.jar' | Select-Object -First 1 -ExpandProperty FullName
$classpath = @(
    "$Libraries\net\neoforged\fancymodloader\loader\4.0.42\loader-4.0.42.jar",
    "$Libraries\net\neoforged\neoforge\21.1.203\neoforge-21.1.203-universal.jar",
    "$Libraries\net\neoforged\bus\8.0.5\bus-8.0.5.jar",
    "$Libraries\net\neoforged\mergetool\2.0.0\mergetool-2.0.0-api.jar",
    "$Libraries\net\neoforged\neoforge\21.1.203\neoforge-21.1.203-client.jar",
    "$Libraries\net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-extra.jar",
    "$Libraries\net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-slim.jar",
    "$Libraries\net\minecraft\client\1.21.1-20240808.144430\client-1.21.1-20240808.144430-srg.jar",
    "$Libraries\com\mojang\brigadier\1.3.10\brigadier-1.3.10.jar",
    $mixin,
    (Join-Path $Instance 'mods\Mekanism-1.21.1-10.7.15.81.jar')
) -join ';'

Remove-Item -LiteralPath $BuildClasses -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force $BuildClasses, $ReleaseDir | Out-Null

& 'C:\Program Files\Common Files\Oracle\Java\javapath\javac.exe' --release 21 -proc:none -cp $classpath -d $BuildClasses (Get-ChildItem -Recurse "$Root\src\main\java" -Filter '*.java' | ForEach-Object FullName)

Remove-Item -LiteralPath $OutputJar -Force -ErrorAction SilentlyContinue
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [IO.Compression.ZipFile]::Open($OutputJar, [IO.Compression.ZipArchiveMode]::Create)
try {
    function Add-FileToJar($Base, $File) {
        $relative = [IO.Path]::GetRelativePath((Resolve-Path $Base), (Resolve-Path $File)).Replace('\', '/')
        [IO.Compression.ZipFileExtensions]::CreateEntryFromFile($script:zip, (Resolve-Path $File), $relative, [IO.Compression.CompressionLevel]::Optimal) | Out-Null
    }

    Get-ChildItem -Recurse "$Root\src\main\resources" -File | ForEach-Object { Add-FileToJar "$Root\src\main\resources" $_.FullName }
    Get-ChildItem -Recurse $BuildClasses -File | ForEach-Object { Add-FileToJar $BuildClasses $_.FullName }
} finally {
    $zip.Dispose()
}

Copy-Item -LiteralPath $OutputJar -Destination (Join-Path $Instance "mods\$JarName") -Force
Write-Host "Built $OutputJar"

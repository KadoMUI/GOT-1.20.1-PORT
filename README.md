# Conquest of Westeros

**Conquest of Westeros 1.0** is a Forge 1.20.1 continuation and modernization of the legacy Game of Thrones Minecraft mod codebase.

The 1.0 release restores and modernizes the core Westeros/Essos sandbox for modern Minecraft, including faction crafting, regional equipment, NPC populations, wildlife and mounts, travel and waypoint systems, economy and traders, quests and lore, conquest systems, world generation, structures, legendary weapons, Valyrian Steel progression, and functional utility blocks.

## Version

- **Release:** 1.0.0
- **Minecraft:** 1.20.1
- **Forge:** 47.4.22
- **Java:** 17
- **Mod ID:** `got`

## Building from source

JDK 17 is required.

### Windows

```text
gradlew.bat build
```

### Linux / macOS

```text
./gradlew build
```

The compiled JAR is written to `build/libs/`.

For IDE development, import the repository as a Gradle project. Forge run configurations can be generated with `genIntellijRuns` or `genEclipseRuns`.

## Repository layout

- `src/` — current mod source and resources.
- `gradle/` — Gradle wrapper.
- `docs/` — implementation audits, parity records, and retained development documentation.
- `docs/development-history/` — historical development-pass notes from the 1.20.1 port.
- `docs/legacy-reference/` — retained legacy disassembly/reference material.
- `migration/` — migration, auditing, and asset-generation utilities used during the port.
- `legacy_quests/` — retained quest migration/reference data.

## Contributing

Bug reports should include the Minecraft version, Forge version, mod version, reproduction steps, and the relevant latest log or crash report.

When contributing code, keep changes focused and avoid committing generated Gradle output, IDE metadata, runtime worlds, logs, or crash reports.

## License and attribution

This repository is a continuation/port of an existing Game of Thrones Minecraft mod codebase and contains work originating from multiple sources and development eras.

See `LICENSE.txt`, `CREDITS.txt`, and copyright/attribution notices retained throughout the source tree before redistributing or reusing project content.

Game of Thrones and related names, characters, settings, and trademarks are the property of their respective rights holders. This is an unofficial fan project and is not affiliated with or endorsed by HBO, Warner Bros. Discovery, George R. R. Martin, or their affiliates.

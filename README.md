# Project Thrones

Project Thrones is the Forge 1.20.1 continuation/port of the Game of Thrones Minecraft mod codebase.

## Development target

- Minecraft: 1.20.1
- Forge: 47.4.22
- Java: 17
- Mod ID: `got`

## Build from source

1. Install JDK 17.
2. Clone the repository.
3. Open the project as a Gradle project in IntelliJ IDEA or another Java IDE.
4. Run `./gradlew build` on Linux/macOS or `gradlew.bat build` on Windows.

Forge development run configurations can be generated with `genIntellijRuns` or `genEclipseRuns` if needed.

## Repository layout

- `src/` — mod source code and resources
- `docs/` — parity/audit documentation retained by the port
- `migration/` — migration, audit, and asset-generation utilities used during the port
- `gradle/` — Gradle wrapper files

## License and credits

See `LICENSE.txt` and `CREDITS.txt`, together with any copyright or attribution notices retained in the source tree.

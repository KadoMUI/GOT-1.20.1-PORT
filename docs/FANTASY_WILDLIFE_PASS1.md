# Fantasy Wildlife Pass 1

Restores Desert Scorpion, Jungle Scorpion, Red Scorpion, and the ASOIAF Manticore.

- Shared modern scorpion entity/model/renderer architecture.
- Species-specific venom; Red Scorpion is stronger and rarer.
- Manticore is implemented as the small venomous ASOIAF insect, using the legacy manticore texture—not a lion-bodied fantasy manticore.
- Natural Planetos biome-modifier spawning added.
- Wyverns intentionally excluded per project direction.
- Termites intentionally excluded.

Build note: the sandbox could not execute Gradle because Gradle 8.8 is not cached and outbound access to services.gradle.org is blocked. JSON validation passed; run `gradlew clean build` locally for definitive compilation.

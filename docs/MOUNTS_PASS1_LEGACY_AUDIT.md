# Mounts Pass 1 — legacy audit

Recovered directly from Game of Thrones 24.08.29(8).jar.

Legacy mount contract: GOTNPCMount + GOTMountFunctions.
Dedicated mount-capable animal classes present in the original include GOTEntityHorse, GOTEntityZebra, GOTEntityRhino, GOTEntityWoolyRhino, GOTEntityCamel, and GOTEntityBoar.

The existing hiring audit contains 51 deferred mounted hire variants. Of those, the mounts actually required by hiring are:
- GOTEntityHorse — normal faction cavalry, Dothraki, Yi Ti, etc.
- GOTEntityZebra — Jogos Nhai mounted units.
- GOTEntityWoolyRhino — North Hillman pledged mounted warrior.

Pass 1 establishes persistent modern mount entity types and the NPC-ownership/saddling contract. It does NOT yet activate the 51 mounted hiring choices because the current hiring UI/service resolves one definition per role and cannot distinguish mounted vs unmounted variants. That selection path must be fixed before enabling them or it will silently choose an arbitrary duplicate role definition.

Rendering currently uses the vanilla HorseRenderer as a functional placeholder for the new entity types. Legacy animal models/textures remain a presentation task for Mounts Pass 2.

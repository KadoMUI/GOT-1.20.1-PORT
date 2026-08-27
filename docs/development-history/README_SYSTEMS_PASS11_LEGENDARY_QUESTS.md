# Project Thrones — Systems Pass 11: Legendary Quests

This source pass reconstructs the final-build legacy legendary miniquest content and closes the two missing dependencies discovered during the first audit attempt.

## Legendary quest content

25 final-build legendary quest definitions are now bound to the modern quest engine:

- Howland Reed — kill 50 Crocodiles
- Balon Greyjoy — kill Euron Greyjoy
- Daenerys Targaryen — collect Dragon Eggs
- Varys — kill Daenerys Targaryen
- Oberyn Martell — kill Gerold Dayne
- Stannis Baratheon — kill Renly Baratheon
- Jon Snow — collect a Valyrian Sword; legacy hire-giver reward supported
- Renly Baratheon — kill Stannis Baratheon
- Bu Gai — kill Tugar Khan
- Tyrion Lannister — collect a randomized 1–10 Red Wine requirement
- Cersei Lannister — collect a randomized 1–10 Wildfire requirement
- Ramsay Bolton — Branding Iron quest
- Sandor Clegane — randomized 1–10 food/chicken requirement; legacy hire-giver reward supported
- Melisandre — Blood of the True Kings quest
- Doran Martell — randomized 1–10 Milk of the Poppy requirement
- Margaery Tyrell — kill Cersei Lannister
- Ellaria Sand — kill Tywin Lannister
- Arya Stark — Wooden Sword quest; legacy hire-giver reward supported
- Olenna Tyrell — kill Joffrey Baratheon
- Samwell Tarly — Obsidian Dagger quest
- Lysa Arryn — kill Tyrion Lannister
- Catelyn Stark — kill Theon Greyjoy
- Daven Lannister — vengeance quest
- Arianne Martell — kill Tommen Baratheon
- Mellario of Norvos — kill Doran Martell

Jaqen H'ghar remains handled by his dedicated existing sequence and was not replaced with generic JSON.

### Kitra

The stale `KITRA` factory constant is explicitly marked `removed_in_final_legacy_build`. The final 24.08.29 legacy build removed Kitra, her achievement and her camp waypoint; Project Thrones therefore does not resurrect her as missing content.

## Quest engine additions

- Legendary quest flag and `DO_MINIQUEST_LEGENDARY` award path.
- Persistent randomized objective counts (`count_min` / `count_max`) for legacy quests whose required item count was rolled at offer time.
- Random targets survive save/reload through per-instance `Targets` NBT.
- Legacy reward-factor calculation restored for reconstructed legendary quests.
- Legacy hire-the-giver rewards supported with the original alignment gate where applicable.
- Legendary NPCs prefer their bound legendary quest instead of randomly offering a generic regional quest when their special quest is available.

## Crocodile dependency

Howland Reed's final-build quest requires 50 Crocodile kills, so the missing animal is restored:

- Registry ID: `got:crocodile`
- 20 HP
- 0.22 movement speed
- 4 attack damage
- Hostile melee targeting
- Legacy snap sound/attack presentation
- Water pursuit behavior
- Frost immunity
- Reconstructed legacy 128x128 Crocodile model/texture presentation
- Natural spawn: Sothoryos Mangrove

## Mellario dependency

Mellario is restored as a legendary Norvos NPC:

- Role ID: `mellario`
- Faction: Norvos
- Female
- Alignment value: +300
- Weapon: Alloy Steel Dagger
- Legacy legendary texture
- Fixed spawn at Norvos waypoint, legacy fixer offset `(0, +1)`
- Bound quest: `got:mellario_doran`

## Validation

`migration/audit-systems-pass11-legendary-quests.py` verifies:

- exactly 25 final-build generic legendary quest definitions;
- no `content_audit_pending` legendary bindings remain;
- all giver roles resolve;
- all role/entity/item objective IDs resolve statically;
- Crocodile and Mellario integration markers exist.

Static audit result: **PASS**.

No Gradle build was attempted; source-only delivery per project workflow.

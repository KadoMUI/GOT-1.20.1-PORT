from pathlib import Path
root = Path(__file__).resolve().parents[1]
checks = {
    'mounted hired controller exists': (root/'src/main/java/got/npc/hiring/GOTHiredMountController.java').exists(),
    'mount delegates hired idle movement': 'GOTHiredMountController.tick(this, rider)' in (root/'src/main/java/got/mount/GOTMountEntity.java').read_text(),
    'command horn moves rider+mount': 'GOTHiredMountController.teleportUnit' in (root/'src/main/java/got/npc/hiring/command/GOTCommandHornService.java').read_text(),
    'follow auto-teleport moves rider+mount': 'GOTHiredMountController.teleportUnit' in (root/'src/main/java/got/npc/hiring/GOTHiredAi.java').read_text(),
    'hold/patrol work owner-offline': 'HOLD/PATROL/WANDER must continue functioning' in (root/'src/main/java/got/npc/hiring/GOTHiredAi.java').read_text(),
    'respawner excludes hired replacement': '!GOTHiredData.isHired(npc)' in (root/'src/main/java/got/npc/GOTNpcRespawnerData.java').read_text(),
    'npc mount cleaned on rider death': 'onHiredDeath' in (root/'src/main/java/got/npc/hiring/GOTHiringEvents.java').read_text(),
}
for name, ok in checks.items(): print(('PASS' if ok else 'FAIL') + ': ' + name)
if not all(checks.values()): raise SystemExit(1)

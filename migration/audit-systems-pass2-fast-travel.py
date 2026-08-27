#!/usr/bin/env python3
from pathlib import Path
root=Path(__file__).resolve().parents[1]
manager=(root/'src/main/java/got/common/fasttravel/GOTFastTravelManager.java').read_text()
data=(root/'src/main/java/got/common/fasttravel/GOTFastTravelData.java').read_text()
events=(root/'src/main/java/got/common/fasttravel/GOTFastTravelEvents.java').read_text()
mapjava=(root/'src/main/java/got/client/gui/GOTGuiMap.java').read_text()
net=(root/'src/main/java/got/network/GOTNetwork.java').read_text()
checks={
'legacy warmup 200 ticks':'WARMUP_TICKS = 200' in manager,
'legacy cooldown defaults':'COOLDOWN_MIN_SECONDS = 60' in manager and 'COOLDOWN_MAX_SECONDS = 600' in manager,
'legacy repeated-use discount':'Math.pow(0.9D, useCount)' in manager,
'distance cooldown scaling':'distance * 1.2E-5D' in manager,
'movement cancellation':'got.fastTravel.motion' in manager and 'StartX' in manager,
'damage cancellation':'LivingHurtEvent' in events,
'under attack gate':'isUnderAttack' in manager,
'region unlock persistence':'UnlockedRegions' in data,
'custom waypoint persistence':'CustomWaypoints' in data,
'custom create/rename/delete packets':all(x in net for x in ['C2SCreateCustomWaypointPacket','C2SRenameCustomWaypointPacket','C2SDeleteCustomWaypointPacket']),
'map custom waypoint UI':'GOTGuiCreateWaypoint' in mapjava and 'getCustomWaypointAt' in mapjava,
'mounted/pet entourage':'ENTOURAGE_RADIUS = 256.0D' in manager and 'TamableAnimal' in manager and 'isFollowingHiredOwner' in manager,
'use count persistence':'UseCounts' in data,
}
failed=[k for k,v in checks.items() if not v]
for k,v in checks.items(): print(('PASS' if v else 'FAIL'),k)
raise SystemExit(1 if failed else 0)

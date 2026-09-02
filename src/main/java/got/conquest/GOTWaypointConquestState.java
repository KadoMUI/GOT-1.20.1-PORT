package got.conquest;

import got.common.world.map.GOTWaypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistent mutable conquest state for one map waypoint.
 *
 * The waypoint's lore/native identity is deliberately NOT duplicated here. Region,
 * native faction and coordinates remain authoritative on {@link GOTWaypoint}; this
 * object stores only conquest-layer state which can change during a campaign.
 */
public final class GOTWaypointConquestState {
    public static final int DEFAULT_STRATEGIC_VALUE = 25;

    private final GOTWaypoint waypoint;
    private int strategicValue;
    private boolean anchor;
    private GOTConquestControlState controlState = GOTConquestControlState.NATIVE;
    private UUID controllingPact;
    private UUID claimingPact;
    private final Map<UUID, Integer> conquestScores = new LinkedHashMap<>();
    private long lastUpdatedGameTime;

    GOTWaypointConquestState(GOTWaypoint waypoint) {
        this(waypoint, DEFAULT_STRATEGIC_VALUE);
    }

    GOTWaypointConquestState(GOTWaypoint waypoint, int strategicValue) {
        this.waypoint = waypoint;
        this.strategicValue = Math.max(0, strategicValue);
    }

    public GOTWaypoint waypoint() { return waypoint; }
    public String waypointKey() { return waypoint.name(); }
    public int strategicValue() { return strategicValue; }
    public boolean anchor() { return anchor; }
    public GOTConquestControlState controlState() { return controlState; }
    public boolean contested() { return controlState == GOTConquestControlState.CONTESTED; }
    public Optional<UUID> controllingPact() { return Optional.ofNullable(controllingPact); }
    public Optional<UUID> claimingPact() { return Optional.ofNullable(claimingPact); }
    public Map<UUID, Integer> conquestScores() { return Collections.unmodifiableMap(conquestScores); }
    public int conquestScore(UUID pactId) { return conquestScores.getOrDefault(pactId, 0); }
    public long lastUpdatedGameTime() { return lastUpdatedGameTime; }

    void setStrategicValue(int strategicValue, long gameTime) {
        this.strategicValue = Math.max(0, strategicValue);
        touch(gameTime);
    }

    void setAnchor(boolean anchor, long gameTime) {
        this.anchor = anchor;
        touch(gameTime);
    }

    void setControlState(GOTConquestControlState controlState, long gameTime) {
        this.controlState = controlState == null ? GOTConquestControlState.NATIVE : controlState;
        if (this.controlState != GOTConquestControlState.CONTROLLED) this.controllingPact = null;
        touch(gameTime);
    }

    void setControllingPact(UUID controllingPact, long gameTime) {
        this.controllingPact = controllingPact;
        this.controlState = controllingPact == null ? GOTConquestControlState.NATIVE : GOTConquestControlState.CONTROLLED;
        touch(gameTime);
    }

    void setClaimingPact(UUID claimingPact, long gameTime) {
        this.claimingPact = claimingPact;
        touch(gameTime);
    }

    void setConquestScore(UUID pactId, int score, long gameTime) {
        if (score == 0) conquestScores.remove(pactId);
        else conquestScores.put(pactId, score);
        touch(gameTime);
    }

    void clearConquestScore(UUID pactId, long gameTime) {
        conquestScores.remove(pactId);
        touch(gameTime);
    }

    void resetMutableState(long gameTime) {
        strategicValue = DEFAULT_STRATEGIC_VALUE;
        anchor = false;
        controlState = GOTConquestControlState.NATIVE;
        controllingPact = null;
        claimingPact = null;
        conquestScores.clear();
        touch(gameTime);
    }

    private void touch(long gameTime) {
        lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Waypoint", waypoint.name());
        tag.putInt("StrategicValue", strategicValue);
        tag.putBoolean("Anchor", anchor);
        tag.putString("ControlState", controlState.name());
        if (controllingPact != null) tag.putUUID("ControllingPact", controllingPact);
        if (claimingPact != null) tag.putUUID("ClaimingPact", claimingPact);
        tag.putLong("LastUpdatedGameTime", lastUpdatedGameTime);

        ListTag scores = new ListTag();
        for (Map.Entry<UUID, Integer> entry : conquestScores.entrySet()) {
            CompoundTag scoreTag = new CompoundTag();
            scoreTag.putUUID("Pact", entry.getKey());
            scoreTag.putInt("Score", entry.getValue());
            scores.add(scoreTag);
        }
        tag.put("Scores", scores);
        return tag;
    }

    static GOTWaypointConquestState load(CompoundTag tag) {
        GOTWaypoint waypoint = GOTWaypoint.waypointForName(tag.getString("Waypoint"));
        if (waypoint == null) return null;

        int strategicValue = tag.contains("StrategicValue", Tag.TAG_INT)
                ? tag.getInt("StrategicValue")
                : DEFAULT_STRATEGIC_VALUE;
        GOTWaypointConquestState state = new GOTWaypointConquestState(waypoint, strategicValue);
        state.anchor = tag.getBoolean("Anchor");
        GOTConquestControlState loadedState = GOTConquestControlState.byName(tag.getString("ControlState"));
        if (loadedState != null) state.controlState = loadedState;
        else if (tag.getBoolean("Contested")) state.controlState = GOTConquestControlState.CONTESTED; // pre-schema compatibility
        if (tag.hasUUID("ControllingPact")) {
            state.controllingPact = tag.getUUID("ControllingPact");
            if (loadedState == null) state.controlState = GOTConquestControlState.CONTROLLED;
        }
        if (tag.hasUUID("ClaimingPact")) state.claimingPact = tag.getUUID("ClaimingPact");
        state.lastUpdatedGameTime = Math.max(0L, tag.getLong("LastUpdatedGameTime"));

        ListTag scores = tag.getList("Scores", Tag.TAG_COMPOUND);
        for (int i = 0; i < scores.size(); i++) {
            CompoundTag scoreTag = scores.getCompound(i);
            if (!scoreTag.hasUUID("Pact")) continue;
            int score = scoreTag.getInt("Score");
            if (score != 0) state.conquestScores.put(scoreTag.getUUID("Pact"), score);
        }
        return state;
    }
}

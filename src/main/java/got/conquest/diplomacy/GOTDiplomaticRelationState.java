package got.conquest.diplomacy;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;

/**
 * Persistent relationship between two Pacts.
 *
 * Formal treaties/status are symmetric. Opinion, trust, fear, and willingness
 * to communicate are directional so the two sides do not have to feel the same
 * way about one another.
 */
public final class GOTDiplomaticRelationState {
    private final UUID pactA;
    private final UUID pactB;

    private int opinionAToB;
    private int opinionBToA;
    private int trustAToB;
    private int trustBToA;
    private int fearAToB;
    private int fearBToA;
    private GOTCommunicationPolicy communicationAToB = GOTCommunicationPolicy.OPEN;
    private GOTCommunicationPolicy communicationBToA = GOTCommunicationPolicy.OPEN;

    private GOTDiplomaticStatus status = GOTDiplomaticStatus.PEACE;
    private boolean nonAggressionPact;
    private boolean tradeAgreement;
    private long truceUntilGameTime;
    private long lastInteractionGameTime;
    private long lastUpdatedGameTime;

    public GOTDiplomaticRelationState(UUID first, UUID second) {
        if (first == null || second == null || first.equals(second)) {
            throw new IllegalArgumentException("Diplomatic relation requires two different Pact UUIDs");
        }
        if (first.compareTo(second) <= 0) {
            this.pactA = first;
            this.pactB = second;
        } else {
            this.pactA = second;
            this.pactB = first;
        }
    }

    public UUID pactA() { return pactA; }
    public UUID pactB() { return pactB; }
    public GOTDiplomaticStatus status() { return status; }
    public boolean nonAggressionPact() { return nonAggressionPact; }
    public boolean tradeAgreement() { return tradeAgreement; }
    public long truceUntilGameTime() { return truceUntilGameTime; }
    public long lastInteractionGameTime() { return lastInteractionGameTime; }
    public long lastUpdatedGameTime() { return lastUpdatedGameTime; }

    public boolean contains(UUID pactId) {
        return pactA.equals(pactId) || pactB.equals(pactId);
    }

    public UUID other(UUID pactId) {
        if (pactA.equals(pactId)) return pactB;
        if (pactB.equals(pactId)) return pactA;
        throw new IllegalArgumentException("Pact " + pactId + " is not part of this diplomatic relation");
    }

    public int opinion(UUID from, UUID to) {
        requireDirection(from, to);
        return pactA.equals(from) ? opinionAToB : opinionBToA;
    }

    public int trust(UUID from, UUID to) {
        requireDirection(from, to);
        return pactA.equals(from) ? trustAToB : trustBToA;
    }

    public int fear(UUID from, UUID to) {
        requireDirection(from, to);
        return pactA.equals(from) ? fearAToB : fearBToA;
    }

    public GOTCommunicationPolicy communication(UUID from, UUID to) {
        requireDirection(from, to);
        return pactA.equals(from) ? communicationAToB : communicationBToA;
    }

    public GOTDiplomaticAttitude attitude(UUID from, UUID to) {
        return GOTDiplomaticAttitude.fromOpinion(opinion(from, to));
    }

    public void setOpinion(UUID from, UUID to, int value, long gameTime) {
        requireDirection(from, to);
        int clamped = clamp(value, -1000, 1000);
        if (pactA.equals(from)) opinionAToB = clamped;
        else opinionBToA = clamped;
        touch(gameTime);
    }

    public void setTrust(UUID from, UUID to, int value, long gameTime) {
        requireDirection(from, to);
        int clamped = clamp(value, 0, 1000);
        if (pactA.equals(from)) trustAToB = clamped;
        else trustBToA = clamped;
        touch(gameTime);
    }

    public void setFear(UUID from, UUID to, int value, long gameTime) {
        requireDirection(from, to);
        int clamped = clamp(value, 0, 1000);
        if (pactA.equals(from)) fearAToB = clamped;
        else fearBToA = clamped;
        touch(gameTime);
    }

    public void setCommunication(UUID from, UUID to, GOTCommunicationPolicy policy, long gameTime) {
        requireDirection(from, to);
        if (policy == null) throw new IllegalArgumentException("Communication policy cannot be null");
        if (pactA.equals(from)) communicationAToB = policy;
        else communicationBToA = policy;
        touch(gameTime);
    }

    public void setStatus(GOTDiplomaticStatus status, long gameTime) {
        if (status == null) throw new IllegalArgumentException("Diplomatic status cannot be null");
        this.status = status;
        if (status == GOTDiplomaticStatus.AT_WAR) {
            nonAggressionPact = false;
            tradeAgreement = false;
            truceUntilGameTime = 0L;
        }
        touchInteraction(gameTime);
    }

    public void setNonAggressionPact(boolean value, long gameTime) {
        if (value && status == GOTDiplomaticStatus.AT_WAR) {
            throw new IllegalStateException("Cannot establish a non-aggression pact while at war");
        }
        nonAggressionPact = value;
        touchInteraction(gameTime);
    }

    public void setTradeAgreement(boolean value, long gameTime) {
        if (value && status == GOTDiplomaticStatus.AT_WAR) {
            throw new IllegalStateException("Cannot establish a trade agreement while at war");
        }
        tradeAgreement = value;
        touchInteraction(gameTime);
    }

    public void setTruceUntilGameTime(long value, long gameTime) {
        truceUntilGameTime = Math.max(0L, value);
        touchInteraction(gameTime);
    }

    public void applySeed(int aToBOpinion, int bToAOpinion,
                          int aToBTrust, int bToATrust,
                          int aToBFear, int bToAFear,
                          GOTCommunicationPolicy aToBCommunication,
                          GOTCommunicationPolicy bToACommunication) {
        this.opinionAToB = clamp(aToBOpinion, -1000, 1000);
        this.opinionBToA = clamp(bToAOpinion, -1000, 1000);
        this.trustAToB = clamp(aToBTrust, 0, 1000);
        this.trustBToA = clamp(bToATrust, 0, 1000);
        this.fearAToB = clamp(aToBFear, 0, 1000);
        this.fearBToA = clamp(bToAFear, 0, 1000);
        this.communicationAToB = aToBCommunication == null ? GOTCommunicationPolicy.OPEN : aToBCommunication;
        this.communicationBToA = bToACommunication == null ? GOTCommunicationPolicy.OPEN : bToACommunication;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("PactA", pactA);
        tag.putUUID("PactB", pactB);
        tag.putInt("OpinionAToB", opinionAToB);
        tag.putInt("OpinionBToA", opinionBToA);
        tag.putInt("TrustAToB", trustAToB);
        tag.putInt("TrustBToA", trustBToA);
        tag.putInt("FearAToB", fearAToB);
        tag.putInt("FearBToA", fearBToA);
        tag.putString("CommunicationAToB", communicationAToB.name());
        tag.putString("CommunicationBToA", communicationBToA.name());
        tag.putString("Status", status.name());
        tag.putBoolean("NonAggressionPact", nonAggressionPact);
        tag.putBoolean("TradeAgreement", tradeAgreement);
        tag.putLong("TruceUntilGameTime", truceUntilGameTime);
        tag.putLong("LastInteractionGameTime", lastInteractionGameTime);
        tag.putLong("LastUpdatedGameTime", lastUpdatedGameTime);
        return tag;
    }

    public static GOTDiplomaticRelationState load(CompoundTag tag) {
        if (!tag.hasUUID("PactA") || !tag.hasUUID("PactB")) return null;
        UUID pactA = tag.getUUID("PactA");
        UUID pactB = tag.getUUID("PactB");
        if (pactA.equals(pactB)) return null;
        GOTDiplomaticRelationState state = new GOTDiplomaticRelationState(pactA, pactB);

        UUID storedA = pactA.compareTo(pactB) <= 0 ? pactA : pactB;
        boolean sameOrientation = state.pactA.equals(storedA) && pactA.equals(storedA);
        int firstOpinion = clamp(tag.getInt("OpinionAToB"), -1000, 1000);
        int secondOpinion = clamp(tag.getInt("OpinionBToA"), -1000, 1000);
        int firstTrust = clamp(tag.getInt("TrustAToB"), 0, 1000);
        int secondTrust = clamp(tag.getInt("TrustBToA"), 0, 1000);
        int firstFear = clamp(tag.getInt("FearAToB"), 0, 1000);
        int secondFear = clamp(tag.getInt("FearBToA"), 0, 1000);
        GOTCommunicationPolicy firstCommunication = communication(tag.getString("CommunicationAToB"));
        GOTCommunicationPolicy secondCommunication = communication(tag.getString("CommunicationBToA"));

        if (sameOrientation) {
            state.opinionAToB = firstOpinion;
            state.opinionBToA = secondOpinion;
            state.trustAToB = firstTrust;
            state.trustBToA = secondTrust;
            state.fearAToB = firstFear;
            state.fearBToA = secondFear;
            state.communicationAToB = firstCommunication;
            state.communicationBToA = secondCommunication;
        } else {
            state.opinionAToB = secondOpinion;
            state.opinionBToA = firstOpinion;
            state.trustAToB = secondTrust;
            state.trustBToA = firstTrust;
            state.fearAToB = secondFear;
            state.fearBToA = firstFear;
            state.communicationAToB = secondCommunication;
            state.communicationBToA = firstCommunication;
        }

        GOTDiplomaticStatus parsedStatus = GOTDiplomaticStatus.byName(tag.getString("Status"));
        state.status = parsedStatus == null ? GOTDiplomaticStatus.PEACE : parsedStatus;
        state.nonAggressionPact = tag.getBoolean("NonAggressionPact") && state.status != GOTDiplomaticStatus.AT_WAR;
        state.tradeAgreement = tag.getBoolean("TradeAgreement") && state.status != GOTDiplomaticStatus.AT_WAR;
        state.truceUntilGameTime = Math.max(0L, tag.getLong("TruceUntilGameTime"));
        state.lastInteractionGameTime = Math.max(0L, tag.getLong("LastInteractionGameTime"));
        state.lastUpdatedGameTime = Math.max(0L, tag.getLong("LastUpdatedGameTime"));
        return state;
    }

    public static String key(UUID first, UUID second) {
        if (first == null || second == null || first.equals(second)) return "";
        UUID a = first.compareTo(second) <= 0 ? first : second;
        UUID b = first.compareTo(second) <= 0 ? second : first;
        return a + "|" + b;
    }

    private void requireDirection(UUID from, UUID to) {
        if (from == null || to == null || from.equals(to) || !contains(from) || !contains(to)) {
            throw new IllegalArgumentException("Direction does not match this diplomatic relation");
        }
    }

    private void touch(long gameTime) {
        lastUpdatedGameTime = Math.max(0L, gameTime);
    }

    private void touchInteraction(long gameTime) {
        long value = Math.max(0L, gameTime);
        lastInteractionGameTime = value;
        lastUpdatedGameTime = value;
    }

    private static GOTCommunicationPolicy communication(String raw) {
        GOTCommunicationPolicy parsed = GOTCommunicationPolicy.byName(raw);
        return parsed == null ? GOTCommunicationPolicy.OPEN : parsed;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}

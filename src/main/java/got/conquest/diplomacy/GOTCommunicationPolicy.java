package got.conquest.diplomacy;

import java.util.Locale;

/** Directional willingness to accept ordinary diplomatic contact. */
public enum GOTCommunicationPolicy {
    OPEN,
    GUARDED,
    CLOSED;

    public static GOTCommunicationPolicy byName(String raw) {
        if (raw == null) return null;
        try {
            return valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

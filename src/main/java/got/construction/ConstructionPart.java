package got.construction;

import java.util.Locale;

/**
 * Reusable construction shapes supported by the port.
 *
 * <p>The legacy mod packed many of these into metadata blocks.  The 1.20.1
 * port deliberately gives every shape a stable registry id, while the family
 * catalogue keeps the repetitive declarations in one place.</p>
 */
public enum ConstructionPart {
    STAIRS("stairs", "Stairs"),
    SLAB("slab", "Slab"),
    WALL("wall", "Wall"),
    FENCE("fence", "Fence"),
    FENCE_GATE("fence_gate", "Fence Gate"),
    DOOR("door", "Door"),
    TRAPDOOR("trapdoor", "Trapdoor"),
    BEAM("beam", "Beam");

    private final String suffix;
    private final String displaySuffix;

    ConstructionPart(String suffix, String displaySuffix) {
        this.suffix = suffix;
        this.displaySuffix = displaySuffix;
    }

    public String suffix() {
        return suffix;
    }

    public String displaySuffix() {
        return displaySuffix;
    }

    public static ConstructionPart parse(String value) {
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        return ConstructionPart.valueOf(normalized);
    }
}

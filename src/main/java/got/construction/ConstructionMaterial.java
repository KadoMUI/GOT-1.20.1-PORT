package got.construction;

import java.util.Locale;

/** Controls recipe/tool defaults without hard-coding individual cultures. */
public enum ConstructionMaterial {
    MASONRY,
    WOOD,
    THATCH,
    EARTH,
    MISC;

    public static ConstructionMaterial parse(String value) {
        return ConstructionMaterial.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }

    public boolean isWoodLike() {
        return this == WOOD || this == THATCH;
    }
}

package got.claim;

import java.util.EnumSet;

/** The eight permission classes exposed by the original banner editor. */
public enum GOTBannerPermission {
    FULL,
    DOORS,
    TABLES,
    CONTAINERS,
    PERSONAL_CONTAINERS,
    FOOD,
    BEDS,
    SWITCHES;

    public int bit() {
        return 1 << ordinal();
    }

    public String translationKey() {
        return "got.gui.bannerEdit.perm." + name();
    }

    public static int encode(Iterable<GOTBannerPermission> permissions) {
        int bits = 0;
        for (GOTBannerPermission permission : permissions) bits |= permission.bit();
        return bits;
    }

    public static EnumSet<GOTBannerPermission> decode(int bits) {
        EnumSet<GOTBannerPermission> result = EnumSet.noneOf(GOTBannerPermission.class);
        for (GOTBannerPermission permission : values()) {
            if ((bits & permission.bit()) != 0) result.add(permission);
        }
        return result;
    }
}

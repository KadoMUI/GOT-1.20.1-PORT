package got.claim;

import net.minecraftforge.common.ForgeConfigSpec;

/** Server-owner controls corresponding to the original banner protection options. */
public final class GOTBannerClaimConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue SELF_PROTECTION;
    public static final ForgeConfigSpec.BooleanValue PREVENT_HOSTILE_SPAWNS;
    public static final ForgeConfigSpec.BooleanValue PROTECT_FROM_EXPLOSIONS;
    public static final ForgeConfigSpec.BooleanValue PROTECT_FROM_PROJECTILES;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("bannerClaims");
        ENABLED = builder.comment("Enable player-created banner territory protection.")
                .define("enabled", true);
        SELF_PROTECTION = builder.comment("Allow claims to protect their own banner from unauthorized breaking.")
                .define("selfProtection", true);
        PREVENT_HOSTILE_SPAWNS = builder.comment("Prevent hostile mobs and enemy-faction NPCs from spawning inside claims.")
                .define("preventHostileSpawns", true);
        PROTECT_FROM_EXPLOSIONS = builder.comment("Remove claimed blocks and banners from explosion effects.")
                .define("protectFromExplosions", true);
        PROTECT_FROM_PROJECTILES = builder.comment("Stop unauthorized projectiles from modifying claimed blocks.")
                .define("protectFromProjectiles", true);
        builder.pop();
        SPEC = builder.build();
    }

    private GOTBannerClaimConfig() {}
}

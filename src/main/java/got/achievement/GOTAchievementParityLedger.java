package got.achievement;

import java.util.List;

public final class GOTAchievementParityLedger {
    private GOTAchievementParityLedger() {}
    public enum Status { AUDITED, COMPLETE, INTEGRATED, DEPENDENCY_BLOCKED }
    public record Entry(String feature, Status status, String note) {}
    public static List<Entry> entries() { return List.of(
            new Entry("Original 390-entry achievement catalog", Status.COMPLETE, "Exact recovered IDs/categories/localization retained"),
            new Entry("Persistence / client synchronization", Status.COMPLETE, "Death-copy plus immediate award sync and GUI request sync"),
            new Entry("Biome / Traveller triggers", Status.INTEGRATED, "Biome-entry achievements plus 20/40/60/80/100 unique Planetos biomes"),
            new Entry("Kill / legendary NPC triggers", Status.INTEGRATED, "Entity registry IDs and consolidated NPC role IDs"),
            new Entry("Full armor triggers", Status.INTEGRATED, "Catalog-driven four-slot armor-family matching"),
            new Entry("Craft / use / drink / pickup / mount triggers", Status.INTEGRATED, "Registry-driven trigger bridge for currently implemented content"),
            new Entry("Quest / trade / invasion / smithing / crime / pledge / hiring hooks", Status.INTEGRATED, "Direct subsystem hooks"),
            new Entry("Alloy Forge / Millstone achievements", Status.INTEGRATED, "Awarded from the legacy-style result slots when machine output is taken"),
            new Entry("Achievements for still-unported mechanics", Status.DEPENDENCY_BLOCKED, "Carts/pouches and other absent legacy systems award when those systems are restored")
    ); }
}

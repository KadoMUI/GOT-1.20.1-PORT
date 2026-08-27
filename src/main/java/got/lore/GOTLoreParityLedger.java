package got.lore;

import java.util.List;

public final class GOTLoreParityLedger {
    private GOTLoreParityLedger() {}
    public enum Status { AUDITED, COMPLETE, INTEGRATED }
    public record Entry(String feature, Status status, String note) {}
    public static List<Entry> entries() { return List.of(
            new Entry("Original lore corpus", Status.COMPLETE, "502 preserved localized text resources"),
            new Entry("Metadata parser", Status.COMPLETE, "title/author/types/reward and recovered formatting directives; # directives and BOM handled"),
            new Entry("Server resource reload", Status.INTEGRATED, "Lore registry now loads on datapack/resource reload"),
            new Entry("Written lore books", Status.INTEGRATED, "Legacy written-book presentation restored with GOTLoreId"),
            new Entry("Discovery persistence", Status.COMPLETE, "Reading a lore book permanently records its ID and survives death"),
            new Entry("Miniquest lore rewards", Status.INTEGRATED, "Legacy 1-in-10 extra lore-book roll using player language and faction/type categories"),
            new Entry("Discovered-lore browser", Status.INTEGRATED, "Menu screen requests discovered entry text from the server")
    ); }
}

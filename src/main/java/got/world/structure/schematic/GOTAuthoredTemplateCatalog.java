package got.world.structure.schematic;

import got.world.structure.north.NorthStructureBuilder;

public enum GOTAuthoredTemplateCatalog {
    WESTEROS_SMALL_CASTLE("WesterosCastleSmall.schem"),
    WILDLING_HOUSE("Wildling_House.schem");

    private final String file;
    GOTAuthoredTemplateCatalog(String file) { this.file = file; }
    public String file() { return file; }
    public boolean place(NorthStructureBuilder builder) {
        return AuthoredSchematicTemplate.place(builder, file);
    }
}

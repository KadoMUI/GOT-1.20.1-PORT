package got.construction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.Objects;

/** One full-block material restored for use by construction variants. */
public record ConstructionBase(
        String id,
        String displayName,
        ConstructionMaterial material,
        Kind kind,
        ResourceLocation copyBlock,
        RegistryObject<Block> block,
        Map<String, ResourceLocation> textures) {
    public ConstructionBase {
        Objects.requireNonNull(id);
        Objects.requireNonNull(displayName);
        Objects.requireNonNull(material);
        Objects.requireNonNull(kind);
        Objects.requireNonNull(copyBlock);
        Objects.requireNonNull(block);
        textures = Map.copyOf(textures);
    }

    public enum Kind {
        CUBE,
        COLUMN,
        FALLING,
        PILLAR
    }
}

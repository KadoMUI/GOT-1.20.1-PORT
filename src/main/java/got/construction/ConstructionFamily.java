package got.construction;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/** Immutable description of one base material and all registered child shapes. */
public final class ConstructionFamily {
    private final String familyId;
    private final ResourceLocation baseBlock;
    private final String displayName;
    private final ConstructionMaterial material;
    private final ResourceLocation baseTexture;
    private final ResourceLocation baseModel;
    private final Map<ConstructionPart, Variant> variants;

    ConstructionFamily(
            String familyId,
            ResourceLocation baseBlock,
            String displayName,
            ConstructionMaterial material,
            ResourceLocation baseTexture,
            ResourceLocation baseModel,
            Map<ConstructionPart, Variant> variants) {
        this.familyId = Objects.requireNonNull(familyId);
        this.baseBlock = Objects.requireNonNull(baseBlock);
        this.displayName = Objects.requireNonNull(displayName);
        this.material = Objects.requireNonNull(material);
        this.baseTexture = Objects.requireNonNull(baseTexture);
        this.baseModel = Objects.requireNonNull(baseModel);
        this.variants = Collections.unmodifiableMap(new EnumMap<>(variants));
    }

    public String familyId() {
        return familyId;
    }

    public ResourceLocation baseBlock() {
        return baseBlock;
    }

    public String displayName() {
        return displayName;
    }

    public ConstructionMaterial material() {
        return material;
    }

    public ResourceLocation baseTexture() {
        return baseTexture;
    }

    public ResourceLocation baseModel() {
        return baseModel;
    }

    public Map<ConstructionPart, Variant> variants() {
        return variants;
    }

    public Variant variant(ConstructionPart part) {
        return variants.get(part);
    }

    public record Variant(
            ConstructionPart part,
            String id,
            String displayName,
            RegistryObject<Block> block,
            Map<String, ResourceLocation> textures) {
        public Variant {
            Objects.requireNonNull(part);
            Objects.requireNonNull(id);
            Objects.requireNonNull(displayName);
            Objects.requireNonNull(block);
            textures = Map.copyOf(textures);
        }
    }
}

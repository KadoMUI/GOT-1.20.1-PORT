package got.world;

import got.GOTMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

public final class GOTDimensions {
    public static final ResourceKey<Level> PLANETOS = ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "planetos"));
    public static final ResourceKey<DimensionType> PLANETOS_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "planetos"));
    public static final ResourceKey<WorldPreset> PLANETOS_PRESET = ResourceKey.create(Registries.WORLD_PRESET, ResourceLocation.fromNamespaceAndPath(GOTMod.MOD_ID, "planetos"));
    private GOTDimensions() {}
}

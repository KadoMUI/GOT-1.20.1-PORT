package got.world.structure.schematic;

import got.GOTMod;
import got.world.structure.north.NorthStructureBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.*;

/** Reusable Sponge-v2 template placer. Schematic air is always ignored. */
public final class AuthoredSchematicTemplate {
    private static final String ROOT = "/data/got/structures/templates/";
    private static final Map<String, SoftReference<Schematic>> CACHE = new HashMap<>();

    private AuthoredSchematicTemplate() {}

    public static boolean place(NorthStructureBuilder builder, String file) {
        Schematic schematic = load(file);
        if (schematic == null) return false;
        schematic.place(builder);
        return true;
    }

    @Nullable
    private static Schematic load(String file) {
        synchronized (CACHE) {
            SoftReference<Schematic> ref = CACHE.get(file);
            Schematic cached = ref == null ? null : ref.get();
            if (cached != null) return cached;
            try (InputStream in = AuthoredSchematicTemplate.class.getResourceAsStream(ROOT + file)) {
                if (in == null) throw new IOException("missing template " + file);
                Schematic loaded = Schematic.read(in);
                CACHE.put(file, new SoftReference<>(loaded));
                GOTMod.LOGGER.info("Loaded authored reusable template {} ({}x{}x{})",
                        file, loaded.width, loaded.height, loaded.length);
                return loaded;
            } catch (Exception ex) {
                GOTMod.LOGGER.error("Unable to load authored reusable template {}", file, ex);
                return null;
            }
        }
    }

    private record BlockEntityData(int x, int y, int z, CompoundTag tag) {}

    private static final class Schematic {
        final int width,height,length,offsetX,offsetY,offsetZ;
        final BlockState[] palette;
        final byte[] blockData;
        final List<BlockEntityData> blockEntities;

        Schematic(int width,int height,int length,int offsetX,int offsetY,int offsetZ,
                  BlockState[] palette,byte[] blockData,List<BlockEntityData> blockEntities) {
            this.width=width; this.height=height; this.length=length;
            this.offsetX=offsetX; this.offsetY=offsetY; this.offsetZ=offsetZ;
            this.palette=palette; this.blockData=blockData; this.blockEntities=blockEntities;
        }

        static Schematic read(InputStream input) throws IOException {
            CompoundTag root=NbtIo.readCompressed(input);
            if(root.getInt("Version")!=2) throw new IOException("unsupported Sponge schematic version");
            int width=root.getShort("Width")&0xffff, height=root.getShort("Height")&0xffff, length=root.getShort("Length")&0xffff;
            CompoundTag md=root.getCompound("Metadata");
            int ox=md.getInt("WEOffsetX"), oy=md.getInt("WEOffsetY"), oz=md.getInt("WEOffsetZ");
            CompoundTag ptag=root.getCompound("Palette");
            BlockState[] palette=new BlockState[Math.max(1,root.getInt("PaletteMax"))];
            Arrays.fill(palette,Blocks.AIR.defaultBlockState());
            for(String s:ptag.getAllKeys()) {
                int id=ptag.getInt(s);
                if(id>=0 && id<palette.length) palette[id]=parseBlockState(s);
            }
            return new Schematic(width,height,length,ox,oy,oz,palette,root.getByteArray("BlockData"),readBEs(root));
        }

        void place(NorthStructureBuilder b) {
            int[] cursor={0};
            for(int y=0;y<height;y++) for(int z=0;z<length;z++) for(int x=0;x<width;x++) {
                int id=readVarInt(blockData,cursor);
                BlockState state=id>=0 && id<palette.length ? palette[id] : Blocks.AIR.defaultBlockState();
                if(state.isAir()) continue;
                b.set(offsetX+x,offsetY+y,offsetZ+z,state);
            }
            for(BlockEntityData data:blockEntities) {
                BlockPos pos=b.worldPos(offsetX+data.x,offsetY+data.y,offsetZ+data.z);
                if(!b.insideClip(pos)) continue;
                BlockEntity be=b.level().getBlockEntity(pos);
                if(be==null) continue;
                CompoundTag tag=data.tag.copy();
                String id=tag.getString("Id");
                tag.remove("Id"); tag.remove("Pos");
                if(!id.isEmpty()) tag.putString("id",id);
                tag.putInt("x",pos.getX()); tag.putInt("y",pos.getY()); tag.putInt("z",pos.getZ());
                be.load(tag); be.setChanged();
            }
        }

        static List<BlockEntityData> readBEs(CompoundTag root) {
            List<BlockEntityData> out=new ArrayList<>();
            ListTag list=root.getList("BlockEntities",Tag.TAG_COMPOUND);
            for(int i=0;i<list.size();i++) {
                CompoundTag tag=list.getCompound(i);
                int[] p=tag.getIntArray("Pos");
                if(p.length==3) out.add(new BlockEntityData(p[0],p[1],p[2],tag.copy()));
            }
            return List.copyOf(out);
        }

        static BlockState parseBlockState(String s) {
            int start=s.indexOf('[');
            String name=start<0?s:s.substring(0,start);
            ResourceLocation id=ResourceLocation.tryParse(name);
            Block block=id==null?null:ForgeRegistries.BLOCKS.getValue(id);
            if(block==null) {
                GOTMod.LOGGER.warn("Authored template references missing block {}",name);
                return Blocks.AIR.defaultBlockState();
            }
            BlockState state=block.defaultBlockState();
            if(start<0 || !s.endsWith("]")) return state;
            String props=s.substring(start+1,s.length()-1);
            if(props.isEmpty()) return state;
            for(String e:props.split(",")) {
                int sep=e.indexOf('=');
                if(sep<=0) continue;
                Property<?> property=block.getStateDefinition().getProperty(e.substring(0,sep));
                if(property!=null) state=setProperty(state,property,e.substring(sep+1));
            }
            return state;
        }

        static <T extends Comparable<T>> BlockState setProperty(BlockState s,Property<T> p,String v) {
            Optional<T> parsed=p.getValue(v);
            return parsed.map(x->s.setValue(p,x)).orElse(s);
        }

        static int readVarInt(byte[] data,int[] cursor) {
            int value=0,shift=0;
            while(cursor[0]<data.length) {
                int next=data[cursor[0]++]&0xff;
                value|=(next&0x7f)<<shift;
                if((next&0x80)==0) return value;
                shift+=7;
                if(shift>28) return 0;
            }
            return 0;
        }
    }
}

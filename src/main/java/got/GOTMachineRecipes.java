package got;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Legacy 1.7.10 Alloy Forge and Millstone recipe bridge. */
public final class GOTMachineRecipes {
    public record MillResult(ItemStack stack,float chance){}
    private static final Map<String,MillResult> MILL = new LinkedHashMap<>();
    static {
        mill("minecraft:stone","minecraft:cobblestone",1,1F);
        mill("minecraft:cobblestone","minecraft:gravel",1,.75F);
        mill("minecraft:gravel","minecraft:flint",1,.25F);
        mill("minecraft:sandstone","minecraft:sand",2,1F);
        mill("minecraft:red_sandstone","minecraft:red_sand",2,1F);
        mill("got:white_sandstone","got:white_sand",2,1F);
        mill("got:basalt_rock","got:basalt_gravel",1,.75F);
        mill("got:basalt_gravel","minecraft:flint",1,.25F);
        mill("got:obsidian_gravel","got:obsidian_shard",1,1F);
        mill("got:salt_ore","got:salt",1,1F);

        cracked("minecraft:bricks","got:cracked_red_bricks");
        cracked("minecraft:stone_bricks","minecraft:cracked_stone_bricks");
        cracked("got:andesite_bricks","got:cracked_andesite_bricks");
        cracked("got:basalt_bricks","got:cracked_basalt_bricks");
        cracked("got:sandstone_bricks","got:cracked_sandstone_bricks");
        cracked("got:sandstone_red_bricks","got:cracked_sandstone_red_bricks");
        cracked("got:sothoryos_bricks","got:cracked_sothoryos_bricks");
        cracked("got:yi_ti_bricks","got:cracked_yi_ti_bricks");
        cracked("got:labradorite_bricks","got:cracked_labradorite_bricks");
        cracked("got:asshai_basalt_bricks","got:asshai_basalt_bricks_cracked");
    }
    private GOTMachineRecipes(){}

    private static void mill(String in,String out,int count,float chance){
        Item item=item(out); if(item!=Items.AIR) MILL.put(in,new MillResult(new ItemStack(item,count),chance));
    }
    private static void cracked(String in,String out){mill(in,out,1,1F);}
    private static Item item(String id){
        ResourceLocation key=ResourceLocation.tryParse(id); if(key==null)return Items.AIR;
        return ForgeRegistries.ITEMS.getValue(key);
    }
    private static String key(ItemStack s){ResourceLocation id=ForgeRegistries.ITEMS.getKey(s.getItem());return id==null?"":id.toString();}

    public static Optional<MillResult> mill(ItemStack in){
        if(in.isEmpty())return Optional.empty();
        MillResult r=MILL.get(key(in));
        return r==null?Optional.empty():Optional.of(new MillResult(r.stack.copy(),r.chance));
    }
    public static boolean canMill(ItemStack in){return mill(in).isPresent();}

    public static ItemStack alloy(ItemStack upper,ItemStack lower){
        if(upper.isEmpty()||lower.isEmpty())return ItemStack.EMPTY;
        String a=key(upper),b=key(lower);
        if(pair(a,b,"minecraft:copper_ingot","got:tin_ingot") || pairAny(a,b,new String[]{"minecraft:copper_ore","minecraft:raw_copper"},new String[]{"got:tin_ore","got:deepslate_tin_ore","got:raw_tin"}))
            return stack("got:bronze_ingot");
        if(pairAny(a,b,new String[]{"got:cobalt_blue","got:cobalt_ore"},new String[]{"minecraft:iron_ingot","minecraft:iron_ore","minecraft:deepslate_iron_ore","minecraft:raw_iron"}))
            return stack("got:alloy_steel_ingot");
        if(pair(a,b,"got:widow_wail","got:oathkeeper")) return stack("got:ice");
        return ItemStack.EMPTY;
    }
    private static boolean pair(String a,String b,String x,String y){return (a.equals(x)&&b.equals(y))||(a.equals(y)&&b.equals(x));}
    private static boolean pairAny(String a,String b,String[] xs,String[] ys){for(String x:xs)for(String y:ys)if(pair(a,b,x,y))return true;return false;}
    private static ItemStack stack(String id){Item i=item(id);return i==Items.AIR?ItemStack.EMPTY:new ItemStack(i);}

    /** Legacy forge also acted as a restricted furnace; modern bridge delegates actual smelting outputs to the recipe manager. */
    public static ItemStack smelt(Level level,ItemStack input){
        if(level==null||input.isEmpty())return ItemStack.EMPTY;
        SimpleContainer c=new SimpleContainer(input.copyWithCount(1));
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,c,level)
                .map(r->r.assemble(c,level.registryAccess())).orElse(ItemStack.EMPTY);
    }
}

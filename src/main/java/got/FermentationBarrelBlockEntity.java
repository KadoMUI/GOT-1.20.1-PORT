package got;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public final class FermentationBarrelBlockEntity extends BlockEntity implements MenuProvider, Container {
    public static final int SLOT_COUNT=10, RESULT=9, STAGE_TICKS=12000, CAPACITY=16;
    private NonNullList<ItemStack> items=NonNullList.withSize(SLOT_COUNT,ItemStack.EMPTY);
    private int mode, brewTime, strengthStage, servings;
    private String drinkId="";
    final ContainerData data=new ContainerData(){ public int get(int i){ return switch(i){case 0->mode;case 1->brewTime;case 2->strengthStage;case 3->servings;default->0;};} public void set(int i,int v){switch(i){case 0->mode=v;case 1->brewTime=v;case 2->strengthStage=v;case 3->servings=v;}} public int getCount(){return 4;}};
    public FermentationBarrelBlockEntity(BlockPos pos, BlockState state){super(GOTBlockEntities.FERMENTATION_BARREL.get(),pos,state);}
    public static void serverTick(Level level, BlockPos pos, BlockState state, FermentationBarrelBlockEntity b){
        if(b.mode==1){ b.brewTime++; if(b.brewTime>=STAGE_TICKS){b.brewTime=0;b.strengthStage++; if(b.strengthStage>=5){b.strengthStage=5;b.mode=2;} setChanged(level,pos,state);} }
        if(b.mode==0)b.updatePreview();
    }
    private boolean hasWater(){for(int i=6;i<9;i++)if(!items.get(i).is(Items.WATER_BUCKET))return false;return true;}
    private FermentationRecipe currentRecipe(){ if(!hasWater())return null; return FermentationRecipes.find(List.of(items.get(0),items.get(1),items.get(2),items.get(3),items.get(4),items.get(5))); }
    private void updatePreview(){ FermentationRecipe r=currentRecipe(); items.set(RESULT,r==null?ItemStack.EMPTY:r.output()); setChanged(); }
    public boolean startOrStop(){
        if(mode==1){mode=0;brewTime=0;strengthStage=0;drinkId="";servings=0;updatePreview();return true;}
        FermentationRecipe r=currentRecipe(); if(mode!=0||r==null)return false;
        drinkId=r.outputId(); for(int i=0;i<6;i++)items.get(i).shrink(1); for(int i=6;i<9;i++)items.set(i,new ItemStack(Items.BUCKET));
        items.set(RESULT,ItemStack.EMPTY); mode=1;brewTime=0;strengthStage=1;servings=CAPACITY;setChanged();return true;
    }
    public boolean tryFillVessel(Player player, net.minecraft.world.InteractionHand hand, ItemStack held){
        if(mode!=2||servings<=0||drinkId.isEmpty())return false;
        GOTDrinkVessel vessel=null;
        for(GOTDrinkVessel v:GOTDrinkVessel.values())if(ItemStack.isSameItemSameTags(held,v.emptyStack())){vessel=v;break;}
        if(vessel==null)return false;
        Item item=net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse(drinkId));
        if(!(item instanceof GOTDrinkItem drink))return false;
        ItemStack filled=drink.createFilled(vessel,Math.max(.25F,strengthStage*.4F)); held.shrink(1);
        if(held.isEmpty())player.setItemInHand(hand,filled); else if(!player.getInventory().add(filled))player.drop(filled,false);
        servings--; if(servings<=0){mode=0;drinkId="";strengthStage=0;brewTime=0;updatePreview();} setChanged(); return true;
    }
    @Override public Component getDisplayName(){return Component.translatable("container.got.fermentation_barrel");}
    @Nullable @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player p){return new FermentationBarrelMenu(id,inv,this,data);}
    @Override protected void saveAdditional(CompoundTag tag){super.saveAdditional(tag);ContainerHelper.saveAllItems(tag,items);tag.putInt("Mode",mode);tag.putInt("BrewTime",brewTime);tag.putInt("Strength",strengthStage);tag.putInt("Servings",servings);tag.putString("Drink",drinkId);}
    @Override public void load(CompoundTag tag){super.load(tag);items=NonNullList.withSize(SLOT_COUNT,ItemStack.EMPTY);ContainerHelper.loadAllItems(tag,items);mode=tag.getInt("Mode");brewTime=tag.getInt("BrewTime");strengthStage=tag.getInt("Strength");servings=tag.getInt("Servings");drinkId=tag.getString("Drink");}
    public int getContainerSize(){return SLOT_COUNT;} public boolean isEmpty(){return items.stream().allMatch(ItemStack::isEmpty);} public ItemStack getItem(int i){return items.get(i);} public ItemStack removeItem(int i,int n){ItemStack r=ContainerHelper.removeItem(items,i,n);setChanged();return r;} public ItemStack removeItemNoUpdate(int i){return ContainerHelper.takeItem(items,i);} public void setItem(int i,ItemStack s){if(mode!=0&&i<9)return;items.set(i,s);setChanged();if(mode==0)updatePreview();} public boolean stillValid(Player p){return level!=null&&level.getBlockEntity(worldPosition)==this&&p.distanceToSqr(worldPosition.getX()+.5,worldPosition.getY()+.5,worldPosition.getZ()+.5)<=64;} public void clearContent(){items.clear();}
    @Override public boolean canPlaceItem(int slot,ItemStack stack){if(mode!=0||slot==RESULT)return false;return slot>=6?stack.is(Items.WATER_BUCKET):true;}
}

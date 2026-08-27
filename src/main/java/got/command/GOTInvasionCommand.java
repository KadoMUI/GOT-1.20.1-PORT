package got.command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import got.GOTMod;
import got.invasion.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid=GOTMod.MOD_ID,bus=Mod.EventBusSubscriber.Bus.FORGE)
public final class GOTInvasionCommand {
 private GOTInvasionCommand(){}
 @SubscribeEvent public static void register(RegisterCommandsEvent e){e.getDispatcher().register(Commands.literal("invasion").requires(s->s.hasPermission(2)).then(Commands.argument("type",StringArgumentType.word()).executes(c->start(c.getSource(),StringArgumentType.getString(c,"type"),c.getSource().getPosition().x,c.getSource().getPosition().y,c.getSource().getPosition().z,-1)).then(Commands.argument("pos",BlockPosArgument.blockPos()).executes(c->{var p=BlockPosArgument.getLoadedBlockPos(c,"pos");return start(c.getSource(),StringArgumentType.getString(c,"type"),p.getX(),p.getY(),p.getZ(),-1);}).then(Commands.argument("size",IntegerArgumentType.integer(1,10000)).executes(c->{var p=BlockPosArgument.getLoadedBlockPos(c,"pos");return start(c.getSource(),StringArgumentType.getString(c,"type"),p.getX(),p.getY(),p.getZ(),IntegerArgumentType.getInteger(c,"size"));})))));}
 private static int start(CommandSourceStack s,String raw,double x,double y,double z,int size){var type=GOTInvasionType.byName(raw);if(type.isEmpty()){s.sendFailure(Component.translatable("got.command.invasion.noType",raw));return 0;}ServerLevel level=s.getLevel();var id=GOTInvasionData.get(level).start(level,type.get(),new net.minecraft.core.BlockPos((int)Math.floor(x),(int)Math.floor(y),(int)Math.floor(z)),size);s.sendSuccess(()->Component.translatable("got.command.invasion.start",Component.translatable("got.invasion."+type.get().codeName()),size<0?"30-70":Integer.toString(size),id.toString()),true);return 1;}
}

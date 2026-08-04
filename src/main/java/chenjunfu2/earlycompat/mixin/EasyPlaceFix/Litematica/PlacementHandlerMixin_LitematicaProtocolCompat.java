package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Litematica;

import chenjunfu2.earlycompat.network.EarlyCompatS2ClientHandler;
import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import fi.dy.masa.litematica.util.PlacementHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static chenjunfu2.earlycompat.config.LitematicaEarlyCompatConfigs.EASY_PLACE_V2_PROTOCOL_EXTRA;
import static chenjunfu2.earlycompat.util.EasyPlaceExtraProtocolHelper.*;

@Mixin(PlacementHandler.class)
@Environment(EnvType.CLIENT)
public abstract class PlacementHandlerMixin_LitematicaProtocolCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/litematica/util/PlacementHandler;applyPlacementProtocolV2(Lnet/minecraft/block/BlockState;Lfi/dy/masa/litematica/util/PlacementHandler$UseContext;)Lnet/minecraft/block/BlockState;",
		cancellable = true,
		at = @At("HEAD")
	)
	private static void replaceExtraProtocol(BlockState state, PlacementHandler.UseContext context, CallbackInfoReturnable<BlockState> cir)
	{
		if(!EarlyCompatS2ClientHandler.isServerSupportsExtraProtocol() || !EASY_PLACE_V2_PROTOCOL_EXTRA.getBooleanValue())//当前服务器未开启扩展协议或客户端没开启扩展协议
		{
			return;
		}
		
		Block block = state.getBlock();
		double relativeHitX = getRelativeHitX(context.getHitVec(), context.getPos());
		
		//最低bit0留给浮点误差兼容，protocolValue已进行摘除处理
		int protocolValue = decodeProtocolValueFromHitDim(relativeHitX);
		if(!isExtraProtocol(protocolValue))
		{
			return;//不是扩展协议
		}
		
		//只处理扩展协议内已知的方块
		if(!(block instanceof BlockProtocolStateAdapter blockProtocolStateAdapter))
		{
			return;//不是已知方块，跳过处理，有可能是其它mixin的协议
		}
		
		if(blockProtocolStateAdapter.earlycompat$getProtocolType() == BlockProtocolStateAdapter.ProtocolType.REPLACE)
		{
			int rawProtocolValue = extraProtocolValueToRawProtocolValue(protocolValue);
			cir.setReturnValue(blockProtocolStateAdapter.earlycompat$fromProtocolValue(rawProtocolValue, state, context.getItemPlacementContext()));
			cir.cancel();
		}
		else if(blockProtocolStateAdapter.earlycompat$getProtocolType() == BlockProtocolStateAdapter.ProtocolType.ADDED)
		{
			cir.setReturnValue(blockProtocolStateAdapter.earlycompat$fromProtocolValue(protocolValue, state, context.getItemPlacementContext()));//使用原值，不解包
			cir.cancel();
		}
	}
	
}

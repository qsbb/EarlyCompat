package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.enums.Orientation;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(JigsawBlock.class)
public abstract class JigsawBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		Object orientation = fromState.get(JigsawBlock.ORIENTATION);
		return orientation != null ? orientation.hashCode() & 0b0000_1111 : 0;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int orientationOrdinal = (extraProtocolValue & 0b0000_1111) % 12;//0~11
		return fromState.with(JigsawBlock.ORIENTATION, Orientation.values()[orientationOrdinal % 12]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
}

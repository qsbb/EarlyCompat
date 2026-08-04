package chenjunfu2.earlycompat.mixin.EasyPlaceFix.CrafterEarly;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import chenjunfu2.earlycompat.util.ItemStackProtocolDataAdapter;
import net.chenjunfu2.block.CrafterBlock;
import net.minecraft.block.BlockState;

import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.block.enums.Orientation;
import net.minecraft.state.property.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(CrafterBlock.class)
public abstract class CrafterBlockMixin_CrafterEarlyProtocolCompat implements BlockProtocolStateAdapter, ItemStackProtocolDataAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		Object orientation = fromState.get(Properties.ORIENTATION);
		int orientationOrdinal = orientation != null ? orientation.hashCode() & 0b0000_1111 : 0;
		return orientationOrdinal;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		//低4bit存储12个方向
		int orientationOrdinal = (extraProtocolValue & 0b0000_1111) % 12;//0~11
		return fromState.with(Properties.ORIENTATION, Orientation.values()[orientationOrdinal % 12]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.REPLACE;
	}
	
	@Override
	public int earlycompat$toProtocolValueAddition(ItemStack fromStack)
	{
		// In 1.21, ItemStack NBT is replaced by components.
		// Disabled slots on crafter items are no longer readable from the item stack NBT in the same way.
		return 0;
	}
	
	@Override
	public @NotNull ItemStack earlycompat$fromProtocolValueAddition(int extraProtocolValue, ItemStack fromStack)
	{
		int dis_count = Integer.bitCount(extraProtocolValue & 0b0001_1111_1111);//9bit
		if(dis_count == 0)
		{
			return fromStack;//啥都没有
		}
		
		// In 1.21, we cannot easily set legacy BlockEntityTag NBT back onto the item stack.
		// Return the original stack to avoid breaking item data.
		return fromStack;
	}
}

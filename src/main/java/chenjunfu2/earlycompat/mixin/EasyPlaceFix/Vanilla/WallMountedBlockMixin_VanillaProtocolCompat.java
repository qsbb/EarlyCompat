package chenjunfu2.earlycompat.mixin.EasyPlaceFix.Vanilla;

import chenjunfu2.earlycompat.util.BlockProtocolStateAdapter;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WallMountedBlock.class)
public abstract class WallMountedBlockMixin_VanillaProtocolCompat implements BlockProtocolStateAdapter
{
	@Override
	public int earlycompat$toProtocolValue(int protocolValue, BlockState fromState)
	{
		BlockFace face = fromState.get(WallMountedBlock.FACE);
		int faceOrdinal = face != null ? face.ordinal() : 0;
		int bits = (faceOrdinal << 4);
		return protocolValue | bits;
	}
	
	@Override
	public @Nullable BlockState earlycompat$fromProtocolValue(int extraProtocolValue, BlockState fromState, ItemPlacementContext context)
	{
		int faceOridinal = ((extraProtocolValue & 0b0011_0000) >>> 4) % 3;//0~2
		return fromState.with(WallMountedBlock.FACE, BlockFace.values()[faceOridinal]);
	}
	
	@Override
	public @NotNull ProtocolType earlycompat$getProtocolType()
	{
		return ProtocolType.ADDED;
	}
}

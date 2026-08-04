package chenjunfu2.earlycompat.mixin.InventoryPreviewFix.Malilib;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryUtils.class)
@Environment(EnvType.CLIENT)
public abstract class InventoryUtilsMixin_MalilibVanillaCompat
{
	@Inject
	(
		method = "Lfi/dy/masa/malilib/util/InventoryUtils;getStoredItems(Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/util/collection/DefaultedList;",
		at = @At
		(
			value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/nbt/NbtCompound;getCompound(Ljava/lang/String;)Lnet/minecraft/nbt/NbtCompound;"
		),
		cancellable = true
	)
	private static void processSingleItem0(ItemStack stackIn, int slotCount, CallbackInfoReturnable<DefaultedList<ItemStack>> cir, @Local(name = "tagBlockEntity") NbtCompound tagBlockEntity)
	{
		var item = getSingleItem(tagBlockEntity);
		if(item == null)
		{
			return;
		}
		
		cir.setReturnValue(item);
		cir.cancel();
	}
	
	@Inject
	(
		method = "Lfi/dy/masa/malilib/util/InventoryUtils;getStoredItems(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/collection/DefaultedList;",
		at = @At
		(
			value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/nbt/NbtCompound;getCompound(Ljava/lang/String;)Lnet/minecraft/nbt/NbtCompound;"
		),
		cancellable = true
	)
	private static void processSingleItem1(ItemStack stackIn, CallbackInfoReturnable<DefaultedList<ItemStack>> cir, @Local(name = "tagBlockEntity") NbtCompound tagBlockEntity)
	{
		var item = getSingleItem(tagBlockEntity);
		if(item == null)
		{
			return;
		}
		
		cir.setReturnValue(item);
		cir.cancel();
	}
	
	
	private static DefaultedList<ItemStack> getSingleItem(NbtCompound tagBlockEntity)
	{
		// In 1.21, ItemStack.fromNbt was removed. Fall back to null so the preview code can handle it.
		return null;
	}
	
}

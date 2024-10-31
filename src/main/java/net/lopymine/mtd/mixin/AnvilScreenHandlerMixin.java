package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.utils.ItemStackUtils;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;"), method = "updateResult")
	private Text swapItemName(ItemStack itemStack, Operation<Text> original) {
		Text text = ItemStackUtils.getItemStackCustomName(itemStack);
		if (text == null) {
			return original.call(itemStack);
		}
		return text;
	}

}

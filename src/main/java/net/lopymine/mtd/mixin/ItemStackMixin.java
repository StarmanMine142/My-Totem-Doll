package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipData;


import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.gui.tooltip.TagsTooltipData;
import net.lopymine.mtd.tag.manager.TagTotemDollManager;
import net.lopymine.mtd.utils.ItemStackUtils;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract boolean isOf(Item item);

	@ModifyReturnValue(at = @At("RETURN"), method = "getName")
	private Text getName(Text original) {
		String string = original.getString();
		if (!string.contains("|")) {
			return original;
		}
		String[] data = TagTotemDollManager.getDataFromName(string);
		String name = data[0];
		String tags = data[1];
		if (tags == null) {
			return original;
		}
		return Text.literal(name).setStyle(original.getStyle());
	}

	@ModifyReturnValue(at = @At("RETURN"), method = "getTooltipData")
	private Optional<TooltipData> getTooltipData(Optional<TooltipData> original) {
		if (!this.isOf(Items.TOTEM_OF_UNDYING)) {
			return original;
		}
		Text text = ItemStackUtils.getItemStackCustomName((ItemStack) (Object) this);
		if (text == null) {
			return original;
		}
		String string = text.getString();
		if (!string.contains("|")) {
			return original;
		}
		String[] data = TagTotemDollManager.getDataFromName(string);
		String tags = data[1];
		if (tags == null) {
			return original;
		}
		return Optional.of(new TagsTooltipData(tags, original));
	}

}

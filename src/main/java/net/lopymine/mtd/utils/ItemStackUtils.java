package net.lopymine.mtd.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

public class ItemStackUtils {

	@Nullable
	public static Text getItemStackCustomName(ItemStack itemStack) {
		return /*? if >=1.20.5 {*/itemStack.get(DataComponentTypes.CUSTOM_NAME); /*?} else {*/ /*stack.hasCustomName() ? stack.getName() : null; *//*?}*/
	}

}

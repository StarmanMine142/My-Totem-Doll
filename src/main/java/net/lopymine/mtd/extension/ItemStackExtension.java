package net.lopymine.mtd.extension;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import net.lopymine.mtd.utils.mixin.*;

import org.jetbrains.annotations.Nullable;

public class ItemStackExtension {

	@Nullable
	public static Text getRealCustomName(ItemStack itemStack) {
		return /*? if >=1.20.5 {*/itemStack.get(DataComponentTypes.CUSTOM_NAME); /*?} else {*/ /*stack.hasCustomName() ? stack.getName() : null; *//*?}*/
	}

	public static void setModdedModel(ItemStack itemStack, boolean modded) {
		((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$setModdedModel(modded);
	}

	public static boolean hasModdedModel(ItemStack itemStack) {
		return ((ItemStackWithModdedBakedModel) itemStack).myTotemDoll$isModdedModel();
	}

	public static void setPlayerEntity(ItemStack itemStack, AbstractClientPlayerEntity playerEntity) {
		((ItemStackWithPlayerEntity) itemStack).myTotemDoll$setPlayerEntity(playerEntity);
	}

	public static AbstractClientPlayerEntity getPlayerEntity(ItemStack itemStack) {
		return ((ItemStackWithPlayerEntity) itemStack).myTotemDoll$getPlayerEntity();
	}

}

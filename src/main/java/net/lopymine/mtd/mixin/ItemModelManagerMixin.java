package net.lopymine.mtd.mixin;

//? >=1.21.4 {
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.utils.ItemRenderStateWithStack;
import net.lopymine.mtd.utils.abc.Badabums;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemModelManager.class)
public class ItemModelManagerMixin {

	@Inject(at = @At("HEAD"), method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V")
	private void capture(ItemRenderState renderState, ItemStack stack, ModelTransformationMode transformationMode, World world, LivingEntity entity, int seed, CallbackInfo ci) {
		((ItemRenderStateWithStack) renderState).myTotemDoll$setStack(stack);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"), method = "update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V")
	private Object wrap(ItemStack stack, ComponentType<?> componentType, Operation<?> original) {
		String string = stack.getName().getString();
		if (Badabums.badabumbsss(string)) {
			stack.setModdedModel(true);
			return MyTotemDoll.id("something_mtd");
		}
		return original.call(stack, componentType);
	}

}

//?}

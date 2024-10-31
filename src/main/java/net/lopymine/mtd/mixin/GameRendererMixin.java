package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.mixin.accessor.*;

import org.jetbrains.annotations.Nullable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	//? if >=1.21 {

	@Shadow
	@Nullable
	private ItemStack floatingItem;

	@Shadow @Final private MinecraftClient client;

	@SuppressWarnings("deprecation")
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;draw(Ljava/lang/Runnable;)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(DrawContext drawContext, Runnable drawCallback, Operation<Void> original, @Local MatrixStack matrices) {
		if (MyTotemDollClient.getConfig().isModEnabled() && this.floatingItem != null && this.floatingItem.isOf(Items.TOTEM_OF_UNDYING)) {
			drawContext.draw(() -> {
				TotemDollRenderer.renderFloatingDoll(matrices, ((BuiltinModelItemRendererAccessor) ((ItemRendererAccessor) this.client.getItemRenderer()).getBuiltinModelItemRenderer()).getEntityModelLoader(), this.floatingItem, drawContext.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV);
			});
		} else {
			original.call(drawContext, drawCallback);
		}
	}

	//?} else {

	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;IILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;I)V"), method = "renderFloatingItem")
	private void renderFloatingDoll(ItemRenderer itemRenderer, ItemStack stack, ModelTransformationMode transformationType, int light, int overlay, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int seed, Operation<Void> original) {
		if (MyTotemDollClient.getConfig().isModEnabled() && stack != null && stack.isOf(Items.TOTEM_OF_UNDYING)) {
			TotemDollRenderer.renderFloatingDoll(matrices, ((BuiltinModelItemRendererAccessor) ((ItemRendererAccessor) itemRenderer).getBuiltinModelItemRenderer()).getEntityModelLoader(), stack, vertexConsumers, light, overlay);
		} else {
			original.call(itemRenderer, stack, transformationType, light, overlay, matrices, vertexConsumers, world, seed);
		}
	}

	*///?}
}


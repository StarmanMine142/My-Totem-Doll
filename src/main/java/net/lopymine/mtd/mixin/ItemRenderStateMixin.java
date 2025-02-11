package net.lopymine.mtd.mixin;

//? if >=1.21.4 {
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.utils.ItemRenderStateWithStack;

import org.jetbrains.annotations.Nullable;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemRenderState.class)
public class ItemRenderStateMixin implements ItemRenderStateWithStack {

	@Shadow ModelTransformationMode modelTransformationMode;
	@Shadow boolean leftHand;

	@Unique
	@Nullable
	private ItemStack stack;

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		if (this.stack != null && TotemDollRenderer.rendered(matrices, this.stack, this.modelTransformationMode, this.leftHand, vertexConsumers, light, overlay)) {
			ci.cancel();
		}
		if (this.stack != null && this.stack.hasModdedModel()) {
			this.stack.setModdedModel(false);
		}
		this.stack = null;
	}

	@Override
	public void myTotemDoll$setStack(ItemStack stack) {
		this.stack = stack;
	}
}

//?}
package net.lopymine.mtd.mixin;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.utils.abc.Badabums;

@ExtensionMethod(ItemStackExtension.class)
@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Shadow
	@Final
	private ItemModels models;

	@Inject(at = @At(value = "HEAD"), method = "getModel", cancellable = true)
	private void renderDoll(ItemStack stack, World world, LivingEntity entity, int seed, CallbackInfoReturnable<BakedModel> cir) {
		if (!MyTotemDollClient.getConfig().isModEnabled() || !stack.isOf(Items.TOTEM_OF_UNDYING)) {
			return;
		}

		String string = stack.getName().getString();
		if (Badabums.badabumbsss(string)) {
			BakedModel model = this.models.getModelManager().getModel(MyTotemDoll.id("item/something_mtd"));
			stack.setModdedModel(true);
			cir.setReturnValue(model);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", cancellable = true)
	private void renderDoll(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		if (MyTotemDollClient.getConfig().isModEnabled() && stack.isOf(Items.TOTEM_OF_UNDYING) && !stack.hasModdedModel()) {
			if (TotemDollRenderer.renderDoll(matrices, stack, renderMode, leftHanded, vertexConsumers, light, overlay)) {
				ci.cancel();
			}
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", cancellable = true)
	private void disableModdedModel(ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo ci) {
		if (stack.hasModdedModel()) {
			stack.setModdedModel(false);
		}
	}
}

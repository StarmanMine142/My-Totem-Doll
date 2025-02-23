package net.lopymine.mtd.doll.renderer;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.*;
import net.minecraft.text.Text;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.rendering.*;
import net.lopymine.mtd.config.totem.TotemDollSkinType;
import net.lopymine.mtd.doll.data.*;
import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.doll.model.TotemDollModel.Drawer;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.doll.manager.*;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.ProfilerUtils;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.*;
import org.jetbrains.annotations.Nullable;


@ExtensionMethod(ItemStackExtension.class)
public class TotemDollRenderer {

	public static boolean rendered(MatrixStack matrices, ItemStack stack, ModelTransformationMode modelTransformationMode, boolean leftHanded, VertexConsumerProvider vertexConsumers, int light, int uv) {
		if (canRender(stack)) {
			return TotemDollRenderer.renderDoll(matrices, stack, modelTransformationMode, leftHanded, vertexConsumers, light, uv);
		}
		return false;
	}

	public static boolean renderFloatingDoll(MatrixStack matrices, ItemStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		beforeDollRendered(null, stack);

		TotemDollData totemDollData = stack.getTotemDollData();

		if (StandardTotemDollManager.getStandardDoll().equals(totemDollData)) {
			if (MyTotemDollClient.getConfig().isUseVanillaTotemModel()) {
				return false;
			}
			TotemDollRenderer.prepareStandardDollForRendering(stack, totemDollData);
		}

		matrices.push();
		matrices.translate(-0.5F, -1.0F, -0.5F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		matrices.pop();

		afterDollRendered(totemDollData);
		return true;
	}

	public static boolean renderDoll(MatrixStack matrices, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ModelTransformationMode mode = renderMode == ModelTransformationMode.NONE ? ModelTransformationMode.GUI : renderMode;

		beforeDollRendered(mode, stack);

		TotemDollData totemDollData = stack.getTotemDollData();

		if (StandardTotemDollManager.getStandardDoll().equals(totemDollData)) {
			if (MyTotemDollClient.getConfig().isUseVanillaTotemModel()) {
				return false;
			}

			prepareStandardDollForRendering(stack, totemDollData);
		}

		matrices.push();
		totemDollData.getModel().getMain().getTransformation().getTransformation(mode).apply(leftHanded, matrices);
		matrices.translate(-0.5F, -1.0F, -0.5F);

		switch (mode) {
			case FIRST_PERSON_LEFT_HAND,
			     FIRST_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(leftHanded, true, matrices, vertexConsumers, light, overlay, totemDollData);
			case THIRD_PERSON_LEFT_HAND,
			     THIRD_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(leftHanded, false, matrices, vertexConsumers, light, overlay, totemDollData);
			default -> TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		}

		matrices.pop();

		afterDollRendered(totemDollData);
		return true;
	}

	public static void renderPreview(DrawContext context, int x, int y, int width, int height, float size, @Nullable TotemDollData data) {
		MatrixStack matrices = context.getMatrices();
		Immediate consumers = context.vertexConsumers;

		float i = (size / 2F);
		float k = (i / 2F);
		int centerX = x + (width / 2);
		int centerY = y + (height / 2);

		long currentTime = Util.getMeasuringTimeMs();
		float rotationSpeed = 0.05f;

		float rotation = (currentTime * rotationSpeed) % 360;

		if (data != null) {
			context.draw();
			DiffuseLighting.disableGuiDepthLighting();

			matrices.push();
			matrices.translate(centerX, centerY, 300F);

			matrices.translate(0F, k, 0F);

			matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(180F));
			matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(rotation));
			matrices.scale(-i, i, i);

			matrices.translate(-0.5F, -0.5F, -0.5F);
			TotemDollRenderer.render(matrices, consumers, 15728880, OverlayTexture.DEFAULT_UV, data);

			context.draw();

			DiffuseLighting.enableGuiDepthLighting();

			matrices.pop();
		} else {
			float v = i / 16;
			float d = i / 2;
			matrices.push();
			matrices.translate(centerX - d, centerY - d, 400F);
			matrices.translate(d, d, 0F);
			matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(rotation));
			matrices.translate(-d, -d, 0F);
			matrices.scale(v, v, v);
			matrices.translate(0F, 0F, -150F); // I hate this
			context.drawItemWithoutEntity(Items.TOTEM_OF_UNDYING.getDefaultStack(), 0, 0);
			matrices.pop();
		}
	}

	public static void renderInHand(boolean leftHanded, boolean firstPerson, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
		matrices.push();

		if (firstPerson) {
			MyTotemDollConfig config = MyTotemDollClient.getConfig();
			RenderingConfig renderingConfig = config.getRenderingConfig();
			HandRenderingConfig handRenderingConfig = leftHanded ? renderingConfig.getLeftHandConfig() : renderingConfig.getRightHandConfig();

			matrices.translate((handRenderingConfig.getOffsetZ() / 100F) * (leftHanded ? 1 : -1), handRenderingConfig.getOffsetY() / 100F, handRenderingConfig.getOffsetX() / 100F);

			matrices.translate(0.5F, 0.5F, 0.5F);

			double scale = handRenderingConfig.getScale();
			matrices.scale((float) scale, (float) scale, (float) scale);
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) handRenderingConfig.getRotationX()));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) handRenderingConfig.getRotationY() * (leftHanded ? -1 : 1)));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) handRenderingConfig.getRotationZ() * (leftHanded ? -1 : 1)));

			matrices.translate(-0.5F, -0.5F, -0.5F);
		}

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		matrices.pop();
	}

	public static void render(MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, TotemDollData totemDollData) {
		TotemDollTextures textures = totemDollData.getRenderTextures();
		Identifier capeTexture = textures.getCapeTexture();
		Identifier skinTexture = textures.getSkinTexture();
		TotemDollModel model = totemDollData.getModel();

		String nickname = totemDollData.getNickname();

		if (nickname != null && (nickname.equals("dinnerbone") || nickname.equals("grumm"))) {
			matrices.translate(0.5F, 1.5F, 0.5F);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
			matrices.translate(-0.5F, -1.5F, -0.5F);
		}

		matrices.push();
		matrices.translate(0.5F, 0.5F, 0.5F);
		matrices.scale(-1.0F, -1.0F, 1.0F); // 0 - -
		matrices.translate(-0.5F, -0.5F, -0.5F);

		Drawer drawer = model.getDrawer();

		if (nickname != null && nickname.equals("deadmau5")) {
			drawer.texture("ears", skinTexture);
			drawer.requestDrawingPart("ears");
		}

		if (capeTexture != null) {
			drawer.texture("cape", textures::getCapeTexture);
			drawer.requestDrawingPart("cape");
		}

		drawer.texture("elytra", textures::getElytraTexture);
		drawer.requestDrawingPart("elytra");

		drawer.draw(matrices, provider, skinTexture, light, overlay, /*? if >=1.21 {*/ /*-1 *//*?} else {*/ 1.0F, 1.0F, 1.0F, 1.0F /*?}*/);

//		if (LoadedMods.EARS_LOADED) {
//			TotemDollEarsRenderer renderer = TotemDollEarsRenderer.getInstance();
//			renderer.render(matrices, provider, totemDollData, light, overlay);
//		}

		matrices.pop();
	}

	private static void prepareStandardDollForRendering(ItemStack stack, TotemDollData totemDollData) {
		AbstractClientPlayerEntity playerEntity = stack.getPlayerEntity();
		if ((playerEntity != null) && MyTotemDollClient.getConfig().getStandardTotemDollSkinType() == TotemDollSkinType.HOLDING_PLAYER) {
			totemDollData.setCurrentTempTextures(TotemDollTextures.of(playerEntity));
			totemDollData.getModel().setSlim(totemDollData.getRenderTextures().getArmsType().isSlim()); // TODO add property when you can override slim and when can't
		}
	}

	private static void beforeDollRendered(@Nullable ModelTransformationMode modelTransformationMode, ItemStack stack) {
		Profiler profiler = ProfilerUtils.getProfiler();
		profiler.swap(MyTotemDoll.MOD_ID);
		if (modelTransformationMode == ModelTransformationMode.GUI) {
			stack.setPlayerEntity(MinecraftClient.getInstance().player);
		}
	}

	private static void afterDollRendered(TotemDollData totemDollData) {
		totemDollData.clearCurrentTempModel();
		totemDollData.clearCurrentTempTextures();
	}

	public static boolean canRender(@Nullable ItemStack stack) {
		return MyTotemDollClient.getConfig().isModEnabled() && stack != null && stack.isOf(Items.TOTEM_OF_UNDYING) && !stack.hasModdedModel();
	}
}

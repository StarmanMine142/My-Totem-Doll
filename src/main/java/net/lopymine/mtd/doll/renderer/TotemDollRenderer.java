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

//? if >=1.20.5 {
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.profiler.Profiler;

import org.jetbrains.annotations.Nullable;
//?}

@ExtensionMethod(ItemStackExtension.class)
public class TotemDollRenderer {

	public static boolean renderFloatingDoll(MatrixStack matrices, ItemStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Profiler profiler = MinecraftClient.getInstance().getProfiler();

		profiler.swap(MyTotemDoll.MOD_ID);

		TotemDollData totemDollData = TotemDollRenderer.parseTotemDollData(stack);

		if (StandardTotemDollManager.getStandardDoll().equals(totemDollData)) {
			if (MyTotemDollClient.getConfig().isUseVanillaTotemModel()) {
				return false;
			}
			TotemDollRenderer.prepareStandardDollForRendering(stack, totemDollData);
		}

		matrices.push();
		matrices.translate(-0.5F, -0.5F, -0.5F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		matrices.pop();

		return true;
	}

	public static boolean renderDoll(MatrixStack matrices, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		Profiler profiler = MinecraftClient.getInstance().getProfiler();
		profiler.swap(MyTotemDoll.MOD_ID);

		TotemDollData totemDollData = TotemDollRenderer.parseTotemDollData(stack);

		if (StandardTotemDollManager.getStandardDoll().equals(totemDollData)) {
			if (MyTotemDollClient.getConfig().isUseVanillaTotemModel()) {
				return false;
			}

			prepareStandardDollForRendering(stack, totemDollData);
		}

		matrices.push();
		totemDollData.getModel().getMain().getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
		matrices.translate(-0.5F, -1.0F, -0.5F);

		switch (renderMode) {
			case FIRST_PERSON_LEFT_HAND,
			     FIRST_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(leftHanded, true, matrices, vertexConsumers, light, overlay, totemDollData);
			case THIRD_PERSON_LEFT_HAND,
			     THIRD_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(leftHanded, false, matrices, vertexConsumers, light, overlay, totemDollData);
			default -> TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
		}

		matrices.pop();

		return true;
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

	public static void renderOnGround(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
//		matrices.push();
//
//		matrices.translate(0.5F, 0.5F, 0.5F);
//		matrices.scale(0.38F, 0.38F, 0.38F);
//		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
//		matrices.translate(-0.5F, -0.5F, -0.5F);

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
//
//		matrices.pop();
	}

	public static void renderFixed(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
//		matrices.push();
//
//		matrices.translate(0.5F, 0.5F, 0.5F);
//		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
//		matrices.translate(-0.5F, -0.5F, -0.5F);
//		TotemDollModel model = totemDollData.getModel();
//		matrices.translate(0.0F, 0.8F, totemDollData.getTextures().getCapeTexture() == null || !model.getCape().visible ? -0.3F : -0.18F);
//		model.getCape().pitch = 0.2F;

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);

//		matrices.pop();
	}

	public static void renderInHead(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
//		matrices.push();
//		matrices.translate(0.0F, 0.0F, -0.2F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);
//		matrices.pop();
	}

	public static void renderInGui(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
//		matrices.push();
//
//		float yOffset = totemDollData.getTextures().getCapeTexture() != null ? 0.37F : 0.3F;
//		float xOffset = 0.25F;
//		String nickname = totemDollData.getNickname();
//		if (nickname != null && nickname.equals("deadmau5")) {
//			yOffset = 0.2F;
//			xOffset = 0.3F;
//		}
//		matrices.translate(xOffset, yOffset, 0.25F);
//		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(20));
//		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(10));
//		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(20));
//		matrices.scale(0.5F, 0.5F, 0.5F);
//		matrices.translate(-0.1F, -yOffset, -0.25F);

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);

//		matrices.pop();
	}

	public static void renderAsFloating(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollData totemDollData) {
		//matrices.push();

//		matrices.translate(0.5F, 0.5F, 0.5F);
//		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F));
//		matrices.translate(-0.5F, -0.5F, -0.5F);
//		matrices.scale(0.8F, 0.8F, 0.8F);
//		matrices.translate(0.1F, 0.0F, 0.0F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, totemDollData);

		//matrices.pop();
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

			drawer.texture("elytra", textures::getCapeTexture);
			drawer.requestDrawingPart("elytra");
		}

		drawer.draw(matrices, provider, skinTexture, light, overlay, /*? if >=1.21 {*/ -1 /*?} else {*/ /*1.0F, 1.0F, 1.0F, 1.0F *//*?}*/);

//		if (LoadedMods.EARS_LOADED) {
//			TotemDollEarsRenderer renderer = TotemDollEarsRenderer.getInstance();
//			renderer.render(matrices, provider, totemDollData, light, overlay);
//		}

		matrices.pop();

		totemDollData.clearCurrentTempModel();
		totemDollData.clearCurrentTempTextures();
	}

	public static void renderPreview(DrawContext context, int x, int y, float size, @Nullable TotemDollData data) {
		MatrixStack matrices = context.getMatrices();
		Immediate consumers = context.getVertexConsumers();

		float i = (size / 2F);
		long currentTime = Util.getMeasuringTimeMs();
		float rotationSpeed = 0.05f;

		float rotation = (currentTime * rotationSpeed) % 360;

		if (data != null) {
			DiffuseLighting.disableGuiDepthLighting();

			matrices.push();
			matrices.translate(x + i, y + (i * 2), 300F);
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
			matrices.translate(x + d, y + d, 0);
			matrices.translate(d, d, 400F);
			matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(rotation));
			matrices.translate(-d, -d, 0);
			matrices.scale(v, v, v);
			matrices.translate(0, 0, -150F); // f**k this line please
			context.drawItemWithoutEntity(Items.TOTEM_OF_UNDYING.getDefaultStack(), 0, 0);
			matrices.pop();
		}
	}

	public static TotemDollData parseTotemDollData(ItemStack stack) {
		Text customName = stack.getRealCustomName();

		if (customName != null) {
			String o = TagsManager.getNicknameOrSkinProviderFromName(customName.getString());
			TotemDollData data = TotemDollManager.getDoll(o);
			TotemDollTextures textures = data.getTextures();
			TotemDollModel model = data.getModel();

			model.apply(textures);

			String tags = TagsManager.getTagsFromName(customName.getString());
			if (tags != null) {
				TagsManager.processTags(tags, data);
			}

			return data;
		}

		return StandardTotemDollManager.getStandardDoll();
	}

	private static void prepareStandardDollForRendering(ItemStack stack, TotemDollData totemDollData) {
		AbstractClientPlayerEntity playerEntity = stack.getPlayerEntity();
		if (playerEntity != null && MyTotemDollClient.getConfig().getStandardTotemDollSkinType() == TotemDollSkinType.HOLDING_PLAYER) {
			totemDollData.setCurrentTempTextures(TotemDollTextures.of(playerEntity.getSkinTextures()));
			totemDollData.getModel().apply(totemDollData.getRenderTextures());
		} else {
			totemDollData.getModel().apply(totemDollData.getRenderTextures());
		}
	}
}

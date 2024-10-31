package net.lopymine.mtd.doll.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.manager.TotemDollManager;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.rendering.*;
import net.lopymine.mtd.doll.data.*;
import net.lopymine.mtd.tag.manager.TagTotemDollManager;
import net.lopymine.mtd.utils.ItemStackUtils;

//? if >=1.20.5 {
import net.minecraft.util.profiler.Profiler;
		//?}

public class TotemDollRenderer {

	public static void renderFloatingDoll(MatrixStack matrices, EntityModelLoader entityModelLoader, ItemStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(-0.5F, -0.5F, -0.5F);

		TotemDollData totemDollData = TotemDollRenderer.parseTotemDollData(stack, entityModelLoader);
		TotemDollModel model = totemDollData.getModel(entityModelLoader);

		TotemDollRenderer.renderAsFloating(matrices, vertexConsumers, light, overlay, model, totemDollData);

		matrices.pop();
	}

	public static void renderDoll(MatrixStack matrices, EntityModelLoader entityModelLoader, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
		Profiler profiler = MinecraftClient.getInstance().getProfiler();

		profiler.swap("mytotemdoll");

		matrices.push();

		Transformation transformation = model.getTransformation().getTransformation(renderMode);

		transformation.apply(leftHanded, matrices);
		matrices.translate(-0.5F, -0.5F, -0.5F);

		TotemDollData totemDollData = TotemDollRenderer.parseTotemDollData(stack, entityModelLoader);
		TotemDollModel dollModel = totemDollData.getModel(entityModelLoader);

		switch (renderMode) {
			case GUI -> TotemDollRenderer.renderInGui(matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case HEAD -> TotemDollRenderer.renderInHead(matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case FIXED -> TotemDollRenderer.renderFixed(matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case GROUND -> TotemDollRenderer.renderOnGround(matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case FIRST_PERSON_LEFT_HAND -> TotemDollRenderer.renderInHand(true, true, matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case FIRST_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(false, true, matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case THIRD_PERSON_LEFT_HAND -> TotemDollRenderer.renderInHand(true, false, matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case THIRD_PERSON_RIGHT_HAND -> TotemDollRenderer.renderInHand(false, false, matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
			case NONE -> TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, dollModel, totemDollData);
		}

		matrices.pop();
	}

	private static void renderInHand(boolean leftHanded, boolean firstPerson, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		float xOffset;
		float yOffset;
		float zOffset;

		if (firstPerson) {
			xOffset = 1.75F;
			yOffset = 0.3F;
			zOffset = -1.7F;
			float zRotation = 20F;

			MyTotemDollConfig config = MyTotemDollClient.getConfig();
			RenderingConfig renderingConfig = config.getRenderingConfig();
			HandRenderingConfig handRenderingConfig = leftHanded ? renderingConfig.getLeftHandConfig() : renderingConfig.getRightHandConfig();

			matrices.push();
			matrices.translate((leftHanded ? xOffset : -xOffset), yOffset, zOffset);
			matrices.translate((handRenderingConfig.getOffsetZ() / 100F) * (leftHanded ? 1 : -1), handRenderingConfig.getOffsetY() / 100F, handRenderingConfig.getOffsetX() / 100F);
			matrices.translate(0.5F, 0.35F, 0.5F);
			matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees((leftHanded ? -zRotation : zRotation)));

			double scale = handRenderingConfig.getScale();
			matrices.scale((float) scale, (float) scale, (float) scale);

			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) handRenderingConfig.getRotationX()));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) handRenderingConfig.getRotationY() * (leftHanded ? -1 : 1)));
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float) handRenderingConfig.getRotationZ() * (leftHanded ? -1 : 1)));
			matrices.translate(-0.5F, -0.35F, -0.5F);

		} else {
			xOffset = 0.18F;
			yOffset = 0.35F;
			zOffset = 0.25F;

			matrices.push();

			matrices.translate(xOffset, yOffset, zOffset);
			matrices.scale(0.65F, 0.65F, 0.65F);
		}

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);
		matrices.pop();
	}

	private static void renderOnGround(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		matrices.push();

		matrices.translate(0.1F, 0.2F, 0.85F);
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
		matrices.scale(0.7F, 0.7F, 0.7F);

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);

		matrices.pop();
	}

	private static void renderFixed(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		matrices.push();

		matrices.translate(0.0F, 0.0F, 0.8F);
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
		matrices.translate(0.0F, 0.0F, -1.2F);

		model.getCape().pitch = 0.2F;

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);

		matrices.pop();
	}

	public static void renderInHead(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		matrices.push();
		matrices.translate(0.0F, 0.0F, -0.2F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);
		matrices.pop();
	}

	private static void renderInGui(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		//? if >=1.20.5 {
		DiffuseLighting.method_34742();
		//?} else {
		/*Vector3f vec = (new Vector3f(0.0F, 1.0F, 1.0F)).normalize();
		Vector3f vec2 = (new Vector3f(0.0F, 1.0F, 1.0F)).normalize();

		RenderSystem.setShaderLights(vec, vec2);
		*///?}

		matrices.push();

		float yOffset = totemDollData.getTextures().getCapeTexture() != null ? 0.37F : 0.3F;
		float xOffset = 0.25F;
		String nickname = totemDollData.getNickname();
		if (nickname != null && nickname.equals("deadmau5")) {
			yOffset = 0.2F;
			xOffset = 0.3F;
		}
		matrices.translate(xOffset, yOffset, 0.25F);
		matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(20));
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(10));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(20));
		matrices.scale(0.5F, 0.5F, 0.5F);
		matrices.translate(-0.1F, -yOffset, -0.25F);

		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);

		matrices.pop();
	}

	private static void renderAsFloating(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		matrices.push();

		matrices.translate(0.5F, 0.5F, 0.5F);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180F));
		matrices.translate(-0.5F, -0.5F, -0.5F);
		matrices.scale(0.8F, 0.8F, 0.8F);
		matrices.translate(0.1F, 0.0F, 0.0F);
		TotemDollRenderer.render(matrices, vertexConsumers, light, overlay, model, totemDollData);

		matrices.pop();
	}

	private static void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, TotemDollModel model, TotemDollData totemDollData) {
		TotemDollTextures textures = totemDollData.getTextures();
		Identifier capeTexture = textures.getCapeTexture();
		Identifier skinTexture = textures.getSkinTexture();

		String nickname = totemDollData.getNickname();

		if (nickname != null && (nickname.equals("dinnerbone") || nickname.equals("grumm"))) {
			matrices.translate(0.5F, 0.5F, 0.5F);
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180));
			matrices.translate(-0.5F, -0.5F, -0.5F);
		}

		matrices.push();
		matrices.translate(0.5F, 0.0F, 0.5F);
		matrices.scale(1.0F, -1.0F, -1.0F);

		VertexConsumer skinVertexConsumer = vertexConsumers.getBuffer(model.getLayer(skinTexture));
		model.render(matrices, skinVertexConsumer, light, overlay,  /*? if >=1.21 {*/ -1 /*?} else {*/ /*1.0F, 1.0F, 1.0F, 1.0F *//*?}*/);

		if (nickname != null && nickname.equals("deadmau5")) {
			model.renderEars(matrices, skinVertexConsumer, light, overlay, /*? if >=1.21 {*/ -1 /*?} else {*/ /*1.0F, 1.0F, 1.0F, 1.0F *//*?}*/);
		}

		if (capeTexture != null) {
			VertexConsumer capeVertexConsumer = vertexConsumers.getBuffer(model.getLayer(capeTexture));
			model.renderCape(matrices, capeVertexConsumer, light, overlay,  /*? if >=1.21 {*/ -1 /*?} else {*/ /*1.0F, 1.0F, 1.0F, 1.0F *//*?}*/);
		}

		matrices.pop();
	}

	private static TotemDollData parseTotemDollData(ItemStack stack, EntityModelLoader loader) {
		Text customName = ItemStackUtils.getItemStackCustomName(stack);

		if (customName != null) {
			String o = TagTotemDollManager.getNicknameOrSkinProviderFromName(customName.getString());
			TotemDollData totemDollData = TotemDollManager.getDoll(o);
			TotemDollTextures textures = totemDollData.getTextures();
			TotemDollModel model = totemDollData.getModel(loader);

			model.apply(textures);

			String tags = TagTotemDollManager.getTagsFromName(customName.getString());
			if (tags != null) {
				TagTotemDollManager.processTags(tags, model);
			}

			return totemDollData;
		}
		
		return TotemDollManager.DEFAULT_DOLL;
	}
}

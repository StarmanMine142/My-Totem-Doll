package net.lopymine.mtd.doll.model;

import lombok.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

import net.lopymine.mtd.doll.data.TotemDollTextures;

@Getter
@Setter
public class TotemDollModel extends Model {

	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart leftArmSlim;
	private final ModelPart rightArmSlim;
	private final ModelPart leftArmDefault;
	private final ModelPart rightArmDefault;
	private final ModelPart leftLeg;
	private final ModelPart rightLeg;
	private final ModelPart cape;
	private final ModelPart ears;
	private boolean slim;

	public TotemDollModel(ModelPart root, boolean slim) {
		super(RenderLayer::getEntityTranslucent);
		this.head            = root.getChild("head");
		this.body            = root.getChild("body");
		this.leftArmSlim     = root.getChild("leftArmSlim");
		this.rightArmSlim    = root.getChild("rightArmSlim");
		this.leftArmDefault  = root.getChild("leftArmDefault");
		this.rightArmDefault = root.getChild("rightArmDefault");
		this.leftLeg         = root.getChild("leftLeg");
		this.rightLeg        = root.getChild("rightLeg");
		this.cape            = root.getChild("cape");
		this.ears            = this.head.getChild("ear");
		this.slim            = slim;
		this.updatePartsVisibility();
	}

	public static TexturedModelData getDollModel() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();

		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(1.5F)), ModelTransform.pivot(0.0F, -9.6F, 0.0F));
		head.addChild("headwear", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -19.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(2.0F)), ModelTransform.pivot(0.0F, 9.0F, 0.0F));

		head.addChild("ear", ModelPartBuilder.create().uv(24, 0).cuboid(-3.0F, -3.0F, -0.5F, 6.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -13.0F, -0.5F));

		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -10.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		body.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, -10.0F, -2.0F, 8.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		// Steve Arms
		ModelPartData leftArm = modelPartData.addChild("leftArmDefault", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -1.0075F, -1.727F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -8.9358F, 0.4288F, -0.6939F, -0.084F, -0.1005F));
		leftArm.addChild("leftSleveDefault", ModelPartBuilder.create().uv(48, 48).cuboid(0.0F, -9.6604F, -8.4279F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, 8.653F, 6.7009F));

		ModelPartData rightArm = modelPartData.addChild("rightArmDefault", ModelPartBuilder.create().uv(40, 16).cuboid(-2.9F, -1.014F, -1.727F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-5.0F, -8.9358F, 0.4288F, -0.6939F, 0.084F, 0.1005F));
		rightArm.addChild("rightSleveDefault", ModelPartBuilder.create().uv(40, 32).cuboid(-4.0F, -9.6604F, -8.4279F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.1F, 8.6464F, 6.7009F));
		//

		// Alex Arms
		ModelPartData leftArmSlim = modelPartData.addChild("leftArmSlim", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -1.0075F, -1.727F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(5.0F, -8.9358F, 0.4288F, -0.6939F, -0.084F, -0.1005F));
		leftArmSlim.addChild("leftSleveSlim", ModelPartBuilder.create().uv(48, 48).cuboid(0.0F, -9.6604F, -8.4279F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(-1.0F, 8.653F, 6.7009F));

		ModelPartData rightArmSlim = modelPartData.addChild("rightArmSlim", ModelPartBuilder.create().uv(40, 16).cuboid(-2.9F, -1.014F, -1.727F, 3.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-4.0F, -8.9358F, 0.4288F, -0.6939F, 0.084F, 0.1005F));
		rightArmSlim.addChild("rightSleveSlim", ModelPartBuilder.create().uv(40, 32).cuboid(-4.0F, -9.6604F, -8.4279F, 3.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.1F, 8.6464F, 6.7009F));
		//

		ModelPartData leftLeg = modelPartData.addChild("leftLeg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.1838F, -1.0914F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(2.0F, 1.0F, 0.0F, -1.3788F, -0.3927F, 0.0F));
		leftLeg.addChild("leftPants", ModelPartBuilder.create().uv(0, 48).cuboid(-3.0F, 0.0F, -12.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(1.0F, 0.1838F, 10.9086F));

		ModelPartData rightLeg = modelPartData.addChild("rightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, 1.0F, 0.0F, -1.3788F, 0.3927F, 0.0F));
		rightLeg.addChild("rightPants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -12.0F, 4.0F, 12.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 11.0F));

		ModelPartData cape = modelPartData.addChild("cape", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -9.0F, 2.5F));
		cape.addChild("cape_", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, -16.0F, -0.5F, 10.0F, 16.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 15.0F, 0.5F, 0.0F, 3.1416F, 0.0F));

		return TexturedModelData.of(modelData, 64, 64);
	}

	public void updatePartsVisibility() {
		this.head.visible            = true;
		this.body.visible            = true;
		this.leftArmSlim.visible     = this.slim;
		this.rightArmSlim.visible    = this.slim;
		this.leftArmDefault.visible  = !this.slim;
		this.rightArmDefault.visible = !this.slim;
		this.leftLeg.visible         = true;
		this.rightLeg.visible        = true;
		this.cape.visible            = true;
		this.ears.visible            = false;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay,
	                   /*? if >=1.21 {*/
	                   int color
	                   /*?} else {*/
			/*float red, float green, float blue, float alpha
			 *//*?}*/
	) {
		this.head.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.body.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.leftArmSlim.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.rightArmSlim.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.leftArmDefault.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.rightArmDefault.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.leftLeg.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
		this.rightLeg.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
	}

	public void renderCape(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay,
	                       /*? if >=1.21 {*/
	                       int color
	                       /*?} else {*/
			/*float red, float green, float blue, float alpha
			 *//*?}*/
	) {
		this.cape.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
	}

	public void renderEars(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay,
	                       /*? if >=1.21 {*/
	                       int color
	                       /*?} else {*/
			/*float red, float green, float blue, float alpha
			 *//*?}*/
	) {
		this.ears.visible = true;

		for (int n = 0; n < 2; ++n) {
			matrices.push();
			matrices.translate(0.48F * (float) (n * 2 - 1), -0.375F, 0.0F);
			matrices.scale(1.3333334F, 1.3333334F, 1.3333334F);
			this.ears.render(matrices, vertexConsumer, light, overlay, /*? if >=1.21 {*/ color /*?} else {*/ /*red, green, blue, alpha *//*?}*/);
			matrices.pop();
		}
	}

	public void apply(TotemDollTextures textures) {
		this.slim = textures.isSlim();
		this.updatePartsVisibility();
	}
}
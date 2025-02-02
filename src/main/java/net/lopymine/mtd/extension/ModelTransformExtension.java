package net.lopymine.mtd.extension;

import net.minecraft.client.model.ModelTransform;

public class ModelTransformExtension {

	public static ModelTransform subtract(ModelTransform root, ModelTransform parent) {
		return ModelTransform.of(
				root.pivotX - parent.pivotX,
				root.pivotY - parent.pivotY,
				root.pivotZ - parent.pivotZ,
				root.pitch,
				root.yaw,
				root.roll
		);
	}

	public static ModelTransform getBlockBenchedModelTransform(ModelTransform transform) {
		return ModelTransform.of(-transform.pivotX, -transform.pivotY, transform.pivotZ, transform.pitch, transform.yaw, transform.roll);
	}

	public static String asString(ModelTransform transform) {
		return "%s %s %s | %s %s %s".formatted(transform.pivotX, transform.pivotY, transform.pivotZ, transform.pitch, transform.yaw, transform.roll);
	}

}

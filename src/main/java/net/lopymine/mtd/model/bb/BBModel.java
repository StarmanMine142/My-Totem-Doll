package net.lopymine.mtd.model.bb;

import lombok.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.render.model.json.*;
import net.minecraft.util.Identifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.mtd.config.other.vector.Vec3f;
import net.lopymine.mtd.extension.ModelTransformationExtension;

import java.util.*;
import org.jetbrains.annotations.Nullable;
import static net.lopymine.mtd.utils.CodecUtils.option;
import static net.lopymine.mtd.utils.CodecUtils.optional;

//? >=1.21.2
import net.minecraft.item.ModelTransformationMode;

@Setter
@Getter
@AllArgsConstructor
public class BBModel {

	private Identifier location;
	private String name;
	private BBModelMeta meta;
	private BBModelResolution resolution;
	private List<BBCube> cubes;
	private List<BBGroup> groups;
	private boolean frontGuiLight;
	private ModelTransformation transformation;

	@Nullable
	public BBCube getCube(UUID uuid) {
		for (BBCube cube : this.cubes) {
			if (cube.getUuid().equals(uuid)) {
				return cube;
			}
		}
		return null;
	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class BBModelMeta {

		public static final Codec<BBModelMeta> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				option("format_version", Codec.STRING, BBModelMeta::getVersion),
				option("model_format", Codec.STRING, BBModelMeta::getModel)
		).apply(inst, BBModelMeta::new));

		private String version;
		private String model;

	}

	@Setter
	@Getter
	@AllArgsConstructor
	public static class BBModelResolution {

		public static final Codec<BBModelResolution> CODEC = RecordCodecBuilder.create(inst -> inst.group(
				option("width", Codec.INT, BBModelResolution::getWidth),
				option("height", Codec.INT, BBModelResolution::getHeight)
		).apply(inst, BBModelResolution::new));

		private int width;
		private int height;

	}

	@ExtensionMethod(ModelTransformationExtension.class)
	public static final class Transformations {

		private static final Vec3f DEFAULT_ROTATION = new Vec3f(0.0F, 0.0F, 0.0F);
		private static final Vec3f DEFAULT_TRANSLATION = new Vec3f(0.0F, 0.0F, 0.0F);
		private static final Vec3f DEFAULT_SCALE = new Vec3f(1.0F, 1.0F, 1.0F);

		public static final Codec<Transformation> TRANSFORMATION_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				optional("rotation", DEFAULT_ROTATION, Vec3f.CODEC, (o) -> new Vec3f(o.rotation)),
				optional("translation", DEFAULT_TRANSLATION, Vec3f.CODEC, (o) -> new Vec3f(o.translation)),
				optional("scale", DEFAULT_SCALE, Vec3f.CODEC, (o) -> new Vec3f(o.scale))
		).apply(instance, Transformations::prepareTransformation));

		private static Transformation prepareTransformation(Vec3f rotation, Vec3f translation, Vec3f scale) {
			translation.mul(0.0625F);
			return new Transformation(rotation, translation, scale);
		}

		public static final Codec<ModelTransformation> MODEL_TRANSFORMATION_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				optional(ModelTransformationMode.THIRD_PERSON_LEFT_HAND.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getTl()),
				optional(ModelTransformationMode.THIRD_PERSON_RIGHT_HAND.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getTr()),
				optional(ModelTransformationMode.FIRST_PERSON_LEFT_HAND.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFl()),
				optional(ModelTransformationMode.FIRST_PERSON_RIGHT_HAND.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFr()),
				optional(ModelTransformationMode.HEAD.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getHead()),
				optional(ModelTransformationMode.GUI.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getGui()),
				optional(ModelTransformationMode.GROUND.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getGround()),
				optional(ModelTransformationMode.FIXED.asString(), Transformation.IDENTITY, TRANSFORMATION_CODEC, (o) -> o.getFixed())
		).apply(instance, ModelTransformation::new));

	}
}

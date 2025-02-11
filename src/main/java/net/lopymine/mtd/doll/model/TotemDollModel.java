package net.lopymine.mtd.doll.model;

import lombok.*;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.data.TotemDollTextures;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;
import java.util.*;
import java.util.function.*;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class TotemDollModel extends Model {

	public static final Identifier STANDARD_DOLL_ID = MyTotemDoll.id("dolls/standard_doll.bbmodel");

	private final MModel main;

	@Nullable
	private final MModel
			head,
			body,
			leftArmSlim,
			rightArmSlim,
			leftArmWide,
			rightArmWide,
			leftLeg,
			rightLeg;

	@Nullable
	private final MModel
			cape,
			elytra,
			ears;

	private boolean slim;

	private Drawer drawer;

	public TotemDollModel(MModel root, boolean slim) {
		super(/*? >=1.21.2 {*/ root, /*?}*/RenderLayer::getEntityTranslucent);

		this.head         = root.findModel("head").orElse(null);
		this.body         = root.findModel("body").orElse(null);
		this.leftArmSlim  = root.findModel("left_arm_slim").orElse(null);
		this.rightArmSlim = root.findModel("right_arm_slim").orElse(null);
		this.leftArmWide  = root.findModel("left_arm_wide").orElse(null);
		this.rightArmWide = root.findModel("right_arm_wide").orElse(null);
		this.leftLeg      = root.findModel("left_leg").orElse(null);
		this.rightLeg     = root.findModel("right_leg").orElse(null);

		this.cape   = root.findModel("cape").orElse(null);
		this.elytra = root.findModel("elytra").orElse(null);
		this.ears   = Optional.ofNullable(this.head).flatMap((model) -> model.findModel("ears")).orElse(null);

		this.main = root;
		this.slim = slim;

		this.disableIfPresent(this.leftArmSlim);
		this.disableIfPresent(this.rightArmSlim);
		this.disableIfPresent(this.leftArmWide);
		this.disableIfPresent(this.rightArmWide);

		this.resetPartsVisibility();
	}

	public static MModel createDollModel() {
		MModel model = BlockBenchModelManager.getModel(MyTotemDollClient.getConfig().getStandardTotemDollModelValue());
		return model == null ? BlockBenchModelManager.getModel(STANDARD_DOLL_ID) : model;
	}

	public void enableIfPresent(@Nullable MModel model) {
		if (model != null) {
			model.visible = true;
		}
	}

	public void disableIfPresent(@Nullable MModel model) {
		if (model != null) {
			model.visible = false;
		}
	}

	public void enableSkipRenderingIfPresent(@Nullable MModel model) {
		if (model != null) {
			model.setSkipRendering(true);
		}
	}

	public void disableSkipRenderingIfPresent(@Nullable MModel model) {
		if (model != null) {
			model.setSkipRendering(false);
		}
	}

	public void resetPartsVisibility() {
		this.enableSkipRenderingIfPresent(this.cape);
		this.enableIfPresent(this.cape);
		this.enableSkipRenderingIfPresent(this.ears);
		this.enableIfPresent(this.ears);
		this.enableSkipRenderingIfPresent(this.elytra);
		this.disableIfPresent(this.elytra);
	}

	//? <=1.21.1 {

	/*@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, /^? if >=1.21 {^/ int color /^?} else {^/ /^float r, float g, float b, float a^//^?}^/) {
		// NO-OP
	}

	*///?}

	public void apply(TotemDollTextures textures) {
		this.slim = textures.getArmsType().isSlim();
		this.resetPartsVisibility();
	}

	@Nullable
	public MModel getLeftArm() {
		return this.slim ? this.leftArmSlim : this.leftArmWide;
	}

	@Nullable
	public MModel getRightArm() {
		return this.slim ? this.rightArmSlim : this.rightArmWide;
	}

	public Drawer getDrawer() {
		if (this.drawer == null) {
			this.drawer = new Drawer(this);
		}
		this.drawer.prepareForRender();
		return this.drawer;
	}

	public static class Drawer {

		private final Map<String, Supplier<Identifier>> textures = new HashMap<>();
		private final Set<String> requestedParts = new HashSet<>();

		private final Function<Identifier, RenderLayer> layerFunction;
		private final TotemDollModel model;

		public Drawer(TotemDollModel model) {
			this.model         = model;
			this.layerFunction = model::getLayer;
		}

		public Drawer texture(String part, Identifier texture) {
			this.textures.put(part, () -> texture);
			return this;
		}

		public Drawer texture(String part, Supplier<Identifier> texture) {
			this.textures.put(part, texture);
			return this;
		}

		public Drawer requestDrawingPart(String part) {
			this.requestedParts.add(part);
			return this;
		}

		public void draw(MatrixStack matrices, VertexConsumerProvider provider, Identifier mainTexture, int light, int overlay, /*? if >=1.21 {*/int color/*?} else {*//*float red, float green, float blue, float alpha *//*?}*/) {
			MModel leftArm = this.model.getLeftArm();
			MModel rightArm = this.model.getRightArm();

			this.model.enableIfPresent(leftArm);
			this.model.enableIfPresent(rightArm);

			this.model.getMain().draw(matrices, provider, this.layerFunction, mainTexture, this.textures, this.requestedParts, light, overlay, /*? if >=1.21 {*/color/*?} else {*/ /*red, green, blue, alpha*//*?}*/);

			this.model.disableIfPresent(leftArm);
			this.model.disableIfPresent(rightArm);

			this.textures.clear();
		}

		public void prepareForRender() {
			this.textures.clear();
			this.requestedParts.clear();
		}
	}
}
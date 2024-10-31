package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.*;

import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.doll.model.layer.TotemDollModelLayers;

import org.jetbrains.annotations.*;

@Getter
@Setter
public class TotemDollData {

	@Nullable
	private String nickname;
	@Nullable
	private TotemDollModel model;
	@NotNull
	private TotemDollTextures textures;

	public TotemDollData(@Nullable String nickname, @NotNull TotemDollTextures textures) {
		this.nickname = nickname;
		this.textures = textures;
	}

	public static TotemDollData create(@Nullable String nickname) {
		return new TotemDollData(nickname, TotemDollTextures.create());
	}

	public TotemDollModel getModel(EntityModelLoader modelLoader) {
		if (this.model != null) {
			return this.model;
		}
		ModelPart modelPart = modelLoader.getModelPart(TotemDollModelLayers.DOLL_MODEL_DATA);
		this.model = new TotemDollModel(modelPart, this.textures.isSlim());
		return this.model;
	}
}

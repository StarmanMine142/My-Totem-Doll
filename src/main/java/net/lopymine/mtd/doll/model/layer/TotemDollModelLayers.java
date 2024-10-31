package net.lopymine.mtd.doll.model.layer;

import net.minecraft.client.render.entity.model.EntityModelLayer;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.model.TotemDollModel;

public final class TotemDollModelLayers {

	public static final EntityModelLayer DOLL_MODEL_DATA = new EntityModelLayer(MyTotemDoll.id("doll_model_layer"), "doll_layer");

	public static void register() {
		EntityModelLayerRegistry.registerModelLayer(DOLL_MODEL_DATA, TotemDollModel::getDollModel);
	}
}

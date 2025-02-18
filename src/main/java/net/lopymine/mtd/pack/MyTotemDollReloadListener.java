package net.lopymine.mtd.pack;

import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.resource.*;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;
import net.lopymine.mtd.tag.manager.TagsManager;

public class MyTotemDollReloadListener implements SimpleSynchronousResourceReloadListener {

	public static void register() {
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new MyTotemDollReloadListener());
	}

	@Override
	public Identifier getFabricId() {
		return MyTotemDoll.id("reload_listener");
	}

	@Override
	public void reload(ResourceManager resourceManager) {
		BlockBenchModelManager.reload();
		TotemDollModelFinder.reload(resourceManager);
		TagsManager.reloadCustomModelIdsTags();
	}
}

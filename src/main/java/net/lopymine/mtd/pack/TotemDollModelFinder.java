package net.lopymine.mtd.pack;

import net.minecraft.resource.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;

import java.util.*;

public class TotemDollModelFinder {

	private static final Map<String, Set<Identifier>> FOUNDED_TOTEM_MODELS = new LinkedHashMap<>();

	public static Map<String, Set<Identifier>> getFoundedTotemModels() {
		return FOUNDED_TOTEM_MODELS;
	}

	public static void find(ResourceManager resourceManager) {
		List<ResourcePack> list = resourceManager.streamResourcePacks().filter(resourcePack -> resourcePack.getNamespaces(ResourceType.CLIENT_RESOURCES).contains(MyTotemDoll.MOD_ID)).toList();

		FOUNDED_TOTEM_MODELS.clear();
		for (ResourcePack pack : list) {
			pack.findResources(ResourceType.CLIENT_RESOURCES, MyTotemDoll.MOD_ID, "dolls", (id, input) -> {
				if (!isModelPath(id)) {
					return;
				}

				String packId = pack.getId().replace("file/", "");

				Set<Identifier> set = FOUNDED_TOTEM_MODELS.getOrDefault(packId, new LinkedHashSet<>());
				set.add(id);

				if (!FOUNDED_TOTEM_MODELS.containsKey(packId)) {
					FOUNDED_TOTEM_MODELS.put(packId, set);
				}
			});
		}
	}

	private static boolean isModelPath(Identifier id) {
		return id.getPath().endsWith(".bbmodel");
	}
}

package net.lopymine.mtd.manager;

import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.data.TotemDollTextures.State;
import net.lopymine.mtd.skin.provider.MojangSkinProvider;
import net.lopymine.mtd.skin.provider.SkinProvider;
import net.lopymine.mtd.tag.manager.TagSkinProviderManager;
import net.lopymine.mtd.utils.Regexes;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TotemDollManager {

	public static final TotemDollData DEFAULT_DOLL = getDefaultDoll();

	private static TotemDollData getDefaultDoll() {
		TotemDollData totemDollData = TotemDollData.create(null);
		totemDollData.getTextures().setState(State.DOWNLOADED);
		return totemDollData;
	}

	public static TotemDollData getDoll(String nickname) {
		if (TagSkinProviderManager.isProvider(nickname)) {
			return TagSkinProviderManager.loadDollFromProvider(nickname);
		}
		if (!nickname.matches(Regexes.MINECRAFT_NICKNAME_REGEX)) {
			return DEFAULT_DOLL;
		}
		return MojangSkinProvider.getInstance().getDoll(nickname);
	}

	public static Set<String> getAllLoaded() {
		Set<String> loaded = new HashSet<>();

		for (SkinProvider value : TagSkinProviderManager.getSkinProvidersIds().values()) {
			loaded.addAll(value.getLoadedKeys());
		}

		loaded.addAll(MojangSkinProvider.getInstance().getLoadedKeys());

		return loaded;
	}

	public static void reload(Consumer<Float> action) {
		List<SkinProvider> providers = new ArrayList<>(TagSkinProviderManager.getSkinProvidersIds().values());
		providers.add(MojangSkinProvider.getInstance());

		Set<CompletableFuture<?>> list = new HashSet<>();
		long startMs = System.currentTimeMillis();

		for (SkinProvider provider : providers) {
			list.add(provider.reloadAll());
		}

		CompletableFuture.allOf(list.toArray(new CompletableFuture[0])).thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}

	public static void reload(String value, Consumer<Float> action) {
		long startMs = System.currentTimeMillis();

		SkinProvider skinProvider = TagSkinProviderManager.getProviderFor(value);

		CompletableFuture<Void> completableFuture =
				skinProvider == null
						?
						(
								value.matches(Regexes.MINECRAFT_NICKNAME_REGEX)
								?
								MojangSkinProvider.getInstance().reload(value)
								:
								null
						)
						:
						skinProvider.reload(value);

		if (completableFuture == null) {
			return;
		}

		completableFuture.thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}
}

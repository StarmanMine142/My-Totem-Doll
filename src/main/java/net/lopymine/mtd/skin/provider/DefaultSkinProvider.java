package net.lopymine.mtd.skin.provider;

import lombok.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.api.*;
import net.lopymine.mtd.doll.data.*;
import net.lopymine.mtd.doll.data.TotemDollTextures.State;
import net.lopymine.mtd.skin.data.ParsedSkinData;
import net.lopymine.mtd.utils.TextureUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public abstract class DefaultSkinProvider implements SkinProvider {

	private final Map<String, TotemDollData> cache = new HashMap<>();

	private boolean maxRequestsCheckEnabled;
	private int requestsCount = 0;
	private long lastRequestTime = 0L;

	protected DefaultSkinProvider(boolean maxRequestsCheckEnabled) {
		this.maxRequestsCheckEnabled = maxRequestsCheckEnabled;
	}

	@Override
	@NotNull
	public TotemDollData getDoll(String value) {
		TotemDollData totemDollData = this.cache.getOrDefault(value, this.createNewDoll(value));

		if (totemDollData.getTextures().canStartDownloading()) {
			this.loadDoll(value, this.maxRequestsCheckEnabled);
		}

		return totemDollData;
	}

	private CompletableFuture<Void> loadDoll(String value, boolean checkMaxRequests) {
		if (checkMaxRequests) {
			// Max 10 requests per second
			long now = System.currentTimeMillis();
			if (now - this.lastRequestTime > 1000) {
				this.requestsCount   = 0;
				this.lastRequestTime = now;
			}
			if (this.requestsCount >= 10) {
				return CompletableFuture.completedFuture(null);
			}
			this.requestsCount++;
		}

		TotemDollData totemDollData = Optional.ofNullable(this.cache.get(value))
				.orElseGet(() -> {
					TotemDollData data = this.createNewDoll(value);

					boolean b = this.cache.put(value, data) == null;
					return b ? data : null;
				});

		if (totemDollData == null){
			return CompletableFuture.completedFuture(null);
		}

		totemDollData.getTextures().setState(State.WAITING_DOWNLOADING);

		return CompletableFuture.runAsync(() -> {
			int waitTime = 0;

			while (true) {
				TotemDollTextures textures = totemDollData.getTextures();

				textures.setState(State.DOWNLOADING);

				Response<ParsedSkinData> response = this.loadDollFromAPI(value);
				if (response.value() == null) {
					State state = switch (response.statusCode()) {
						case 404 -> State.NOT_FOUND; // Not Found
						case 429 -> State.ERROR; // Too many requests
						default -> State.CRITICAL_ERROR;
					};

					if (state == State.ERROR) { // Too many requests, we can retry
						try {
							waitTime += 1000;
							Thread.sleep(waitTime);
							continue;
						} catch (Exception e) {
							textures.setState(State.CRITICAL_ERROR);
							return;
						}
					}

					textures.setState(state);
					return;
				}

				textures.setState(State.REGISTERING);

				ParsedSkinData parsedSkinData = response.value();
				if (parsedSkinData.getSkinUrl() == null) {
					textures.setState(State.CRITICAL_ERROR);
					return;
				}

				textures.setSlim(parsedSkinData.isSlim());

				Identifier skinId = this.getSkinId(value);

				Runnable onFailed = () -> {
					textures.setState(State.CRITICAL_ERROR);
				};
				Runnable onSuccess = () -> {
					textures.setSkinTexture(skinId);
					textures.setState(State.DOWNLOADED);
				};

				TextureUtils.registerUrlTexture(parsedSkinData.getSkinUrl(), skinId, onSuccess, onFailed);

				if (parsedSkinData.getCapeUrl() != null ) {
					Identifier capeId = this.getCapeId(value);
					TextureUtils.registerUrlTexture(parsedSkinData.getCapeUrl(), capeId, () -> {
						textures.setCapeTexture(capeId);
					}, null);
				}

				if (parsedSkinData.getElytraUrl() != null) {
					Identifier elytraId = this.getElytraId(value);
					TextureUtils.registerUrlTexture(parsedSkinData.getElytraUrl(), elytraId, () -> {
						textures.setElytraTexture(elytraId);
					}, null);
				}

				break;
			}
		});
	}

	public void reload(TotemDollData data, Consumer<Float> action) {
		long startMs = System.currentTimeMillis();

		TotemDollTextures textures = data.getTextures();
		textures.destroy();

		loadDoll(data.getNickname(), false).thenApply((__) -> {
			action.accept((System.currentTimeMillis() - startMs) / 1000F);
			return null;
		});
	}

	@Override
	public Set<String> getLoadedKeys() {
		return this.cache.keySet();
	}

	@Override
	public CompletableFuture<Void> reloadAll() {
		Set<CompletableFuture<?>> list = new HashSet<>();

		for (Entry<String, TotemDollData> entry : this.cache.entrySet()) {

			TotemDollTextures textures = entry.getValue().getTextures();
			textures.destroy();

			list.add(loadDoll(entry.getKey(), false));
		}

		return CompletableFuture.allOf(list.toArray(new CompletableFuture[0]));
	}

	@Override
	public CompletableFuture<Void> reload(String value) {
		TotemDollData totemDollData = this.cache.get(value);
		if (totemDollData == null) {
			return CompletableFuture.completedFuture(null);
		}

		TotemDollTextures textures = totemDollData.getTextures();
		textures.destroy();

		return loadDoll(value, false);
	}

	protected abstract Response<ParsedSkinData> loadDollFromAPI(String value);

	protected abstract TotemDollData createNewDoll(String value);

	protected Identifier getSkinId(String value) {
		return this.getId(value, "skin");
	}

	protected Identifier getCapeId(String value) {
		return this.getId(value, "cape");
	}

	protected Identifier getElytraId(String value) {
		return this.getId(value, "elytra");
	}

	protected abstract Identifier getId(String value, String type);
}

package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.*;

@Getter
@Setter
public class TotemDollTextures {

	public static final Identifier STEVE_SKIN = Identifier.of("minecraft","textures/entity/player/wide/steve.png");

	@NotNull
	private State state = State.NOT_DOWNLOADED;

	@Nullable
	private Identifier skinTexture;
	@Nullable
	private Identifier capeTexture;
	@Nullable
	private Identifier elytraTexture;

	private boolean slim;

	public TotemDollTextures(@Nullable Identifier skinTexture, @Nullable Identifier capeTexture, @Nullable Identifier elytraTexture, boolean slim) {
		this.skinTexture   = skinTexture;
		this.capeTexture   = capeTexture;
		this.elytraTexture = elytraTexture;
		this.slim = slim;
	}

	public static TotemDollTextures create() {
		return new TotemDollTextures(null, null, null, false);
	}

	public @NotNull Identifier getSkinTexture() {
		return this.skinTexture == null || this.state != State.DOWNLOADED ? STEVE_SKIN : this.skinTexture;
	}

	public @Nullable Identifier getCapeTexture() {
		return this.capeTexture;
	}

	public void destroy() {
		this.setState(State.DESTROYED);

		MinecraftClient.getInstance().send(() -> {
			TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

			if (this.skinTexture != null) {
				textureManager.destroyTexture(this.skinTexture);
			}

			if (this.capeTexture != null) {
				textureManager.destroyTexture(this.capeTexture);
			}

			if (this.elytraTexture != null) {
				textureManager.destroyTexture(this.elytraTexture);
			}
		});
	}

	public boolean canStartDownloading() {
		return this.state == State.ERROR || this.state == State.NOT_DOWNLOADED;
	}

	public enum State {
		ERROR, // Y
		CRITICAL_ERROR, // X
		NOT_FOUND, // X
		DESTROYED, // X
		NOT_DOWNLOADED, // Y
		WAITING_DOWNLOADING, // X
		DOWNLOADING, // X
		REGISTERING, // X
		DOWNLOADED // X
	}
}

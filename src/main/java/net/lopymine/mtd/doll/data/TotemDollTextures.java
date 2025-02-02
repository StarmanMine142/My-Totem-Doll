package net.lopymine.mtd.doll.data;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.config.totem.TotemDollArmsType;

import org.jetbrains.annotations.*;

@Getter
@Setter
public class TotemDollTextures {

	public static final Identifier STEVE_SKIN = Identifier.of("minecraft", "textures/entity/player/wide/steve.png");

	@NotNull
	private LoadingState state = LoadingState.NOT_DOWNLOADED;

	@Nullable
	private Identifier skinTexture;
	@Nullable
	private Identifier capeTexture;
	@Nullable
	private Identifier elytraTexture;

	private TotemDollArmsType armsType;

	public TotemDollTextures(@Nullable Identifier skinTexture, @Nullable Identifier capeTexture, @Nullable Identifier elytraTexture, TotemDollArmsType armsType) {
		this.skinTexture   = skinTexture;
		this.capeTexture   = capeTexture;
		this.elytraTexture = elytraTexture;
		this.armsType      = armsType;
	}

	public static TotemDollTextures create() {
		return new TotemDollTextures(null, null, null, TotemDollArmsType.WIDE);
	}

	public static TotemDollTextures of(SkinTextures skinTextures) {
		TotemDollTextures totemDollTextures = new TotemDollTextures(skinTextures.texture(), skinTextures.capeTexture(), skinTextures.elytraTexture(), TotemDollArmsType.of(skinTextures.model().getName()));
		totemDollTextures.setState(LoadingState.DOWNLOADED);
		return totemDollTextures;
	}

	public @NotNull Identifier getSkinTexture() {
		return this.skinTexture == null || this.state != LoadingState.DOWNLOADED ? STEVE_SKIN : this.skinTexture;
	}

	public void destroy() {
		this.setState(LoadingState.DESTROYED);

		Identifier skinTexture = this.skinTexture;
		Identifier capeTexture = this.capeTexture;
		Identifier elytraTexture = this.elytraTexture;

		this.skinTexture = null;
		this.capeTexture = null;
		this.elytraTexture = null;

		MinecraftClient.getInstance().send(() -> {
			TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

			if (skinTexture != null) {
				textureManager.destroyTexture(skinTexture);
			}

			if (capeTexture != null) {
				textureManager.destroyTexture(capeTexture);
			}

			if (elytraTexture != null) {
				textureManager.destroyTexture(elytraTexture);
			}
		});
	}

	public boolean canStartDownloading() {
		return this.state == LoadingState.ERROR || this.state == LoadingState.NOT_DOWNLOADED;
	}

	public TotemDollTextures copy() {
		TotemDollTextures totemDollTextures = new TotemDollTextures(this.skinTexture, this.capeTexture, this.elytraTexture, this.armsType);
		totemDollTextures.setState(this.state);
		return totemDollTextures;
	}
}

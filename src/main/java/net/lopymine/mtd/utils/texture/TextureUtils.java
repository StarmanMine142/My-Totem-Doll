package net.lopymine.mtd.utils.texture;

import lombok.experimental.ExtensionMethod;


import net.minecraft.util.Identifier;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.extension.PlayerSkinTextureExtension;

import java.io.*;
import java.nio.file.*;
import org.jetbrains.annotations.*;

@ExtensionMethod(PlayerSkinTextureExtension.class)
public final class TextureUtils {

	public static void registerUrlTexture(@NotNull String textureUrl, @NotNull Identifier textureId, @Nullable SuccessAction onSuccessRegistration, @Nullable FailedAction onFailedRegistration, boolean cape) {
		Path cachedTexturePath = TextureUtils.getCachedTexturePath(textureUrl);
		if (cachedTexturePath == null) {
			if (onFailedRegistration != null) {
				onFailedRegistration.onFailed("Failed to find and create cache folder", null);
			}
			return;
		}

		PlayerSkinUtils.downloadSkin(textureUrl, textureId, onSuccessRegistration, onFailedRegistration, cape, cachedTexturePath);
	}

	private static @Nullable Path getCachedTexturePath(String textureUrl) {
		Path defCacheFolder = FabricLoader.getInstance().getGameDir().resolve(".cache");
		File cacheFolderFile = defCacheFolder.toFile();
		if (!cacheFolderFile.exists() && !cacheFolderFile.mkdirs()) {
			return null;
		}
		return defCacheFolder.resolve(String.format("%s.png", Math.abs(textureUrl.hashCode())));
	}
}

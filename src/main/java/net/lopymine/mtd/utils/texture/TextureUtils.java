package net.lopymine.mtd.utils.texture;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;


import net.minecraft.client.texture.PlayerSkinTexture;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.mtd.client.MyTotemDollClient;
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

		try (PlayerSkinTexture playerSkinTexture = new PlayerSkinTexture(cachedTexturePath.toFile(), textureUrl, DefaultSkinHelper.getTexture(), true, () -> {
			try {
				if (cachedTexturePath.toFile().exists()) {
					Files.delete(cachedTexturePath);
				}
			} catch (FileSystemException ignored) {
			} catch (FileNotFoundException e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.warn("Failed to find temp texture file at {} to delete it", cachedTexturePath);
				}
			} catch (Exception e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.error("Failed to delete temp texture file: ", e);
				}
			}
		})) {
			playerSkinTexture.setOnSuccessAction(onSuccessRegistration);
			playerSkinTexture.setOnFailedAction(onFailedRegistration);
			if (cape) {
				playerSkinTexture.markAsCape();
			}

			MinecraftClient.getInstance().send(() -> {
				MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, playerSkinTexture);
			});
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to register texture by texture id \"{}\": ", textureId, e);
		}
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

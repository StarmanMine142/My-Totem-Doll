package net.lopymine.mtd.utils.texture;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.extension.PlayerSkinTextureExtension;

import java.io.*;
import java.nio.file.*;
import org.jetbrains.annotations.*;

//? <=1.21.3 {
/*import net.minecraft.client.util.DefaultSkinHelper;
*///?}

@ExtensionMethod(PlayerSkinTextureExtension.class)
public class PlayerSkinUtils {

	public static void downloadSkin(@NotNull String textureUrl, @NotNull Identifier textureId, @Nullable SuccessAction onSuccessRegistration, @Nullable FailedAction onFailedRegistration, boolean cape, Path cachedTexturePath) {
		try {
			AbstractTexture texture = PlayerSkinUtils.download(cachedTexturePath, textureUrl, cape); // DO NOT CLOSE

			//? <=1.21.3 {
			/*if (texture instanceof PlayerSkinTexture playerSkinTexture) {
				playerSkinTexture.setOnSuccessAction(onSuccessRegistration);
				playerSkinTexture.setOnFailedAction(onFailedRegistration);
				if (cape) {
					playerSkinTexture.markAsCape();
				}
			}
			*///?}

			MinecraftClient.getInstance().send(() -> {
				MinecraftClient.getInstance().getTextureManager().registerTexture(textureId, texture);
				//? >=1.21.4 {
				if (onSuccessRegistration != null) {
					onSuccessRegistration.onSuccess();
				}
				//?}
			});
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to download skin texture with id \"%s\": ".formatted(textureId), e);
			if (onFailedRegistration != null) {
				onFailedRegistration.onFailed(e.getMessage(), e);
			}
		}
	}

	private static AbstractTexture download(Path path, String uri, boolean cape) throws IOException {
		//? >=1.21.4 {
		NativeImage download = PlayerSkinTextureDownloader.download(path, uri);
		if (cape) {
			NativeImage nativeImage = new NativeImage(64, 64, true);
			nativeImage.copyFrom(download);
			download.close();
			download = nativeImage;
		} else {
			download = PlayerSkinTextureDownloader.remapTexture(download, uri);
		}
		return new NativeImageBackedTexture(download);
		//?} else {
		/*return new PlayerSkinTexture(path.toFile(), uri, DefaultSkinHelper.getTexture(), true, () -> {
			try {
				if (path.toFile().exists()) {
					Files.delete(path);
				}
			} catch (FileSystemException ignored) {
			} catch (FileNotFoundException e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.warn("Failed to find temp texture file at {} to delete it", path);
				}
			} catch (Exception e) {
				if (MyTotemDollClient.getConfig().isDebugLogEnabled()) {
					MyTotemDollClient.LOGGER.error("Failed to delete temp texture file: ", e);
				}
			}
		});
		*///?}
	}


}

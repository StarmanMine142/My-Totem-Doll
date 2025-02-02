package net.lopymine.mtd.extension;

import net.minecraft.client.texture.PlayerSkinTexture;

import net.lopymine.mtd.utils.mixin.MTDPlayerSkinTexture;
import net.lopymine.mtd.utils.texture.*;

import org.jetbrains.annotations.Nullable;

public class PlayerSkinTextureExtension {

	public static void setOnSuccessAction(PlayerSkinTexture playerSkinTexture, @Nullable SuccessAction successAction) {
		((MTDPlayerSkinTexture) playerSkinTexture).myTotemDoll$setOnSuccessAction(successAction);
	}

	public static void setOnFailedAction(PlayerSkinTexture playerSkinTexture, @Nullable FailedAction failedAction) {
		((MTDPlayerSkinTexture) playerSkinTexture).myTotemDoll$setOnFailedAction(failedAction);
	}

	public static void markAsCape(PlayerSkinTexture playerSkinTexture) {
		((MTDPlayerSkinTexture) playerSkinTexture).myTotemDoll$markAsCape();
	}
}

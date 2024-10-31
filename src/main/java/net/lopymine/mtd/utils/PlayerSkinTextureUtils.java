package net.lopymine.mtd.utils;

import net.minecraft.client.texture.PlayerSkinTexture;

import net.lopymine.mtd.utils.interfaces.mixin.MTDPlayerSkinTexture;

import org.jetbrains.annotations.Nullable;

public class PlayerSkinTextureUtils {

	public static void setOnSuccessAction(PlayerSkinTexture playerSkinTexture, @Nullable Runnable onSuccessRegistration) {
		((MTDPlayerSkinTexture) playerSkinTexture).myTotemDoll$setOnSuccessAction(onSuccessRegistration);
	}

	public static void setOnFailedAction(PlayerSkinTexture playerSkinTexture, @Nullable Runnable onFailedRegistration) {
		((MTDPlayerSkinTexture) playerSkinTexture).myTotemDoll$setOnFailedAction(onFailedRegistration);
	}
}

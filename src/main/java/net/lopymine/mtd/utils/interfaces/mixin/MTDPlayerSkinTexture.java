package net.lopymine.mtd.utils.interfaces.mixin;

import org.jetbrains.annotations.Nullable;

public interface MTDPlayerSkinTexture {

	void myTotemDoll$setOnSuccessAction(@Nullable Runnable onSuccessRegistration);

	void myTotemDoll$setOnFailedAction(@Nullable Runnable onFailedRegistration);

}

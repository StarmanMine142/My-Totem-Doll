package net.lopymine.mtd.utils.mixin;

import net.lopymine.mtd.utils.texture.*;

import org.jetbrains.annotations.Nullable;

public interface MTDPlayerSkinTexture {

	void myTotemDoll$setOnSuccessAction(@Nullable SuccessAction onSuccessRegistration);

	void myTotemDoll$setOnFailedAction(@Nullable FailedAction onFailedRegistration);

	void myTotemDoll$markAsCape();

}

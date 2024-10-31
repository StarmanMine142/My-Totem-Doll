package net.lopymine.mtd.mixin;

import net.minecraft.client.texture.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.utils.interfaces.mixin.MTDPlayerSkinTexture;

import java.io.InputStream;
import org.jetbrains.annotations.Nullable;

@Mixin(PlayerSkinTexture.class)
public class PlayerSkinTextureMixin implements MTDPlayerSkinTexture {

	@Unique
	@Nullable
	private Runnable onSuccessLoading;

	@Unique
	@Nullable
	private Runnable onFailedLoading;

	@Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false), method = "loadTexture")
	private void onFailedLoading1(InputStream stream, CallbackInfoReturnable<NativeImage> cir) {
		if (this.onFailedLoading != null) {
			this.onFailedLoading.run();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V", remap = false), method = "method_22801")
	private void onFailedLoading2(CallbackInfo ci) {
		if (this.onFailedLoading != null) {
			this.onFailedLoading.run();
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V", remap = false), method = "method_22799")
	private void onFailedLoading3(ResourceManager resourceManager, CallbackInfo ci) {
		if (this.onFailedLoading != null) {
			this.onFailedLoading.run();
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "uploadTexture")
	private void onSuccessLoading(NativeImage image, CallbackInfo ci) {
		if (this.onSuccessLoading != null) {
			this.onSuccessLoading.run();
		}
	}

	@Override
	public void myTotemDoll$setOnSuccessAction(@Nullable Runnable runnable) {
		this.onSuccessLoading = runnable;
	}

	@Override
	public void myTotemDoll$setOnFailedAction(@Nullable Runnable runnable) {
		this.onFailedLoading = runnable;
	}
}

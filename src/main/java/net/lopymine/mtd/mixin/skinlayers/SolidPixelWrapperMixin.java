package net.lopymine.mtd.mixin.skinlayers;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import dev.tr7zw.skinlayers.versionless.config.Config;
import dev.tr7zw.skinlayers.versionless.util.wrapper.SolidPixelWrapper;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.client.MyTotemDollClient;

@Pseudo
@Mixin(SolidPixelWrapper.class)
public class SolidPixelWrapperMixin {

	@Dynamic
	@WrapOperation(at = @At(value = "FIELD", target = "Ldev/tr7zw/skinlayers/versionless/config/Config;fastRender:Z"), method = "addPixel", remap = false)
	private static boolean disableFastRenderTwo(Config instance, Operation<Boolean> original) {
		return MyTotemDollClient.getConfig().isDebugLogEnabled();
	}

	@Dynamic
	@WrapOperation(at = @At(value = "FIELD", target = "Ldev/tr7zw/skinlayers/versionless/config/Config;fastRender:Z"), method = "wrapBox", remap = false)
	private static boolean disableFastRenderOne(Config instance, Operation<Boolean> original) {
		return MyTotemDollClient.getConfig().isDebugLogEnabled();
	}

}

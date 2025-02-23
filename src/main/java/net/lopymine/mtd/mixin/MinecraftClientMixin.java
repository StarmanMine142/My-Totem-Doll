package net.lopymine.mtd.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.gui.screen.WelcomeScreen;

import java.util.List;
import java.util.function.Function;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(at = @At("HEAD"), method = "createInitScreens")
	private void generated(List<Function<Runnable, Screen>> list, CallbackInfo ci) {
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		if (config.isFirstRun() || FabricLoader.getInstance().isDevelopmentEnvironment()) {
			list.add(WelcomeScreen::new);
			config.setFirstRun(false);
		}
	}

}

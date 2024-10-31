package net.lopymine.mtd.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.mtd.client.event.MTDEventsManager;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Inject(at = @At("TAIL"), method = "onInitFinished")
	private void onInit(CallbackInfoReturnable<Runnable> cir) {
		MTDEventsManager.registerScreenEvents();
	}

}

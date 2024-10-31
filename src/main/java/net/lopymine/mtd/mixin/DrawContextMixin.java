package net.lopymine.mtd.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.utils.interfaces.TooltipRequest;
import net.lopymine.mtd.utils.interfaces.mixin.*;

import java.util.List;

@Mixin(DrawContext.class)
public class DrawContextMixin {

	@Inject(at = @At("HEAD"), method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;)V", cancellable = true)
	private void cancelTooltipDrawing(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, CallbackInfo ci) {
		Screen currentScreen = MinecraftClient.getInstance().currentScreen;
		if (!(currentScreen instanceof IRequestableTooltipScreen tooltipScreen)) {
			return;
		}
		TooltipRequest currentRequest = tooltipScreen.myTotemDoll$getCurrentRequest();
		if (currentRequest != null) {
			ci.cancel();
		}
	}
}

package net.lopymine.mtd.mixin.yacl.category;

//? >=1.21.4 {

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.yacl.YACLConfigurationScreen;
import net.lopymine.mtd.yacl.custom.TransparencySprites;

import java.util.function.Function;

@Mixin(ScrollableWidget.class)
public class ScrollableWidgetMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0), method = "drawScrollbar")
	private void renderTransparencyScrollerBackground(DrawContext context, Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_BACKGROUND_SPRITE, x, y, width, height);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1), method = "drawScrollbar")
	private void renderTransparencyScroller(DrawContext context, Function<?, ?> function, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen)) {
			original.call(context, function, identifier, x, y, width, height);
			return;
		}
		original.call(context, function, TransparencySprites.SCROLLER_SPRITE, x, y, width, height);
	}

}

//?}

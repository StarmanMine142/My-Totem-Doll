package net.lopymine.mtd.mixin.yacl.widget;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.gui.BackgroundDrawer;
import net.lopymine.mtd.yacl.YACLConfigurationScreen;
import net.lopymine.mtd.yacl.custom.TransparencySprites;

@Pseudo
@Mixin(PressableWidget.class)
public abstract class PressableWidgetMixin extends ClickableWidget implements Drawable {

	@Unique
	private static final String RENDER_METHOD = /*? >=1.20.3 {*/ "renderWidget" /*?} else {*/ /*"renderButton" *//*?}*/;

	public PressableWidgetMixin(int x, int y, int width, int height, Text message) {
		super(x, y, width, height, message);
	}

	//? if >=1.20.2 {

	@Dynamic
	@WrapOperation(method = RENDER_METHOD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"))
	private void renderTransparencyWidget(DrawContext instance, Identifier identifier, int x, int y, int width, int height, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen)) {
			original.call(instance, identifier, x, y, width, height);
			return;
		}
		BackgroundDrawer.drawTransparencyWidgetBackground(instance, x, y, width, height, this.active, this.isSelected());
	}

	//?} else {
	/*@Dynamic
	@WrapOperation(method = RENDER_METHOD, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawNineSlicedTexture(Lnet/minecraft/util/Identifier;IIIIIIIIII)V"))
	private void renderTransparencyWidget1(DrawContext context, Identifier identifier, int x, int y, int w, int h, int a, int b, int c, int d, int e, int i, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen)) {
			original.call(context, identifier, x, y, w, h, a, b, c, d, e, i);
			return;
		}
		RenderSystem.enableBlend();
		context.drawNineSlicedTexture(TransparencySprites.WIDGET_SPRITES.get(this.active, this.isSelected()), x, y, w, h, 2, 256,22,0,0);
		RenderSystem.disableBlend();
	}
	*///?}
}
package net.lopymine.mtd.mixin.yacl.category;

import net.minecraft.client.gui.widget.TabNavigationWidget;
import org.spongepowered.asm.mixin.*;

@Pseudo
@Mixin(TabNavigationWidget.class)
public class TabNavigationWidgetMixin {

	//? if <=1.20.4 {

	/*@Dynamic
	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"), method = "render")
	private void renderTransparencyHeaderSeparator(DrawContext context, Identifier textureId, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, Operation<Void> original) {
		if (YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen)) {
			original.call(context, textureId, x, y, u, v, width, height, textureWidth, textureHeight);
			return;
		}
		RenderSystem.enableBlend();
		context.drawTexture(TransparencySprites.HEADER_SEPARATOR_TEXTURE, x, y, u, x, width, height, textureWidth, textureHeight);
		RenderSystem.disableBlend();
	}

	@Dynamic
	@WrapWithCondition(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), method = "render")
	private boolean disableBlackBackground(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
		return YACLConfigurationScreen.notOpen(MinecraftClient.getInstance().currentScreen);
	}

	*///?}

}

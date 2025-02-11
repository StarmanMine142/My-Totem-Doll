package net.lopymine.mtd.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.util.List;

public class DrawUtils {

	public static void drawTexture(DrawContext context, Identifier sprite, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
		context.drawTexture(/*? >=1.21.2 {*/RenderLayer::getGuiTextured,/*?}*/ sprite, x, y, u, v, width, height, textureWidth, textureHeight);
	}

	public static void drawTooltip(DrawContext context, List<TooltipComponent> list, int x, int y) {
		context.drawTooltip(MinecraftClient.getInstance().textRenderer, list, x, y, HoveredTooltipPositioner.INSTANCE /*? >=1.21.2 {*/,null/*?}*/);
	}

}

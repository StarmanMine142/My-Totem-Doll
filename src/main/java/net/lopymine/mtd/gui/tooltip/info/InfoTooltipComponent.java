package net.lopymine.mtd.gui.tooltip.info;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;


import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;
import net.lopymine.mtd.MyTotemDoll;

public class InfoTooltipComponent implements TooltipComponent {

	public static final Identifier SEPARATOR = MyTotemDoll.id("textures/gui/icon/separator.png");
	public static final int TITLE_COLOR = Argb.getArgb(89, 206, 255);

	private final Text title;
	private final MultilineText text;

	public InfoTooltipComponent(String key) {
		this.title = MyTotemDoll.text("%s.title".formatted(key)).withColor(TITLE_COLOR);
		this.text  = MultilineText.create(MinecraftClient.getInstance().textRenderer, 140, MyTotemDoll.text("%s.text".formatted(key)));
	}

	@Override
	public int getHeight() {
		return (this.text.count() * 10) + 26 + 2 + 5 + 2 + 5;
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		return 150;
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
		int width = this.getWidth(textRenderer);
		int titleWidth = textRenderer.getWidth(this.title);
		context.drawText(textRenderer, this.title, x + (((width) / 2) - (titleWidth / 2)), y + 8, -1, false);
		context.drawTexture(SEPARATOR, x, y + 24, 0, 0, 150, 5, 150, 5);
		this.text.draw(context, x + 5, y + 26 + 2 + 5 + 2, 10, -1);
	}
}

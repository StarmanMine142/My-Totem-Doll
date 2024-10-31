package net.lopymine.mtd.gui.widget;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.utils.pos.PixelInfo;

@Setter
@Getter
public class PixelInfoWidget implements Drawable {

	public static final Identifier BACKGROUND = MyTotemDoll.spriteId("background/gui_background");
	public static final Identifier CELL_BACKGROUND = MyTotemDoll.spriteId("background/cell_background");

	private PixelInfo pixelInfo;

	private int x;
	private int y;
	private int width;
	private int height;

	public PixelInfoWidget(int x, int y, int width, int height) {
		this.x      = x;
		this.y      = y;
		this.width  = width;
		this.height = height;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		context.drawGuiTexture(BACKGROUND, this.x, this.y, this.width, this.height);

		int space = 8;
		int cellSize = this.getHeight() - (space * 2);
		int cellX = this.getX() + space;
		int cellY = this.getY() + space;
		context.drawGuiTexture(CELL_BACKGROUND, cellX, cellY, cellSize, cellSize);
		context.drawTexture(TextureCanvasWidget.BACKGROUND, cellX + 1, cellY + 1, 0,0,cellSize - 2, cellSize - 2, cellSize - 2, cellSize - 2);

		if (this.pixelInfo != null && this.pixelInfo.getColor() != 0) {
			context.fill(cellX + 1, cellY + 1, cellX + cellSize - 1, cellY + cellSize - 1, this.pixelInfo.getColor());
		}

		int titleAndTextSpacing = this.getHeight() / 10;

		String currentPosTitle = "Current:";
		String currentPosText = "X: %s Y: %s".formatted(this.getPixelX(), this.getPixelY());

		String originPosTitle = "Origin:";
		String originPosText = "X: %s Y: %s".formatted(this.getPixelOriginX(), this.getPixelOriginY());

		int originTextWidth = this.getMaxWidth(textRenderer, originPosTitle, originPosText);

		int currentPosX = cellX + cellSize + space;
		int originPosX = this.getX() + this.getWidth() - originTextWidth - space - 2;

		int textPosY = this.getY() + (this.getHeight() / 2) - (((textRenderer.fontHeight * 2) + titleAndTextSpacing) / 2);

		// Current
		context.drawText(textRenderer, currentPosTitle, currentPosX, textPosY, -1, true);
		context.drawText(textRenderer, currentPosText, currentPosX, textPosY + textRenderer.fontHeight + titleAndTextSpacing, -1, true);

		// Origin
		context.drawText(textRenderer, originPosTitle, originPosX, textPosY, -1, true);
		context.drawText(textRenderer, originPosText, originPosX, textPosY + textRenderer.fontHeight + titleAndTextSpacing, -1, true);
	}

	private int getMaxWidth(TextRenderer textRenderer, String... texts) {
		int i = 0;

		for (String text : texts) {
			i = Math.max(i, textRenderer.getWidth(text));
		}

		return i;
	}

	private int getPixelOriginX() {
		if (this.pixelInfo == null) {
			return 0;
		}

		return this.pixelInfo.getOriginX();
	}

	private int getPixelOriginY() {
		if (this.pixelInfo == null) {
			return 0;
		}

		return this.pixelInfo.getOriginY();
	}

	private int getPixelX() {
		if (this.pixelInfo == null) {
			return 0;
		}

		return this.pixelInfo.getX();
	}

	private int getPixelY() {
		if (this.pixelInfo == null) {
			return 0;
		}

		return this.pixelInfo.getY();
	}
}

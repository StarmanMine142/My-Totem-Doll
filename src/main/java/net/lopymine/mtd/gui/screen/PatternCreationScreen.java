package net.lopymine.mtd.gui.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.data.TotemDollTextures;
import net.lopymine.mtd.modmenu.yacl.TransparencySprites;
import net.lopymine.mtd.gui.widget.*;
import net.lopymine.mtd.utils.pos.PixelInfo;


import java.util.List;
import org.jetbrains.annotations.Nullable;

public class PatternCreationScreen extends Screen {

	private Dimension canvasBackgroundPanel;
	private Dimension buttonsBackgroundPanel;
	@Nullable
	private PixelInfo selectedPixel;

	public PatternCreationScreen() {
		super(Text.of(""));
	}

	@Override
	protected void init() {
		int screenWidth = this.width;
		int screenHeight = this.height;

		int spacing = 5;
		int buttonSpacing = 10;
		int buttonHeight = 20;
		int buttonWidth = screenWidth / 6;

		int spacingBetweenCanvases = 20;

		int canvasBorderSize = 8;
		int pixelInfoAdditionWidth = 15;

		int backgroundPanelHeight = (int) (screenHeight / 1.7F);
		int pixelInfoPanelHeight = Math.max((int) (backgroundPanelHeight / 8F), 30);
		this.canvasBackgroundPanel = new Dimension(0, (screenHeight / 2) - (backgroundPanelHeight / 2), screenWidth, backgroundPanelHeight);

		int buttonsBackgroundPanelHeight = buttonHeight + (spacing * 2);
		this.buttonsBackgroundPanel = new Dimension(0, spacing * 2, this.canvasBackgroundPanel.width(), buttonsBackgroundPanelHeight);

		int canvasSize = backgroundPanelHeight - (spacing * 2) - pixelInfoPanelHeight + canvasBorderSize;
		int pixelInfoPanelWidth = canvasSize + (pixelInfoAdditionWidth * 2);

		int totemCanvasPosX = ((screenWidth / 2) - (((pixelInfoPanelWidth * 2) + spacingBetweenCanvases) / 2)) + pixelInfoAdditionWidth;
		int skinCanvasPosX = totemCanvasPosX + pixelInfoPanelWidth + spacingBetweenCanvases;
		int canvasPosY = this.canvasBackgroundPanel.y() + spacing;

		int totemPixelInfoPosX = totemCanvasPosX - pixelInfoAdditionWidth;
		int skinPixelInfoPosX = skinCanvasPosX - pixelInfoAdditionWidth;

		int pixelInfoPosY = canvasPosY + canvasSize - canvasBorderSize;

		PixelInfoWidget totemPixelInfoWidget = new PixelInfoWidget(totemPixelInfoPosX, pixelInfoPosY, pixelInfoPanelWidth, pixelInfoPanelHeight);
		PixelInfoWidget skinPixelInfoWidget = new PixelInfoWidget(skinPixelInfoPosX, pixelInfoPosY, pixelInfoPanelWidth, pixelInfoPanelHeight);

		TextureCanvasWidget totemCanvasWidget = new TextureCanvasWidget(totemCanvasPosX, canvasPosY, canvasSize, canvasSize, 16, 16, MyTotemDoll.id("canvas.png"), Identifier.ofVanilla("textures/item/diamond.png"), Text.of(""));
		TextureCanvasWidget skinCanvasWidget = new TextureCanvasWidget(skinCanvasPosX, canvasPosY, canvasSize, canvasSize, -1, -1, null, TotemDollTextures.STEVE_SKIN, Text.of(""));

//		totemCanvasWidget.setOnTextureClick((pixelInfo) -> {
//			if (this.selectedPixel != null) {
//				totemCanvasWidget.setPixel(pixelInfo.getX() - 1, pixelInfo.getY() - 1, pixelInfo.getOriginX() - 1, pixelInfo.getOriginY() - 1, this.selectedPixel.getColor());
//				this.selectedPixel = null;
//				skinCanvasWidget.setSelectedPixel(null);
//				skinPixelInfoWidget.setPixelInfo(null);
//			}
//		});
//
//		skinCanvasWidget.setOnTextureClick((pixelInfo) -> {
//			skinPixelInfoWidget.setPixelInfo(pixelInfo);
//			this.selectedPixel = pixelInfo;
//		});
//
//		totemCanvasWidget.setOnTextureHovered((pixelInfo) -> {
//			totemPixelInfoWidget.setPixelInfo(pixelInfo);
//		});
//
//		skinCanvasWidget.setOnTextureHovered((pixelInfo) -> {
//			if (skinCanvasWidget.getSelectedPixel() == null) {
//				skinPixelInfoWidget.setPixelInfo(pixelInfo);
//			}
//		});

		int firstButtonPosX = (this.buttonsBackgroundPanel.width() / 2) - ((((buttonWidth + buttonSpacing) * 3) + (buttonSpacing * 2) + buttonWidth) / 2);
		int buttonPosY = this.buttonsBackgroundPanel.y() + spacing;

		ButtonWidget loadSkin = ButtonWidget.builder(Text.of("Load Skin"), (buttonWidget) -> {})
				.dimensions(firstButtonPosX, buttonPosY, buttonWidth, buttonHeight)
				.build();

		ButtonWidget savePattern = ButtonWidget.builder(Text.of("Save Pattern"), (buttonWidget) -> {})
				.dimensions(firstButtonPosX + buttonWidth + buttonSpacing, buttonPosY, buttonWidth, buttonHeight)
				.build();

		TextFieldWidget textFieldWidget = new TextFieldWidget(
				MinecraftClient.getInstance().textRenderer,
				firstButtonPosX + ((buttonWidth + buttonSpacing) * 2), buttonPosY,
				buttonWidth, buttonHeight, Text.of(""));
		textFieldWidget.setSuggestion("Pattern ID");

		ButtonWidget back = ButtonWidget.builder(Text.of("Back"), (buttonWidget) -> {})
				.dimensions(firstButtonPosX + ((buttonWidth + buttonSpacing) * 3) + (buttonSpacing * 2), buttonPosY, buttonWidth, buttonHeight)
				.build();

		this.addDrawableChild(totemCanvasWidget);
		this.addDrawableChild(skinCanvasWidget);

		this.addDrawable(totemPixelInfoWidget);
		this.addDrawable(skinPixelInfoWidget);

		this.addDrawableChild(loadSkin);
		this.addDrawableChild(savePattern);
		this.addDrawableChild(textFieldWidget);
		this.addDrawableChild(back);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);

		RenderSystem.enableBlend();

		for (Dimension panel : List.of(this.buttonsBackgroundPanel, this.canvasBackgroundPanel)) {
			context.drawTexture(TransparencySprites.HEADER_SEPARATOR_TEXTURE, panel.x(), panel.y() - 2, 0, 0, 0, panel.width(), 2, 32, 2);
			context.drawTexture(TransparencySprites.MENU_LIST_BACKGROUND_TEXTURE, panel.x(), panel.y(), 0, 0, 0, panel.width(), panel.height(), 32, 32);
			context.drawTexture(TransparencySprites.FOOTER_SEPARATOR_TEXTURE, panel.x(), panel.y() + panel.height(), 0, 0, 0, panel.width(), 2, 32, 2);
		}

		RenderSystem.disableBlend();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		if (this.selectedPixel != null) {
			int pixelSize = (int) (this.height / 70F);
			context.fill(mouseX - pixelSize, mouseY - pixelSize, mouseX + pixelSize, mouseY + pixelSize, this.selectedPixel.getColor());
		}
	}

	@Override
	protected void clearChildren() {
		for (Element child : this.children()) {
			if (child instanceof TextureCanvasWidget textureCanvasWidget) {
				textureCanvasWidget.close();
			}
		}
		super.clearChildren();
	}

	@Override
	public void close() {
		super.close();
		for (Element child : this.children()) {
			if (child instanceof TextureCanvasWidget textureCanvasWidget) {
				textureCanvasWidget.close();
			}
		}
	}

	private record Dimension(int x, int y, int width, int height) {

	}
}

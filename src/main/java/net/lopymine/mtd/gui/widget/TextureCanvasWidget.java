package net.lopymine.mtd.gui.widget;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.RotationAxis;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.utils.*;
import net.lopymine.mtd.utils.pos.*;

import java.util.*;
import java.util.function.Consumer;
import org.jetbrains.annotations.*;

@Setter
@Getter
public class TextureCanvasWidget extends ClickableWidget {

	public static final Identifier BACKGROUND = MyTotemDoll.id("textures/gui/canvas/background.png");
	public static final Identifier OVERLAY_CORNER = MyTotemDoll.id("textures/gui/canvas/overlay_corner.png");
	public static final Identifier OVERLAY_BORDER = MyTotemDoll.id("textures/gui/canvas/overlay_border.png");

	private final int borderSize = 8;
	private final Map<Integer, PixelInfo> hashedOriginPixels = new HashMap<>();
	@NotNull
	private CanvasTexture mainTexture;
	@Nullable
	private CanvasTexture backgroundTexture;
	@Nullable
	private PixelInfo hoveredPixel;
	@Nullable
	private Consumer<PixelInfo> onTextureClick;
	@Nullable
	private Consumer<PixelInfo> onTextureHovered;
	@Nullable
	private PixelInfo startDraggingPixel;
	@Nullable
	private PixelInfo endDraggingPixel;

	public TextureCanvasWidget(int x, int y, int canvasWidth, int canvasHeight, int textureWidth, int textureHeight, @Nullable Identifier textureId, @Nullable Identifier backgroundTexture, Text message) {
		super(x, y, canvasWidth - 16, canvasHeight - 16, message);
		this.setBackgroundTexture(backgroundTexture);
		this.createMainTexture(textureWidth, textureHeight, textureId);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		CanvasMousePos mousePos = this.getMousePos(mouseX, mouseY);
		boolean hovered = this.isHovered(mouseX, mouseY);

		this.hoveredPixel = new PixelInfo(mousePos.imageX() + 1, mousePos.imageY() + 1, this.getColorAtCanvas(mousePos.imageX(), mousePos.imageY()));
		if (this.onTextureHovered != null && hovered) {
			this.onTextureHovered.accept(this.hoveredPixel);
		}

		this.renderCanvasBorders(context);
		this.renderCanvasTexture(this.backgroundTexture, this.mainTexture, mousePos, context, hovered);
	}

	private void renderCanvasBorders(DrawContext context) {
		context.drawTexture(MyTotemDoll.id("textures/gui/canvas/left_corner.png"), this.getX(), this.getY(), 0, 0, this.borderSize, this.borderSize, this.borderSize, this.borderSize);

		context.drawTexture(MyTotemDoll.id("textures/gui/canvas/right_corner.png"), this.getX() + this.getWidth() + this.borderSize, this.getY(), 0, 0, this.borderSize, this.borderSize, this.borderSize, this.borderSize);

		context.getMatrices().push();
		context.getMatrices().translate(this.getX() + this.getWidth() + this.borderSize, this.getY(), 0);
		context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90), 0, 0, 0);
		context.drawTexture(MyTotemDoll.id("textures/gui/canvas/left_border.png"), 0, 0, 0, 0, this.borderSize, this.getHeight(), this.borderSize, 16);
		context.getMatrices().pop();

		context.drawTexture(MyTotemDoll.id("textures/gui/canvas/left_border.png"), this.getX(), this.getY() + this.borderSize, 0, 0, this.borderSize, this.getHeight(), this.borderSize, 16);
		context.drawTexture(MyTotemDoll.id("textures/gui/canvas/right_border.png"), this.getX() + this.getWidth() + this.borderSize, this.getY() + this.borderSize, 0, 0, this.borderSize, this.getHeight(), this.borderSize, 16);

	}

	private void renderCanvasTexture(@Nullable CanvasTexture backgroundTexture, CanvasTexture mainTexture, CanvasMousePos mousePos, DrawContext context, boolean hovered) {
		int rootWidth = (int) mainTexture.pixelWidth * mainTexture.textureHeight;
		int rootHeight = (int) mainTexture.pixelHeight * mainTexture.textureWidth;
		float differenceWidth = (float) this.getWidth() / rootWidth;
		float differenceHeight = (float) this.getHeight() / rootHeight;

		MatrixStack matrices = context.getMatrices();

		matrices.push();

		matrices.translate(this.getX() + this.borderSize, this.getY() + this.borderSize, 0.0F);
		matrices.scale(differenceWidth, differenceHeight, 1.0F);

		context.drawTexture(BACKGROUND, 0, 0, 0, 0, 0, rootWidth, rootHeight, rootWidth, rootHeight);

		if (backgroundTexture != null && backgroundTexture.textureId != null) {
			context.drawTexture(backgroundTexture.textureId, 0, 0, 0, 0, 0, rootWidth, rootHeight, rootWidth, rootHeight);
		}
		if (mainTexture.textureId != null) {
			context.drawTexture(mainTexture.textureId, 0, 0, 0, 0, 0, rootWidth, rootHeight, rootWidth, rootHeight);
		}

		if (hovered || this.startDraggingPixel != null || this.endDraggingPixel != null) {
			if (this.startDraggingPixel != null && this.endDraggingPixel != null) {
				this.drawSelection(context, this.startDraggingPixel, this.endDraggingPixel, mainTexture.pixelWidth, mainTexture.pixelHeight);
			} else if (this.startDraggingPixel != null) {
				PixelInfo endDraggingPixel = new PixelInfo(mousePos.imageX() + 1, mousePos.imageY() + 1, this.getColorAtCanvas(mousePos.imageX(), mousePos.imageY()));
				this.drawSelection(context, this.startDraggingPixel, endDraggingPixel, mainTexture.pixelWidth, mainTexture.pixelHeight);
			} else if (this.endDraggingPixel != null) {
				this.drawSelection(context, this.endDraggingPixel, this.endDraggingPixel, mainTexture.pixelWidth, mainTexture.pixelHeight);
			}
		}

		matrices.pop();
	}

	private void drawSelection(DrawContext context, PixelInfo start, PixelInfo end, float pixelWidth, float pixelHeight) {
		int x = Math.abs(end.getX() - start.getX());
		int y = Math.abs(end.getY() - start.getY());

		RenderSystem.enableBlend();
		//RenderSystem.blendFuncSeparate(SrcFactor.ONE_MINUS_DST_COLOR, DstFactor.ONE_MINUS_SRC_COLOR, SrcFactor.ONE, DstFactor.ZERO);

		// Top
		boolean b1 = x % 2 == 1;
		boolean b = y % 2 == 1;
		boolean bl = b1 || b;

		for (int i = 0; i < x + 1; i++) {
			if (i % 2 == 1) {
				continue;
			}
			int d = bl ? (int) (pixelWidth / 2) : 0;
			context.drawTexture(OVERLAY_BORDER, d + ((start.getX() - 1 + i) * ((int) pixelWidth)), (start.getY()-1) * ((int) pixelHeight), 0, 0,0, (int) pixelWidth, (int) pixelHeight, (int) pixelWidth, (int) pixelHeight);
		}

		MatrixStack matrices = context.getMatrices();

		// Left
		matrices.push();
		matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90), ((int) pixelHeight) / 2F, ((int) pixelHeight) / 2F, 0F);
		for (int i = 0; i < y + 1; i++) {
			if (i % 2 == 1) {
				continue;
			}
			int d = bl ? (int) (pixelHeight / 2) : 0;
			context.drawTexture(OVERLAY_BORDER, (d + ((start.getY() - 1 + i) * ((int) pixelHeight))) * -1, (start.getX()-1) * ((int) pixelWidth), 0, 0,0, (int) pixelWidth, (int) pixelHeight, (int) pixelWidth, (int) pixelHeight);
		}
		matrices.pop();

		// Right
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90), ((int) pixelHeight) / 2F, ((int) pixelHeight) / 2F, 0F);
		for (int i = 0; i < y + 1; i++) {
			if (i % 2 == 1) {
				continue;
			}
			int d = bl ? (int) (pixelHeight / 2) : 0;
			context.drawTexture(OVERLAY_BORDER, d + ((start.getY() - 1 + i) * ((int) pixelHeight)), ((start.getX()-1 + x) * ((int) pixelWidth)) * -1, 0, 0,0, (int) pixelWidth, (int) pixelHeight, (int) pixelWidth, (int) pixelHeight);
		}
		matrices.pop();

		// Top
		matrices.push();
		matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180), ((int) pixelHeight) / 2F, ((int) pixelHeight) / 2F, 0F);
		for (int i = 0; i < x + 1; i++) {
			if (i % 2 == 1) {
				continue;
			}
			int d = bl ? (int) (pixelWidth / 2) : 0;
			context.drawTexture(OVERLAY_BORDER, (d + ((start.getX() - 1 + i) * ((int) pixelWidth))) * -1, ((start.getY()-1 + y) * ((int) pixelHeight)) * -1, 0, 0,0, (int) pixelWidth, (int) pixelHeight, (int) pixelWidth, (int) pixelHeight);
		}
		matrices.pop();

		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY) {
		return this.active && this.visible && this.isHovered(mouseX, mouseY);
	}

	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX >= this.getX() + this.borderSize && mouseY >= this.getY() + this.borderSize && mouseX < this.getX() + this.getWidth() + this.borderSize && mouseY < this.getY() + this.getHeight() + this.borderSize;
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {

	}

	private void onRelease(int mouseX, int mouseY, int button) {
		CanvasMousePos pos = this.getMousePos(mouseX, mouseY);
		if (button != 0) {
			return;
		}

		this.endDraggingPixel = new PixelInfo(pos.imageX() + 1, pos.imageY() + 1, this.getColorAtCanvas(pos.imageX(), pos.imageY()));
		if (this.startDraggingPixel != null && !this.startDraggingPixel.equals(this.endDraggingPixel)) {
			this.playDownSound(MinecraftClient.getInstance().getSoundManager());
		} else if (this.onTextureClick != null) {
			//this.onTextureClick.accept(this.endDraggingPixel);
		}
	}

	private void onClick(int mouseX, int mouseY, int button) {
		CanvasMousePos pos = this.getMousePos(mouseX, mouseY);
		if (button == 0) {
			this.endDraggingPixel = null;
			this.startDraggingPixel = new PixelInfo(pos.imageX() + 1, pos.imageY() + 1, this.getColorAtCanvas(pos.imageX(), pos.imageY()));
		} else {
			this.setPixel(pos.imageX(), pos.imageY(), -1, -1, 0);
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (!this.active || !this.visible) {
			return false;
		}
		if (!this.isValidClickButton(button)) {
			return false;
		}
		if (!this.clicked(mouseX, mouseY)) {
			return false;
		}
		this.onRelease((int) mouseX, (int) mouseY, button);
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!this.active || !this.visible) {
			return false;
		}
		if (!this.isValidClickButton(button)) {
			return false;
		}
		if (!this.clicked(mouseX, mouseY)) {
			return false;
		}
		this.playDownSound(MinecraftClient.getInstance().getSoundManager());
		this.onClick((int) mouseX, (int) mouseY, button);
		return true;
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return super.isValidClickButton(button) || button == 1;
	}

	private void setBackgroundTexture(@Nullable Identifier textureId) {
		this.backgroundTexture = this.getCanvasTexture(textureId).orElse(null);
	}

	private void createMainTexture(int textureWidth, int textureHeight, @Nullable Identifier textureId) {
		Optional<CanvasTexture> canvasTexture = this.createCanvasTexture(textureWidth, textureHeight, textureId);
		if (canvasTexture.isPresent()) {
			this.mainTexture = canvasTexture.get();
			return;
		}

		if (this.backgroundTexture != null) {
			this.mainTexture = new CanvasTexture(null, null, this.backgroundTexture.textureWidth, this.backgroundTexture.textureHeight, this.backgroundTexture.pixelWidth, this.backgroundTexture.pixelHeight);
			return;
		}

		int standardTextureSize = 16;
		float pixelWidth = (float) this.getWidth() / standardTextureSize;
		float pixelHeight = (float) this.getHeight() / standardTextureSize;
		this.mainTexture = new CanvasTexture(null, null, standardTextureSize, standardTextureSize, pixelWidth, pixelHeight);
	}

	private Optional<CanvasTexture> getCanvasTexture(@Nullable Identifier textureId) {
		if (textureId == null) {
			return Optional.empty();
		}
		try {
			NativeImage nativeImage = NativeImage.read(MinecraftClient.getInstance().getResourceManager().open(textureId));
			NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);

			float pixelWidth = (float) this.getWidth() / nativeImage.getWidth();
			float pixelHeight = (float) this.getHeight() / nativeImage.getHeight();

			return Optional.of(new CanvasTexture(texture, textureId, nativeImage.getWidth(), nativeImage.getHeight(), pixelWidth, pixelHeight));
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to get background canvas textureId by id \"{}\": ", textureId, e);
		}
		return Optional.empty();
	}

	private Optional<CanvasTexture> createCanvasTexture(int textureWidth, int textureHeight, @Nullable Identifier textureId) {
		if (textureWidth == -1 || textureHeight == -1 || textureId == null) {
			return Optional.empty();
		}
		try {
			NativeImageBackedTexture texture = new NativeImageBackedTexture(textureWidth, textureHeight, true);
			Identifier id = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(textureId.getNamespace(), texture);

			float pixelWidth = (float) this.getWidth() / textureWidth;
			float pixelHeight = (float) this.getHeight() / textureHeight;

			return Optional.of(new CanvasTexture(texture, id, textureWidth, textureHeight, pixelWidth, pixelHeight));
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.error("Failed to create main canvas textureId by id \"{}\": ", textureId, e);
		}
		return Optional.empty();
	}

	private int getColorAtCanvas(int x, int y) {
		int color = this.getColor(this.mainTexture, x, y);
		if (color == 0 && this.backgroundTexture != null) {
			return this.getColor(this.backgroundTexture, x, y);
		}
		return color;
	}

	private int getColor(CanvasTexture canvasTexture, int x, int y) {
		if (canvasTexture.texture == null || x < 0 || y < 0 || x >= canvasTexture.textureHeight || y >= canvasTexture.textureWidth) {
			return 0;
		}
		NativeImage image = canvasTexture.texture.getImage();
		if (image == null) {
			return 0;
		}
		return ColorUtils.swapAbgr2Argb(image.getColor(x, y));
	}

	private CanvasMousePos getMousePos(int screenMouseX, int screenMouseY) {
		int x = screenMouseX - this.getX() - 8;
		int y = screenMouseY - this.getY() - 8;

		int imageX = (int) (x / this.mainTexture.pixelWidth);
		int imageY = (int) (y / this.mainTexture.pixelHeight);

		if (imageY > this.mainTexture.textureHeight - 1) {
			imageY = this.mainTexture.textureHeight - 1;
		}
		if (imageX > this.mainTexture.textureWidth - 1) {
			imageX = this.mainTexture.textureWidth - 1;
		}

		int canvasX = (imageX * (int) this.mainTexture.pixelWidth);
		int canvasY = (imageY * (int) this.mainTexture.pixelHeight);

		return new CanvasMousePos(x, y, canvasX, canvasY, imageX, imageY);
	}

	public void setPixel(int x, int y, int originX, int originY, int color) {
		NativeImageBackedTexture texture = this.mainTexture.texture;
		if (texture == null) {
			return;
		}
		NativeImage image = texture.getImage();
		if (image == null) {
			return;
		}
		this.hashedOriginPixels.put(Objects.hash(originX, originY), new PixelInfo(originX, originY, color));
		image.setColor(x, y, ColorUtils.swapArgb2Abgr(color));
		texture.upload();
	}

	public void close() {
		Identifier textureId = this.mainTexture.textureId;
		if (textureId != null) {
			MinecraftClient.getInstance().getTextureManager().destroyTexture(textureId);
		}
	}

	public record CanvasTexture(@Nullable NativeImageBackedTexture texture, @Nullable Identifier textureId,
	                            int textureWidth,
	                            int textureHeight,
	                            float pixelWidth, float pixelHeight) {

	}

}

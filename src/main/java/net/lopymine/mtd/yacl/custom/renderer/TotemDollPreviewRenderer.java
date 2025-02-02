package net.lopymine.mtd.yacl.custom.renderer;

import dev.isxander.yacl3.gui.image.ImageRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.totem.TotemDollSkinType;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.doll.manager.StandardTotemDollManager;
import net.lopymine.mtd.gui.BackgroundDrawer;
import net.lopymine.mtd.yacl.custom.TransparencySprites;

import org.jetbrains.annotations.Nullable;

public class TotemDollPreviewRenderer implements ImageRenderer {

	private static final float[] STANDARD_SUGGESTION_TEXT_COLORS = new float[]{1.0F, 0.31F, 0.25F, 1.0F};
	private static final float[] HOLDING_PLAYER_COLORS = new float[]{0.83F, 0.47F, 0.11F, 1.0F};

	private TotemDollData data;
	@Nullable
	private MultilineText suggestionText;
	@Nullable
	private TotemDollSkinType suggestionSkinType;

	private int lastRenderWidth;

	public TotemDollPreviewRenderer() {
		this.data = StandardTotemDollManager.getStandardDoll();
	}

	@Override
	public int render(DrawContext context, int x, int y, int renderWidth, float tickDelta) {
		int offset = 5;
		int width = renderWidth - (offset * 2);

		this.renderDollStatus(context, x + offset, y + offset, width);
		this.updateSuggestion(width, this.lastRenderWidth != renderWidth);
		this.lastRenderWidth = renderWidth;

		int i = this.renderSuggestionText(context, x + offset, y + offset + 30 + 10, width);
		return (this.renderDoll(context, x + offset, i, width) + offset) - y;
	}

	private void updateSuggestion(int width, boolean resized) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		TotemDollSkinType skinType = config.getStandardTotemDollSkinType();
		String skinValue = config.getStandardTotemDollSkinValue();

		TotemDollSkinType type = this.suggestionSkinType;

		if ((!skinType.isNeedData() && skinValue.isEmpty()) && skinType != TotemDollSkinType.STEVE) {
			this.suggestionSkinType = skinType;
		} else {
			this.suggestionSkinType = null;
			this.suggestionText     = null;
		}

		if (this.suggestionSkinType != null && (type != this.suggestionSkinType || resized)) {
			this.suggestionText = MultilineText.create(textRenderer, this.suggestionSkinType.getSuggestionText(), width - 5);
		}
	}

	private int renderSuggestionText(DrawContext context, int x, int y, int width) {
		float[] suggestionColors = this.getSuggestionColors();

		if (this.suggestionText == null) {
			return y;
		}

		MatrixStack matrices = context.getMatrices();
		matrices.push();
		matrices.translate(0, 0, 10);
		RenderSystem.setShaderColor(suggestionColors[0], suggestionColors[1], suggestionColors[2], suggestionColors[3]);
		int i = this.suggestionText.draw(context, x + 5, y + 5, 10, -1);
		RenderSystem.enableBlend();
		matrices.translate(0, 0, -5);
		context.drawGuiTexture(TransparencySprites.WIDGET_SPRITES.get(true, true), x, y, width, i - y + 5);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();

		matrices.pop();

		return i + 5 + 10;
	}

	private float[] getSuggestionColors() {
		if (this.suggestionSkinType == TotemDollSkinType.HOLDING_PLAYER) {
			return HOLDING_PLAYER_COLORS;
		}
		return STANDARD_SUGGESTION_TEXT_COLORS;
	}

	private void renderDollStatus(DrawContext context, int x, int y, int width) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		BackgroundDrawer.drawTransparencyWidgetBackground(context, x, y, width, 30, true, true);

		ClickableWidget.drawScrollableText(context, textRenderer, MyTotemDoll.text("text.status").append(this.data.getTextures().getState().getText()), x + 2, y, x + width - 2, y + 30, -1);
	}

	private int renderDoll(DrawContext context, int x, int y, int size) {
		MyTotemDollConfig config = MyTotemDollClient.getConfig();

		BackgroundDrawer.drawTransparencyWidgetBackground(context, x, y, size, size, true, true);

		float s = size / 1.5F;
		int o = (int) (size - s) / 2;

		TotemDollRenderer.renderPreview(context, x + o, y + o, s, config.isUseVanillaTotemModel() ? null : this.data);

		return y + size + 2;
	}

	@Override
	public void close() {

	}

	public void updateDoll() {
		this.data = StandardTotemDollManager.updateDoll();
	}
}

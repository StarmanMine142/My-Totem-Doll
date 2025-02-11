package net.lopymine.mtd.gui.widget.tag;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.rendering.TooltipSize;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.manager.StandardTotemDollManager;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.gui.tooltip.preview.TotemDollPreviewTooltipData;
import net.lopymine.mtd.tag.Tag;

import org.jetbrains.annotations.Nullable;

@Setter
@Getter
public class CustomModelTagButtonWidget extends TagButtonWidget {

	@Nullable
	private final Identifier model;
	@Nullable
	private TotemDollData data;

	public CustomModelTagButtonWidget(Tag tag, int x, int y, TagPressAction pressAction) {
		super(tag, x, y, pressAction);
		this.model = MyTotemDollClient.getConfig().getCustomModelIds().get(String.valueOf(tag.getTag()));
		this.data = this.model == null ? null : StandardTotemDollManager.getStandardDoll().copy();
	}

	public void updateData(TotemDollData data) {
		if (this.model == null) {
			return;
		}
		this.data = data.copy();
	}

	@Override
	protected void renderIcon(DrawContext context, int x, int y) {
		if (this.data == null || this.model == null) {
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			String text = "?";
			int width = textRenderer.getWidth(text);
			context.drawText(textRenderer, text, x + (this.getWidth() / 2) - (width / 2), y + (this.getHeight() / 2) - (textRenderer.fontHeight / 2), -1, true);
		} else {
			this.data.setTempModel(this.model);
			context.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1);
			MatrixStack matrices = context.getMatrices();
			matrices.push();
			matrices.translate(0, -2, 0);
			TotemDollRenderer.renderPreview(context, x, y, Math.min(this.getWidth(), this.getHeight()), this.data);
			matrices.pop();
			context.disableScissor();
		}
	}

	@Override
	public @Nullable TooltipComponent getCurrentTooltipComponent() {
		if (!this.active && this.getInactiveTooltipComponentSuppler() != null) {
			return this.getInactiveTooltipComponentSuppler().get();
		}
		if (this.data != null && this.model != null) {
			return TooltipComponent.of(new TotemDollPreviewTooltipData(this.data, this.model));
		}
		return TooltipComponent.of(Text.of("Unknown Model").asOrderedText());
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, /*? if >=1.21 {*/ double horizontalAmount, /*?}*/ double verticalAmount) {
		if (!this.isMouseOver(mouseX, mouseY)) {
			return false;
		}
		int amount = ((int) verticalAmount) > 0 ? 1 : -1;
		MyTotemDollConfig config = MyTotemDollClient.getConfig();
		if (Screen.hasShiftDown()) {
			config.setTagMenuTooltipSize(TooltipSize.values()[MathHelper.floorMod(config.getTagMenuTooltipSize().ordinal() + amount, TooltipSize.values().length)]);
			return true;
		} else if (Screen.hasControlDown()) {
			config.setTagMenuTooltipModelScale(MathHelper.clamp(config.getTagMenuTooltipModelScale() + (amount / 18F), 0.1F, 10F));
			return true;
		}
		return false;
	}
}

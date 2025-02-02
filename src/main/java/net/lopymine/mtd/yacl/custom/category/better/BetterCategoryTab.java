package net.lopymine.mtd.yacl.custom.category.better;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.gui.YACLScreen.CategoryTab;
import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.yacl.custom.TransparencySprites;
import net.lopymine.mtd.yacl.custom.screen.MyTotemDollYACLScreen;

public class BetterCategoryTab extends CategoryTab {

	private final ScreenRect rightPaneDim;

	public BetterCategoryTab(YACLScreen screen, ConfigCategory category, ScreenRect tabArea) {
		super(screen, category, tabArea);
		this.rightPaneDim = new ScreenRect(screen.width / 3 * 2, tabArea.getTop() + 1, screen.width / 3, tabArea.height());
	}

	@Override
	public void renderBackground(DrawContext graphics) {
		RenderSystem.enableBlend();
		RenderSystem.enableDepthTest();

		// right pane darker db
		graphics.drawTexture(TransparencySprites.getMenuListBackgroundTexture(), rightPaneDim.getLeft(), rightPaneDim.getTop(), rightPaneDim.getRight() + 2, rightPaneDim.getBottom() + 2, rightPaneDim.width() + 2, rightPaneDim.height() + 2, 32, 32);

		// top separator for right pane
		graphics.getMatrices().push();
		graphics.getMatrices().translate(0, 0, 10);
		graphics.drawTexture(TransparencySprites.getMenuSeparatorTexture(), rightPaneDim.getLeft() - 1, rightPaneDim.getTop() - 2, 0.0F, 0.0F, rightPaneDim.width() + 1, 2, 32, 2);
		graphics.getMatrices().pop();

		// left separator for right pane
		graphics.getMatrices().push();
		graphics.getMatrices().translate(rightPaneDim.getLeft(), rightPaneDim.getTop() - 1, 0);
		graphics.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90), 0, 0, 1);
		graphics.drawTexture(TransparencySprites.getMenuSeparatorTexture(), 0, 0, 0f, 0f, rightPaneDim.height() + 1, 2, 32, 2);
		graphics.getMatrices().pop();

		RenderSystem.disableBlend();
		RenderSystem.disableDepthTest();
	}

	@Override
	public void updateButtons() {
		this.undoButton.active = false;
		this.saveFinishedButton.setMessage(GuiUtils.translatableFallback("yacl.gui.done", ScreenTexts.DONE));
		this.saveFinishedButton.setTooltip(new YACLTooltip(Text.translatable("yacl.gui.finished.tooltip"), this.saveFinishedButton));
		this.cancelResetButton.setMessage(Text.translatable("controls.reset"));
		this.cancelResetButton.setTooltip(new YACLTooltip(Text.translatable("yacl.gui.reset.tooltip"), this.cancelResetButton));
	}
}

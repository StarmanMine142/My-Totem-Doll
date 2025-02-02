package net.lopymine.mtd.gui.widget.button;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.*;
import net.minecraft.util.math.ColorHelper.Argb;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.gui.widget.list.ListWithStaticHeaderWidget;

import java.util.*;

public class ButtonListWidget extends ListWithStaticHeaderWidget<ButtonListEntryWidget> {

	public static final MutableText NOTHING_FOUND_TEXT = MyTotemDoll.text("text.nothing_found");
	private final List<ButtonListEntryWidget> searchWidgets = new ArrayList<>();
	private boolean searching = false;

	public ButtonListWidget(int x, int y, int width, int height, int buttonHeight) {
		super(x, y, width, height - 5, buttonHeight, 1 + 5 + MinecraftClient.getInstance().textRenderer.fontHeight + 6 + 1 + 5);
	}

	@Override
	public int addEntry(ButtonListEntryWidget entry) {
		return super.addEntry(entry);
	}

	@Override
	public int getRowWidth() {
		return this.width - (5 * 2);
	}

	public int getRowLeft() {
		return this.getX() + (this.width / 2) - this.getRowWidth() / 2;
	}

	@Override
	protected void drawHeaderAndFooterSeparators(DrawContext context) {
	}

	@Override
	protected void drawMenuListBackground(DrawContext context) {
//		if (this.getHoveredEntry() != null) {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(0, 255, 0));
//			context.drawText(MinecraftClient.getInstance().textRenderer, this.getHoveredEntry().getWidget().getMessage().getString(), 0, 0, -1, true);
//		} else {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(255, 0, 0));
//		}
		if (this.searching && this.searchWidgets.isEmpty()) {
			int a = (this.getWidth() - this.getRowWidth()) / 2;
			ClickableWidget.drawScrollableText(context, MinecraftClient.getInstance().textRenderer, NOTHING_FOUND_TEXT, this.getX() + a, this.getY(), this.getX() + this.getWidth() - a, this.getY() + this.getHeight() + 4, -1);
		}
	}

	@Override
	protected void renderStaticHeader(DrawContext context, int x, int y) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int offset = (this.getWidth() - this.getRowWidth()) / 2;
		int centerYTop = y + 6;
		int centerYBottom = centerYTop + textRenderer.fontHeight;
		int size = this.searching ? this.searchWidgets.size() : this.children().size();
		int left = this.getX() + offset;
		int right = this.getX() + this.getWidth() - offset;

		ClickableWidget.drawScrollableText(context, textRenderer, MyTotemDoll.text("text.found_models", size), left, centerYTop, right, centerYBottom, -1);
		context.fill(left + offset, centerYBottom + 5, right - offset, centerYBottom + 6, -1);
	}

	@Override
	protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
	}

	@Override
	protected boolean isScrollbarVisible() {
		return false;
	}

	@Override
	protected int getEntryCount() {
		return this.searching ? this.searchWidgets.size() : super.getEntryCount();
	}

	@Override
	protected ButtonListEntryWidget getEntry(int index) {
		return this.searching ? this.searchWidgets.get(index) : super.getEntry(index);
	}

	public void search(String string) {
		this.setScrollAmount(0);

		if (string.isEmpty()) {
			this.searching = false;
			return;
		}

		this.searchWidgets.clear();
		for (ButtonListEntryWidget child : this.children()) {
			if (child.getWidget().getMessage().toString().contains(string)) {
				this.searchWidgets.add(child);
			}
		}

		this.searchWidgets.sort(Comparator.comparing(a -> a.getWidget().getMessage().getString()));
		this.searching = true;
	}
}

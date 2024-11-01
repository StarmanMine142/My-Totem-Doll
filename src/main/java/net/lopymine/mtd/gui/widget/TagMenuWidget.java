package net.lopymine.mtd.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.gui.widget.TagMenuWidget.TagRow;
import net.lopymine.mtd.tag.manager.TagTotemDollManager;
import net.lopymine.mtd.utils.ItemStackUtils;
import net.lopymine.mtd.utils.interfaces.NameApplier;
import net.lopymine.mtd.utils.interfaces.mixin.IRequestableTooltipScreen;

import java.util.*;

public class TagMenuWidget extends ElementListWidget<TagRow> {

	public static final Identifier BACKGROUND = MyTotemDoll.id("textures/gui/tag_menu/background_new.png");

	public TagMenuWidget(int x, int y, NameApplier nameApplier) {
		super(MinecraftClient.getInstance(), 50, 166, y, 16);
		this.setRenderHeader(true, 31);
		this.setX(x);

		List<Character> list = TagTotemDollManager.getTags().keySet().stream().toList();
		for (int i = 0; i < list.size(); i += 2) {

			List<Character> characters = new ArrayList<>();
			characters.add(list.get(i));
			if (i + 1 < list.size()) {
				characters.add(list.get(i + 1));
			}

			List<TagButtonWidget> widgets = characters.stream()
					.map((c) -> {
						TagButtonWidget tagButtonWidget = new TagButtonWidget(c, 0, 0, (b) -> {
							String name = !b.isEnabled() ? TagTotemDollManager.addTag(nameApplier.getName(), c) : TagTotemDollManager.removeTag(nameApplier.getName(), c);
							nameApplier.setName(name);
						});
						tagButtonWidget.setTooltip(MyTotemDoll.text("tags.%s".formatted(c)));
						return tagButtonWidget;
					})
					.toList();

			this.addEntry(new TagRow(widgets));
		}
	}

	@Override
	protected void renderHeader(DrawContext context, int x, int y) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, "Totem", this.getX() + 5, y + 3, 4210752, false);
		context.drawText(textRenderer, "Settings", this.getX() + 5, y + textRenderer.fontHeight + 5 + 1, 4210752, false);
	}

	@Override
	public int getRowWidth() {
		return 28;
	}

	public int getRowLeft() {
		return this.getX() + 10;
	}

	@Override
	protected void drawHeaderAndFooterSeparators(DrawContext context) {
	}

	@Override
	protected void drawMenuListBackground(DrawContext context) {
		context.drawTexture(BACKGROUND, this.getX(), this.getY(), 0, 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	@Override
	protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
	}

	@Override
	protected boolean isScrollbarVisible() {
		return false;
	}

	public void updateButtons(ItemStack stack) {
		Text text = ItemStackUtils.getItemStackCustomName(stack);
		if (text == null) {
			return;
		}
		String customName = text.getString();

		String tags = TagTotemDollManager.getTagsFromName(customName);
		if (tags == null) {
			return;
		}

		for (TagRow row : this.children()) {
			for (TagButtonWidget button : row.buttons) {
				if (tags.contains(button.getText())) {
					button.setEnabled(true);
				}
			}
		}
	}

	public static class TagRow extends ElementListWidget.Entry<TagRow> {

		private final List<TagButtonWidget> buttons;

		public TagRow(List<TagButtonWidget> buttons) {
			this.buttons = buttons;
		}

		@Override
		public List<? extends Selectable> selectableChildren() {
			return this.buttons;
		}

		@Override
		public List<? extends Element> children() {
			return this.buttons;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			int xOffset = 0;

			for (TagButtonWidget widget : this.buttons) {
				widget.setPosition(x + xOffset, y);
				widget.render(context, mouseX, mouseY, tickDelta);
				xOffset += widget.getWidth() + 2;
			}
		}
	}
}

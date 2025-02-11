package net.lopymine.mtd.gui.widget.tag;

import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import com.mojang.blaze3d.systems.RenderSystem;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.extension.ItemStackExtension;
import net.lopymine.mtd.gui.tooltip.combined.*;
import net.lopymine.mtd.gui.tooltip.tags.TagsTooltipData;
import net.lopymine.mtd.gui.tooltip.wrapped.WrappedTextTooltipData;
import net.lopymine.mtd.gui.widget.list.ListWithStaticHeaderWidget;
import net.lopymine.mtd.gui.widget.tag.TagMenuWidget.TagRow;
import net.lopymine.mtd.tag.Tag;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.DrawUtils;
import net.lopymine.mtd.utils.tooltip.IRequestableTooltipScreen;
import java.util.*;

@ExtensionMethod(ItemStackExtension.class)
public class TagMenuWidget extends ListWithStaticHeaderWidget<TagRow> {

	public static final Identifier BACKGROUND = MyTotemDoll.id("textures/gui/tag_menu/background_new.png");

	public TagMenuWidget(int x, int y, NameApplier nameApplier) {
		super(x, y, 50, 156, 16, 35);

		List<Tag> list = TagsManager.getTags().values().stream().toList();

		for (int i = 0; i < list.size(); i += 2) {

			List<Tag> tags = new ArrayList<>();
			tags.add(list.get(i));
			if (i + 1 < list.size()) {
				tags.add(list.get(i + 1));
			}

			List<TagButtonWidget> widgets = tags.stream()
					.map((tag) -> {
						char c = tag.getTag();
						TagButtonWidget tagButtonWidget = new TagButtonWidget(tag, 0, 0, (b) -> {
							String name = !b.isEnabled() ? TagsManager.addTag(nameApplier.getName(), c) : TagsManager.removeTag(nameApplier.getName(), c);
							nameApplier.setName(name);
						});
						tagButtonWidget.setTooltip(TagsManager.getTagDescription(c));
						tagButtonWidget.setInactiveTooltipComponentSuppler(() -> TooltipComponent.of(
								new CombinedTooltipData(
										new WrappedTextTooltipData(tag.getText()),
										new TagsTooltipData(tag.getIncompatibilityTags())
								)
						));
						return tagButtonWidget;
					})
					.toList();

			this.addEntry(new TagRow(widgets));
		}

		List<Tag> customModelIds = TagsManager.getCustomModelIdsTags().values().stream().toList();

		if (!customModelIds.isEmpty()) {
			this.addEntry(new SeparatorRow(MyTotemDoll.text("tag_menu.custom_models.title")));
		}

		for (int i = 0; i < customModelIds.size(); i += 2) {

			List<Tag> characters = new ArrayList<>();
			characters.add(customModelIds.get(i));
			if (i + 1 < customModelIds.size()) {
				characters.add(customModelIds.get(i + 1));
			}

			List<TagButtonWidget> widgets = characters.stream()
					.map((tag) -> {
						char c = tag.getTag();
						CustomModelTagButtonWidget tagButtonWidget = new CustomModelTagButtonWidget(tag, 0, 0, (b) -> {
							String name = !b.isEnabled() ? TagsManager.addTag(nameApplier.getName(), c) : TagsManager.removeTag(nameApplier.getName(), c);
							nameApplier.setName(name);
						});
						tagButtonWidget.setInactiveTooltipComponentSuppler(() -> TooltipComponent.of(new WrappedTextTooltipData(MyTotemDoll.text("tag_menu.custom_model.inactive_tooltip"))));
						return (TagButtonWidget) tagButtonWidget;
					})
					.toList();

			this.addEntry(new TagRow(widgets));
		}
	}

	@Override
	protected void renderStaticHeader(DrawContext context, int x, int y) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		ClickableWidget.drawScrollableText(context, textRenderer, MyTotemDoll.text("tag_menu.title"), this.getX() + 8, this.getWidgetY() + 13, this.getX() + this.getWidth() - 8, this.getWidgetY() + 13 + textRenderer.fontHeight, -1);
	}

	@Override
	public int getRowWidth() {
		return 30;
	}

	@Override
	public int getRowLeft() {
		return this.getX() + 10;
	}

	@Override
	public int getListHeight() {
		return super.getListHeight();
	}

	@Override
	protected void drawMenuListBackground(DrawContext context) {
//				if (this.getHoveredEntry() != null) {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(0, 255, 0));
//		} else {
//			context.fill(this.getX(), this.getY(), this.getRight() + 5, this.getBottom(), Argb.getArgb(255, 0, 0));
//		}
		DrawUtils.drawTexture(context, BACKGROUND, this.getX(), this.getWidgetY(), 0, 0, 50, 166, 50, 166);
	}

	@Override
	protected void drawSelectionHighlight(DrawContext context, int y, int entryWidth, int entryHeight, int borderColor, int fillColor) {
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, /*? if >=1.21 {*/ double horizontalAmount, /*?}*/ double verticalAmount) {
		TagRow entry = this.getEntryAtPosition(mouseX, mouseY);
		if (entry != null && entry.mouseScrolled(mouseX, mouseY, /*? if >=1.21 {*/horizontalAmount, /*?}*/ verticalAmount)) {
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, /*? if >=1.21 {*/horizontalAmount, /*?}*/ verticalAmount);
	}

	@Override
	public boolean needScrollBar() {
		return false;
	}

	public void updateButtons(ItemStack stack) {
		Text text = stack.getRealCustomName();
		if (text == null) {
			return;
		}
		String customName = text.getString();

		String tags = TagsManager.getTagsFromName(customName);
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

	public void updateButtonsAvailable(ItemStack stack) {
		Text text = stack.getRealCustomName();

		String a = text == null ? "" : TagsManager.getTagsFromName(text.getString());
		String tags = a == null ? "" : a;

		for (TagRow row : this.children()) {
			for (TagButtonWidget button : row.buttons) {
				button.setActive(button.getTag().compatibilityTest(tags));
			}
		}
	}

	public void updateButtonsWithCustomModels(ItemStack stack) {
		for (TagRow row : this.children()) {
			for (TagButtonWidget button : row.buttons) {
				if (button instanceof CustomModelTagButtonWidget widget) {
					widget.updateData(TotemDollRenderer.parseTotemDollData(stack));
				}
			}
		}
	}

	public interface NameApplier {

		String getName();

		void setName(String name);

	}

	public static class TagRow extends ElementListWidget.Entry<TagRow> {

		private final List<TagButtonWidget> buttons;

		public TagRow(List<TagButtonWidget> buttons) {
			this.buttons = buttons;
		}

		@Override
		public List<TagButtonWidget> selectableChildren() {
			return this.buttons;
		}

		@Override
		public List<TagButtonWidget> children() {
			return this.buttons;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			int xOffset = 0;

			for (TagButtonWidget widget : this.buttons) {
				widget.setPosition(x + xOffset, y);
				widget.setCanBeHovered(hovered);
				widget.render(context, mouseX, mouseY, tickDelta);
				xOffset += widget.getWidth() + 2;
			}
		}
	}

	public static class SeparatorRow extends TagRow {

		public static final Identifier SEPARATOR = MyTotemDoll.id("textures/gui/tag_menu/separator.png");

		private final Text text;

		public SeparatorRow(Text text) {
			super(new ArrayList<>());
			this.text = text;
		}

		@Override
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
			MinecraftClient client = MinecraftClient.getInstance();
			TextRenderer textRenderer = client.textRenderer;

			RenderSystem.enableBlend();
			DrawUtils.drawTexture(context, SEPARATOR, x - 1, y + (entryHeight / 2) - 3, 0, 0, 32, 7, 32, 7);
			RenderSystem.disableBlend();

			if (hovered) {
				if (!(client.currentScreen instanceof IRequestableTooltipScreen tooltipScreen)) {
					return;
				}

				tooltipScreen.myTotemDoll$requestTooltip(((c, mx, my, d) -> {
					c.drawOrderedTooltip(textRenderer, textRenderer.wrapLines(this.text, 10000), mx, my);
				}));
			}
		}
	}

}

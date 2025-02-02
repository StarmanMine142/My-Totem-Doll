package net.lopymine.mtd.yacl.custom.controller.entry;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.math.ColorHelper.Argb;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.utils.tooltip.IRequestableTooltipScreen;
import net.lopymine.mtd.yacl.custom.option.entry.*;

import java.util.List;
import org.jetbrains.annotations.Nullable;

@ExtensionMethod(OptionExtension.class)
public class EntryControllerElement<K, V> extends ControllerWidget<EntryController<K, V>> implements ParentElement {

	private static final int RED_COLOR = Argb.getArgb(255, 0, 0);

	private final AbstractWidget keyWidget;
	private final AbstractWidget valueWidget;

	private final Text keyText;
	private final MutableText valueText;
	private final EntryOptionImpl<K,V> entryOption;

	private Dimension<Integer> keyTextDimension;
	private Dimension<Integer> valueTextDimension;
	private Element focused;
	private boolean dragging;

	public EntryControllerElement(EntryController<K, V> controller, YACLScreen screen, Dimension<Integer> dim) {
		super(controller, screen, dim);

		this.entryOption = controller.getOption();
		this.keyWidget   = this.entryOption.keyOption().controller().provideWidget(screen, dim);
		this.valueWidget = this.entryOption.valueOption().controller().provideWidget(screen, dim);

		this.keyText = controller.getKeyName() == null ? MyTotemDoll.text("text.key") : controller.getKeyName();
		this.valueText = MyTotemDoll.text("text.value");

		this.setDimension(dim);
	}

	@Override
	public void setDimension(Dimension<Integer> dim) {
		super.setDimension(dim);
		int offset = 2;

		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		int keyTextWidth = Math.max(textRenderer.getWidth(this.keyText) + 15, 25);
		int valueTextWidth = Math.max(textRenderer.getWidth(this.valueText) + 15, 30);

		Dimension<Integer> partDimension = dim.withWidth(((dim.width() - (offset * 3)) / 3)).moved(offset, 0);
		Dimension<Integer> keyWidgetDimension = partDimension.expanded(-keyTextWidth, 0);
		this.keyTextDimension = partDimension.expanded(-keyWidgetDimension.width(), 0);
		this.keyWidget.setDimension(keyWidgetDimension.withX(this.keyTextDimension.xLimit() + offset));

		Dimension<Integer> w = partDimension.withWidth(partDimension.width() * 2);
		Dimension<Integer> valueWidgetDimension = w.expanded(-valueTextWidth, 0);
		this.valueTextDimension = w.expanded(-valueWidgetDimension.width(), 0).withX(this.keyWidget.getDimension().xLimit() + offset);
		this.valueWidget.setDimension(valueWidgetDimension.withX(this.valueTextDimension.xLimit() + offset).expanded(offset * -2, 0));
	}

	@Override
	protected int getHoveredControlWidth() {
		return this.getUnhoveredControlWidth();
	}

	@Override
	public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		this.drawButtonRect(graphics, keyTextDimension.x(), keyTextDimension.y(), keyTextDimension.xLimit(), keyTextDimension.yLimit(), false, false);
		this.drawButtonRect(graphics, valueTextDimension.x(), valueTextDimension.y(), valueTextDimension.xLimit(), valueTextDimension.yLimit(), false, false);

		ClickableWidget.drawScrollableText(graphics, textRenderer, this.keyText, keyTextDimension.x() + this.getXPadding(), keyTextDimension.y(), keyTextDimension.xLimit() - this.getXPadding(), keyTextDimension.yLimit(), -1);
		ClickableWidget.drawScrollableText(graphics, textRenderer, this.valueText, valueTextDimension.x() + this.getXPadding(), valueTextDimension.y(), valueTextDimension.xLimit() - this.getXPadding(), valueTextDimension.yLimit(), -1);

		this.keyWidget.render(graphics, mouseX, mouseY, delta);
		this.valueWidget.render(graphics, mouseX, mouseY, delta);

		EntryEntryOption<K> keyOption = this.entryOption.keyOption();
		EntryEntryOption<V> valueOption = this.entryOption.valueOption();

		AbstractWidget widget = keyOption.wrong() ? this.keyWidget : (valueOption.wrong() ? this.valueWidget : null);
		Option<?> option = keyOption.wrong() ? this.entryOption.keyOption() : (valueOption.wrong() ? this.entryOption.valueOption() : null);

		if (widget != null && option != null) {
			Dimension<Integer> dimension = widget.getDimension();
			MatrixStack matrices = graphics.getMatrices();
			matrices.push();
			matrices.translate(0,0, 10);
			graphics.drawBorder(dimension.x(), dimension.y(), dimension.width(), dimension.height(), RED_COLOR);
			matrices.pop();

			Screen currentScreen = MinecraftClient.getInstance().currentScreen;
			if (!widget.isMouseOver(mouseX, mouseY) || !(currentScreen instanceof IRequestableTooltipScreen tooltipScreen)) {
				return;
			}

			tooltipScreen.myTotemDoll$requestTooltip((context, mouseX1, mouseY1, delta1) -> {
				context.drawTooltip(MinecraftClient.getInstance().textRenderer, option.getReason(), mouseX1, mouseY1);
			});
		}
	}

	@Override
	public List<? extends Element> children() {
		return List.of(this.keyWidget, this.valueWidget);
	}

	@Override
	public boolean isDragging() {
		return this.dragging;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	@Nullable
	@Override
	public Element getFocused() {
		return this.focused;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
		for (Element child : this.children()) {
			child.setFocused(focused);
		}
	}

	@Override
	public void setFocused(@Nullable Element focused) {
		if (this.focused != null) {
			this.focused.setFocused(false);
		}

		if (focused != null) {
			focused.setFocused(true);
		}

		this.focused = focused;
	}

	@Override
	public void unfocus() {
		super.unfocus();
		for (Element child : this.children()) {
			child.setFocused(false);
		}
	}
}

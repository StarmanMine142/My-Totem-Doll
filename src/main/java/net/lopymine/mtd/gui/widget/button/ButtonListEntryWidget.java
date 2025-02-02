package net.lopymine.mtd.gui.widget.button;

import lombok.Getter;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;
import net.minecraft.text.Text;

import java.util.List;

@Getter
public class ButtonListEntryWidget extends Entry<ButtonListEntryWidget> {

	private final ButtonWidget widget;

	public ButtonListEntryWidget(Text text, PressAction pressAction) {
		this.widget = ButtonWidget.builder(text, pressAction).build();
	}

	@Override
	public List<? extends Selectable> selectableChildren() {
		return List.of(this.widget);
	}

	@Override
	public List<? extends Element> children() {
		return List.of(this.widget);
	}

	@Override
	public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
		this.widget.setWidth(entryWidth);
		this.widget.setHeight(entryHeight);
		this.widget.setX(x);
		this.widget.setY(y);

		this.widget.render(context, mouseX, mouseY, tickDelta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return this.widget.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return this.widget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return this.widget.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		this.widget.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.widget.isMouseOver(mouseX, mouseY);
	}
}

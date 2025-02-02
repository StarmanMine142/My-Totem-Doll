package net.lopymine.mtd.gui.widget.list;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.gui.widget.ElementListWidget.Entry;
import net.minecraft.util.math.ColorHelper.Argb;

@Getter
public abstract class ListWithStaticHeaderWidget<E extends Entry<E>> extends ElementListWidget<E> {

	protected final int heightOfStaticHeader;

	public ListWithStaticHeaderWidget(int x, int y, int width, int height, int itemHeight, int headerHeight) {
		super(MinecraftClient.getInstance(), width, height - headerHeight, y + headerHeight, itemHeight);
		this.setX(x);
		this.setRenderHeader(true, 0);
		this.heightOfStaticHeader = headerHeight;
	}

	@Override
	protected void renderList(DrawContext context, int mouseX, int mouseY, float delta) {
		context.enableScissor(this.getX(), this.getListY(), this.getX() + this.getWidth(), this.getListY() + this.getListHeight());
		super.renderList(context, mouseX, mouseY, delta);
		context.disableScissor();
	}

	public int getListY() {
		return this.getY();
	}

	public int getListHeight() {
		return this.getHeight();
	}

	public int getWidgetY() {
		return this.getListY() - this.heightOfStaticHeader;
	}

	public int getWidgetHeight() {
		return this.getHeightOfStaticHeader() + this.getListHeight();
	}

	@Override
	protected void renderHeader(DrawContext context, int x, int y) {
		//context.fill(this.getX() - 5, this.getWidgetY(), this.getRight(), this.getListY(), Argb.getArgb(0, 0, 255));
		this.renderStaticHeader(context, x, this.getWidgetY());
	}

	protected abstract void renderStaticHeader(DrawContext context, int x, int y);

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return mouseY >= (double)this.getWidgetY() && mouseY <= (double)this.getBottom() && mouseX >= (double)this.getX() && mouseX <= (double)this.getRight();
	}

	@Override
	public void setY(int y) {
		super.setY(y + this.heightOfStaticHeader);
	}

	@Override
	protected int getRowTop(int index) {
		return super.getRowTop(index) - 4;
	}

	@Override
	public void setHeight(int height) {
		super.setHeight(height - this.heightOfStaticHeader);
	}

	public int getItemHeight() {
		return this.itemHeight;
	}
}

package net.lopymine.mtd.gui.widget.tag;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.tag.Tag;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.DrawUtils;
import net.lopymine.mtd.utils.tooltip.IRequestableTooltipScreen;

import java.util.List;
import java.util.function.*;
import org.jetbrains.annotations.Nullable;

//? if >=1.21 {
import net.minecraft.client.gui.screen.ButtonTextures;
//?} else {
/*import net.lopymine.mtd.utils.ButtonTextures;
*///?}

@Getter
@Setter
public class TagButtonWidget extends ButtonWidget {

	public static final Identifier INACTIVE_TEXTURE = MyTotemDoll.id("textures/gui/tag_menu/button_inactive.png");

	public static final ButtonTextures TEXTURES = new ButtonTextures(
			MyTotemDoll.id("textures/gui/tag_menu/button_enabled.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_disabled.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_enabled_hovered.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_disabled_hovered.png")
	);

	private final Tag tag;
	private final String text;
	private final Identifier icon;

	private boolean enabled;
	@Nullable
	private Text tooltipText;
	@Nullable
	private Supplier<TooltipComponent> inactiveTooltipComponentSuppler;
	private boolean pressed;
	private boolean canBeHovered = true;

	public TagButtonWidget(Tag tag, int x, int y, TagPressAction pressAction) {
		super(x, y, 14, 14, Text.of(""), (widget) -> pressAction.onPress((TagButtonWidget) widget), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
		this.tag          = tag;
		this.text         = String.valueOf(tag.getTag()).toLowerCase();
		this.icon         = TagsManager.getTagIcon(this.text.charAt(0));
	}

	@Override
	public void onPress() {
		super.onPress();
		this.enabled = !this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.setEnabled(enabled, false);
	}

	public void setEnabled(boolean enabled, boolean callback) {
		this.enabled = enabled;
		if (callback) {
			this.onPress.onPress(this);
		}
	}

	public void setTooltip(@Nullable Text text) {
		this.tooltipText = text;
	}


	@Override
	public void /*? if >=1.21 {*/renderWidget/*?} else {*//*renderButton*//*?}*/(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderButton(context, this.getX(), this.getY());
		this.requestTooltip();
	}

	protected void renderButton(DrawContext context, int x, int y) {
		this.renderBackground(context, x, y);
		this.renderIcon(context, x, y);
	}

	protected void renderIcon(DrawContext context, int x, int y) {
		DrawUtils.drawTexture(context, this.icon, x + (this.getWidth() / 2) - 5, y + (this.getHeight() / 2) - 5, 0, 0, 10, 10, 10, 10);
	}

	protected void renderBackground(DrawContext context, int x, int y) {
		Identifier texture = !this.active ? INACTIVE_TEXTURE : TEXTURES.get(this.isEnabled(), this.isHovered());
		DrawUtils.drawTexture(context, texture, x, y, 0, 0, this.width, this.height, this.width, this.height);
	}

	public void requestTooltip() {
		MinecraftClient client = MinecraftClient.getInstance();
		Screen screen = client.currentScreen;

		TooltipComponent component = this.getCurrentTooltipComponent();
		if (component == null) {
			return;
		}

		if (!(screen instanceof IRequestableTooltipScreen tooltipScreen)) {
			return;
		}

		if (!this.isHovered()) {
			return;
		}

		tooltipScreen.myTotemDoll$requestTooltip(((c, x, y, d) -> {
			DrawUtils.drawTooltip(c, List.of(component), x, y);
		}));
	}

	@Nullable
	public TooltipComponent getCurrentTooltipComponent() {
		if (!this.active && this.inactiveTooltipComponentSuppler != null) {
			return this.inactiveTooltipComponentSuppler.get();
		}
		return this.getTooltipComponent();
	}

	private @Nullable TooltipComponent getTooltipComponent() {
		if (this.tooltipText == null) {
			return null;
		}
		return TooltipComponent.of(this.tooltipText.asOrderedText());
	}

	public void setActive(boolean bl) {
		this.active = bl;
	}

	public boolean over(double mouseX, double mouseY) {
		return this.active
				&& this.visible
				&& mouseX >= (double)this.getX()
				&& mouseY >= (double)this.getY()
				&& mouseX < (double)(this.getX() + this.getWidth())
				&& mouseY < (double)(this.getY() + this.getHeight());
	}

	@Override
	public boolean isHovered() {
		return super.isHovered() && this.isCanBeHovered();
	}

	@FunctionalInterface
	public interface TagPressAction {

		void onPress(TagButtonWidget button);

	}
}

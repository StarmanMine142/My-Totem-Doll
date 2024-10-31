package net.lopymine.mtd.gui.widget;

import lombok.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.tag.manager.TagTotemDollManager;
import net.lopymine.mtd.utils.interfaces.mixin.IRequestableTooltipScreen;

import java.util.List;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class TagButtonWidget extends ButtonWidget {

	public static final ButtonTextures TEXTURES = new ButtonTextures(
			MyTotemDoll.id("textures/gui/tag_menu/button_enabled.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_disabled.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_enabled_hovered.png"),
			MyTotemDoll.id("textures/gui/tag_menu/button_disabled_hovered.png")
	);

	private final String text;
	private boolean enabled;
	@Nullable
	private Text tooltipText;

	public TagButtonWidget(char c, int x, int y, TagPressAction pressAction) {
		super(x, y, 14, 14, Text.of(""), (widget) -> pressAction.onPress((TagButtonWidget) widget), ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
		this.text = String.valueOf(c).toLowerCase();
	}

	@Override
	public void onPress() {
		super.onPress();
		this.enabled = !this.enabled;
	}

	public void setTooltip(@Nullable Text text) {
		this.tooltipText = text;
	}

	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		Identifier texture = TEXTURES.get(this.isEnabled(), this.isSelected());
		context.drawTexture(texture, this.getX(), this.getY(), 0, 0, 0, this.width, this.height, this.width, this.height);
		Identifier icon = TagTotemDollManager.getTagIcon(this.text.charAt(0));
		context.drawTexture(icon, this.getX() + (this.getWidth() / 2) - 5, this.getY() + (this.getHeight() / 2) - 5, 0, 0, 0, 10, 10, 10, 10);
		this.requestTooltip();
	}

	public void requestTooltip() {
		MinecraftClient client = MinecraftClient.getInstance();
		Screen screen = client.currentScreen;
		TextRenderer textRenderer = client.textRenderer;

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
			c.drawTooltip(textRenderer, List.of(component), x, y, HoveredTooltipPositioner.INSTANCE);
		}));
	}

	@Nullable
	public TooltipComponent getCurrentTooltipComponent() {
		if (this.tooltipText == null) {
			return null;
		}
		return TooltipComponent.of(this.tooltipText.asOrderedText());
	}

	public interface TagPressAction {

		void onPress(TagButtonWidget button);

	}
}

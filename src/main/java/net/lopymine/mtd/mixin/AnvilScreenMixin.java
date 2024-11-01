package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.mtd.gui.tooltip.InfoTooltipData;
import net.lopymine.mtd.gui.widget.*;
import net.lopymine.mtd.utils.ItemStackUtils;
import net.lopymine.mtd.utils.interfaces.NameApplier;
import net.lopymine.mtd.utils.interfaces.mixin.MTDAnvilScreen;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> implements MTDAnvilScreen {

	@Shadow
	private TextFieldWidget nameField;

	@Shadow public abstract void resize(MinecraftClient client, int width, int height);

	@Shadow protected abstract void setup();

	@Unique
	private TagButtonWidget tagButton;
	@Unique
	private TagMenuWidget tagMenuWidget;
	@Unique
	private SmallInfoWidget infoWidget;
	@Unique
	private boolean menuVisible = false;

	public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
		super(handler, playerInventory, title, texture);
	}

	@Inject(at = @At("HEAD"), method = "setup")
	private void setupTagMenu(CallbackInfo ci) {
		ItemStack stack = this.handler.getSlot(0).getStack();
		ItemStack result = this.handler.getSlot(2).getStack();
		boolean bl = stack.isOf(Items.TOTEM_OF_UNDYING);

		NameApplier nameApplier = new NameApplier() {
			@Override
			public String getName() {
				return nameField.getText();
			}

			@Override
			public void setName(String name) {
				nameField.setText(name);
			}
		};
		this.tagMenuWidget         = new TagMenuWidget(0, 0, nameApplier);
		this.tagMenuWidget.visible = this.menuVisible;
		if (this.tagMenuWidget.visible) {
			this.tagMenuWidget.updateButtons(result.isEmpty() ? stack : result);
		}
		this.addDrawableChild(this.tagMenuWidget);

		//

		this.infoWidget         = new SmallInfoWidget(0, 0, new InfoTooltipData("tags.info"));
		this.infoWidget.visible = this.tagMenuWidget.visible;
		this.addDrawable(this.infoWidget);

		//

		this.tagButton = new TagButtonWidget('t', 0, 0, (b) -> {
			this.menuVisible = !b.isEnabled();
			this.resize(this.client, this.width, this.height);
		});
		this.tagButton.setEnabled(this.tagMenuWidget.visible);
		this.tagButton.visible = bl;
		this.addDrawableChild(this.tagButton);

		//

		if (this.tagMenuWidget.visible) {
			this.backgroundWidth = 176 + this.tagMenuWidget.getWidth() + 5 + this.infoWidget.getWidth();
		} else {
			this.backgroundWidth = 176;
		}

		this.x = (this.width - this.backgroundWidth) / 2;
		this.updateWidgetsPositions();
	}

	@Unique
	private void updateWidgetsPositions() {
		int tagMenuX = this.x + 176 + 5;
		int tagMenuY = this.y;
		this.tagMenuWidget.setPosition(tagMenuX, tagMenuY);

		int infoWidgetX = this.tagMenuWidget.getX() + this.tagMenuWidget.getWidth() + 2;
		int infoWidgetY = this.tagMenuWidget.getY() + 2;
		this.infoWidget.setPosition(infoWidgetX, infoWidgetY);

		Slot slot = this.handler.getSlot(2);
		int tagButtonX = (((176 - slot.x) / 2) + slot.x) + this.x;
		int tagButtonY = (slot.y + 2) + this.y;
		this.tagButton.setPosition(tagButtonX, tagButtonY);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"), method = "drawForeground")
	private void swapBackgroundValue(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
		original.call(instance, x1 - this.backgroundWidth + 176, y1, x2 - this.backgroundWidth + 176, y2, color);
	}

	@Inject(at = @At("TAIL"), method = "drawBackground")
	private void updateWidgetPositions(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		this.updateWidgetsPositions();
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"), method = "drawForeground")
	private int swapBackgroundValue(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
		return original.call(instance, textRenderer, text, x - this.backgroundWidth + 176, y, color);
	}

	//? <1.21 {
	/*@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V"), method = "drawInvalidRecipeArrow")
	private void swapBackgroundValue(DrawContext instance, Identifier identifier, int x, int y, int u, int v, Operation<Void> original) {
		original.call(instance, identifier, x, y, u - this.backgroundWidth + 176, v);
	}
	*///?}

	@Inject(at = @At("HEAD"), method = "onSlotUpdate")
	private void checkTotem(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (slotId == 0) {
			this.tagButton.visible = stack.isOf(Items.TOTEM_OF_UNDYING);
			if (!this.tagButton.visible && this.tagMenuWidget.visible) {
				this.tagButton.setEnabled(true, true);
			}
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getName()Lnet/minecraft/text/Text;"), method = "onSlotUpdate")
	private Text swapItemName(ItemStack instance, Operation<Text> original) {
		Text text = ItemStackUtils.getItemStackCustomName(instance);
		if (text == null) {
			return original.call(instance);
		}
		return text;
	}
}

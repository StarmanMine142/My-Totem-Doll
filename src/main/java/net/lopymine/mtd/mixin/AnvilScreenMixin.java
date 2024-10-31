package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;


import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.*;
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
	@Unique
	private TagButtonWidget tagButton;
	@Unique
	private TagMenuWidget tagMenuWidget;
	@Unique
	private SmallInfoWidget infoWidget;

	public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
		super(handler, playerInventory, title, texture);
	}

	@Inject(at = @At("TAIL"), method = "setup")
	private void setupTagMenu(CallbackInfo ci) {
		this.tagMenuWidget         = new TagMenuWidget(this.x + 176 + 5, this.y, new NameApplier() {
			@Override
			public String getName() {
				return nameField.getText();
			}

			@Override
			public void setName(String name) {
				nameField.setText(name);
			}
		});
		this.tagMenuWidget.visible = false;

		this.infoWidget = new SmallInfoWidget(
				this.tagMenuWidget.getX() + this.tagMenuWidget.getWidth() + 2,
				this.tagMenuWidget.getY() + 2,
				new InfoTooltipData("tags.info")
		);
		this.infoWidget.visible = this.tagMenuWidget.visible;

		this.tagButton = new TagButtonWidget('t', this.x + 134 + 16 + 4, this.y + 47 + 1, (b) -> {
			this.tagMenuWidget.visible = !this.tagMenuWidget.visible;
			this.infoWidget.visible    = this.tagMenuWidget.visible;
			this.tagMenuWidget.updateButtons(this.handler.getSlot(0).getStack());
		});
		this.tagButton.setEnabled(false);
		this.tagButton.visible = false;

		this.checkTotem();
	}

	@Inject(at = @At("TAIL"), method = "renderForeground")
	private void checkTotem(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		this.tagButton.requestTooltip();
		this.tagMenuWidget.requestTooltip();
		this.infoWidget.requestTooltip();
	}

	@Inject(at = @At("HEAD"), method = "onSlotUpdate")
	private void checkTotem(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (slotId == 0) {
			this.checkTotem();
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

	@Unique
	private void checkTotem() {
		ItemStack stack = this.handler.getSlot(0).getStack();
		boolean bl = stack.isOf(Items.TOTEM_OF_UNDYING);
		this.tagButton.visible = bl;
		this.tagButton.setEnabled(false);
		if (this.tagMenuWidget.visible && !bl) {
			this.tagMenuWidget.visible = false;
			this.infoWidget.visible    = false;
		}
	}

	@Override
	public TagMenuWidget myTotemDoll$getTagMenuWidget() {
		return this.tagMenuWidget;
	}

	@Override
	public TagButtonWidget myTotemDoll$getTagButtonWidget() {
		return this.tagButton;
	}

	@Override
	public SmallInfoWidget myTotemDoll$getInfoWidget() {
		return this.infoWidget;
	}
}

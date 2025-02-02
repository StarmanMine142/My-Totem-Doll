package net.lopymine.mtd.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.lopymine.mtd.gui.widget.tag.TagButtonWidget;
import net.lopymine.mtd.utils.mixin.MTDAnvilScreen;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

	@Inject(at = @At("HEAD"), method = "mouseDragged", cancellable = true)
	private void onMouseClicked(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
		if (!(this instanceof MTDAnvilScreen anvilScreen)) {
			return;
		}
		TagButtonWidget tagButtonWidget = anvilScreen.myTotemDoll$getTagButtonWidget();
		if (tagButtonWidget == null) {
			return;
		}
		if (tagButtonWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "mouseReleased", cancellable = true)
	private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (!(this instanceof MTDAnvilScreen anvilScreen)) {
			return;
		}
		TagButtonWidget tagButtonWidget = anvilScreen.myTotemDoll$getTagButtonWidget();
		if (tagButtonWidget == null) {
			return;
		}
		if (tagButtonWidget.mouseReleased(mouseX, mouseY, button)) {
			cir.setReturnValue(true);
		}
	}
}

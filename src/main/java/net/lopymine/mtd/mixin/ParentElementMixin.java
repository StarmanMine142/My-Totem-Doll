package net.lopymine.mtd.mixin;

import net.minecraft.client.gui.ParentElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.mtd.gui.widget.*;
import net.lopymine.mtd.utils.interfaces.mixin.MTDAnvilScreen;

@Mixin(ParentElement.class)
public interface ParentElementMixin {

	@Inject(at = @At("HEAD"), method = "mouseClicked")
	private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
//		if (!(this instanceof MTDAnvilScreen mtdAnvilScreen)) {
//			return;
//		}
//
//		TagButtonWidget tagButtonWidget = mtdAnvilScreen.myTotemDoll$getTagButtonWidget();
//		boolean bl = tagButtonWidget.mouseClicked(mouseX, mouseY, button);
//
//		TagMenuWidget tagMenuWidget = mtdAnvilScreen.myTotemDoll$getTagMenuWidget();
//		boolean bl2 = tagMenuWidget.mouseClicked(mouseX, mouseY, button);
//
//		if (bl || bl2) {
//			cir.setReturnValue(true);
//		}
	}

}

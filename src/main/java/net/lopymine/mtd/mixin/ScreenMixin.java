package net.lopymine.mtd.mixin;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import net.lopymine.mtd.utils.tooltip.TooltipRequest;
import net.lopymine.mtd.utils.tooltip.IRequestableTooltipScreen;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable, IRequestableTooltipScreen {

	@Unique
	private TooltipRequest tooltipRequest;

	@Inject(at = @At("TAIL"), method = "render")
	private void renderWithTooltip(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (this.tooltipRequest != null) {
			this.tooltipRequest.render(context, mouseX, mouseY, delta);
			this.tooltipRequest = null;
		}
	}

	@Override
	public void myTotemDoll$requestTooltip(TooltipRequest tooltipRequest) {
		this.tooltipRequest = tooltipRequest;
	}

	@Override
	public TooltipRequest myTotemDoll$getCurrentRequest() {
		return this.tooltipRequest;
	}
}

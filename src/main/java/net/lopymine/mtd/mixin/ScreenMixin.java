package net.lopymine.mtd.mixin;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.utils.interfaces.TooltipRequest;
import net.lopymine.mtd.utils.interfaces.mixin.IRequestableTooltipScreen;

import org.jetbrains.annotations.Nullable;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractParentElement implements Drawable, IRequestableTooltipScreen {

	@Unique
	@Nullable
	private TooltipRequest tooltipRequest;

	@Override
	public void myTotemDoll$requestTooltip(TooltipRequest tooltipRequest) {
		this.tooltipRequest = tooltipRequest;
	}

	@Override
	@Nullable
	public TooltipRequest myTotemDoll$getCurrentRequest() {
		return this.tooltipRequest;
	}
}

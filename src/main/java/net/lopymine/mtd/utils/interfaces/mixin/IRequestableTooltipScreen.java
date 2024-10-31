package net.lopymine.mtd.utils.interfaces.mixin;

import net.lopymine.mtd.utils.interfaces.TooltipRequest;

import org.jetbrains.annotations.Nullable;

public interface IRequestableTooltipScreen {

	void myTotemDoll$requestTooltip(@Nullable TooltipRequest tooltipRequest);

	@Nullable TooltipRequest myTotemDoll$getCurrentRequest();

}

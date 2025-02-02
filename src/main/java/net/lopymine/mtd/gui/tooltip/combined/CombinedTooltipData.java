package net.lopymine.mtd.gui.tooltip.combined;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;

import java.util.*;

public record CombinedTooltipData(List<TooltipComponent> list) implements TooltipData {

	public CombinedTooltipData(TooltipData... data) {
		this(Arrays.stream(data).map(TooltipComponent::of).toList());
	}

}

package net.lopymine.mtd.gui.tooltip.state;

import net.minecraft.item.tooltip.TooltipData;

import net.lopymine.mtd.doll.data.LoadingState;

public record LoadingStateTooltipData(LoadingState state) implements TooltipData {

}

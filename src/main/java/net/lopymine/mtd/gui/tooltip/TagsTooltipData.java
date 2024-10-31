package net.lopymine.mtd.gui.tooltip;

import net.minecraft.item.tooltip.TooltipData;

import java.util.Optional;

public record TagsTooltipData(String tags, Optional<TooltipData> original) implements TooltipData {

}

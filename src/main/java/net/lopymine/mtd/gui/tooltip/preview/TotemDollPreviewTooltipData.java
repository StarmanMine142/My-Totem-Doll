package net.lopymine.mtd.gui.tooltip.preview;

import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.doll.data.TotemDollData;

public record TotemDollPreviewTooltipData(TotemDollData data, Identifier model) implements TooltipData {

}

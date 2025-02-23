package net.lopymine.mtd.gui.tooltip.preview;

//? if >=1.21 {
/*import net.minecraft.item.tooltip.TooltipData;
 *///?} else {
import net.minecraft.client.item.TooltipData;
//?}

import net.minecraft.util.Identifier;

import net.lopymine.mtd.doll.data.TotemDollData;

public record TotemDollPreviewTooltipData(TotemDollData data, Identifier model) implements TooltipData {

}

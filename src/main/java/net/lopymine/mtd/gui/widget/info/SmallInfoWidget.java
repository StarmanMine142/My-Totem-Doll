package net.lopymine.mtd.gui.widget.info;

//? if >=1.21 {
import net.minecraft.item.tooltip.TooltipData;
 //?} else {
/*import net.minecraft.client.item.TooltipData;
*///?}
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;

import org.jetbrains.annotations.Nullable;

public class SmallInfoWidget extends InfoWidget {

	public static final Identifier TEXTURE = MyTotemDoll.id("textures/gui/icon/info_small.png");

	public SmallInfoWidget(int x, int y, @Nullable TooltipData tooltipData) {
		super(x, y, 9, 10, tooltipData, TEXTURE);
	}
}

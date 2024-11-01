package net.lopymine.mtd.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import net.lopymine.mtd.gui.tooltip.*;

public class MTDEventsManager {

	public static void registerTooltipCallbacks() {
		TooltipComponentCallback.EVENT.register((data -> {
			if (data instanceof TagsTooltipData tooltipData) {
				return new TagsTooltipComponent(tooltipData.tags(), tooltipData.original());
			}
			if (data instanceof InfoTooltipData tooltipData) {
				return new InfoTooltipComponent(tooltipData.key());
			}
			return null;
		}));
	}
}

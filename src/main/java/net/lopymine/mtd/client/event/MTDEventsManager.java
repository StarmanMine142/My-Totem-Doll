package net.lopymine.mtd.client.event;

import net.minecraft.client.util.math.MatrixStack;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.gui.tooltip.*;
import net.lopymine.mtd.gui.widget.*;
import net.lopymine.mtd.utils.interfaces.TooltipRequest;
import net.lopymine.mtd.utils.interfaces.mixin.*;

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

	public static void registerScreenEvents() {
		ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
			ScreenEvents.afterRender(screen).register(MyTotemDoll.id("o_mtd"), ((s, context, mouseX, mouseY, delta) -> {
				if (s instanceof MTDAnvilScreen anvilScreen) {
					RenderSystem.enableDepthTest();
					MatrixStack matrices = context.getMatrices();
					matrices.push();
					matrices.translate(0, 0, 350);

					TagButtonWidget tagButtonWidget = anvilScreen.myTotemDoll$getTagButtonWidget();
					tagButtonWidget.render(context, mouseX, mouseY, delta);

					TagMenuWidget tagMenuWidget = anvilScreen.myTotemDoll$getTagMenuWidget();
					tagMenuWidget.render(context, mouseX, mouseY, delta);

					SmallInfoWidget infoWidget = anvilScreen.myTotemDoll$getInfoWidget();
					infoWidget.render(context, mouseX, mouseY, delta);
					matrices.pop();
					RenderSystem.disableDepthTest();
				}

				if (s instanceof IRequestableTooltipScreen tooltipScreen) {
					TooltipRequest request = tooltipScreen.myTotemDoll$getCurrentRequest();
					if (request == null) {
						return;
					}

					tooltipScreen.myTotemDoll$requestTooltip(null);
					request.render(context, mouseX, mouseY, delta);
				}
			}));
		}));

		ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
			if (!(screen instanceof MTDAnvilScreen anvilScreen)) {
				return;
			}

			ScreenMouseEvents.allowMouseClick(screen).register(MyTotemDoll.id("a_mtd"), ((s, mouseX, mouseY, button) -> {
				TagButtonWidget tagButtonWidget = anvilScreen.myTotemDoll$getTagButtonWidget();
				boolean bl = tagButtonWidget.mouseClicked(mouseX, mouseY, button);

				TagMenuWidget tagMenuWidget = anvilScreen.myTotemDoll$getTagMenuWidget();
				boolean bl2 = tagMenuWidget.mouseClicked(mouseX, mouseY, button) || tagMenuWidget.isHovered();

				return !(bl || bl2);
			}));
		}));
	}
}

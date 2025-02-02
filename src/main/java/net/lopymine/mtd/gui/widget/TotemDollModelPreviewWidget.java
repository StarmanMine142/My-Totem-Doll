package net.lopymine.mtd.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.text.Text;
import net.minecraft.util.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.renderer.TotemDollRenderer;
import net.lopymine.mtd.doll.manager.StandardTotemDollManager;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.model.bb.manager.BlockBenchModelManager;

public class TotemDollModelPreviewWidget implements Drawable {

	private static final Text[] TEXTS = new Text[]{
			MyTotemDoll.text("text.loading.0"),
			MyTotemDoll.text("text.loading.1"),
			MyTotemDoll.text("text.loading.2"),
			MyTotemDoll.text("text.loading.3"),
	};

	private final int x;
	private final int y;
	private final float size;

	private final TotemDollData data;

	private boolean loading;
	private boolean failedLoading;

	public TotemDollModelPreviewWidget(int x, int y, float size) {
		this.x    = x;
		this.y    = y;
		this.size = size;
		this.data = StandardTotemDollManager.getStandardDoll().copy();
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (this.loading) {
			int halfOfSize = (int) this.size / 2;
			TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
			context.drawCenteredTextWithShadow(textRenderer, this.getLoadingDots(Util.getMeasuringTimeMs()), this.x + halfOfSize, this.y + halfOfSize - (textRenderer.fontHeight / 2), -1);
		} else {
			TotemDollRenderer.renderPreview(context, this.x, this.y, this.size, this.data);
		}
	}

	public void updateModel(Identifier id) {
		this.loading = true;
		BlockBenchModelManager.getModelAsyncAsResponse(id, (response) -> {
			if (response.statusCode() == 0) {
				MModel value = response.value();
				if (value != null) {
					this.loading = false;
					this.updateModel(value);
				} else {
					this.failedLoading = true;
					MyTotemDollClient.LOGGER.error("[DEV] Was received null model with status code 0 (success), this shouldn't happen!");
				}
			}
		});
	}

	public void updateModel(MModel model) {
		this.data.setCustomModel(model);
	}

	private Text getLoadingDots(long tick) {
		int i = (int) (tick / 300L % (long) TEXTS.length);
		return TEXTS[i];
	}
}

package net.lopymine.mtd.gui.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import org.joml.Matrix4f;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.tag.manager.TagTotemDollManager;

import java.util.*;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public class TagsTooltipComponent implements TooltipComponent {

	private static final Text TITLE = MyTotemDoll.text("tags.title").formatted(Formatting.GRAY);

	@Nullable
	private final TooltipComponent originalComponent;
	private final Map<Identifier, Text> rows = new HashMap<>();

	public TagsTooltipComponent(String tags, Optional<TooltipData> originalData) {
		this.originalComponent = originalData.map(TooltipComponent::of).orElse(null);
		tags.trim().chars().mapToObj(i -> (char) i).distinct().forEach((character) -> {
			if (!TagTotemDollManager.hasTag(character)) {
				return;
			}
			this.rows.put(TagTotemDollManager.getTagIcon(character), TagTotemDollManager.getTagDescription(character));
		});
	}

	@Override
	public int getHeight() {
		int i = this.getTagsComponentHeight();
		if (this.originalComponent != null) {
			i += this.originalComponent.getHeight();
		}
		return i;
	}

	private int getTagsComponentHeight() {
		return 10 * (this.rows.size() + 1);
	}

	@Override
	public int getWidth(TextRenderer textRenderer) {
		int maxWidth = textRenderer.getWidth(TITLE);
		for (Text text : this.rows.values()) {
			int textWidth = textRenderer.getWidth(text) + 10;
			maxWidth = Math.max(maxWidth, textWidth);
		}
		return Math.max(maxWidth, this.originalComponent != null ? this.originalComponent.getWidth(textRenderer) : 0);
	}

	@Override
	public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, Immediate vertexConsumers) {
		if (this.originalComponent != null) {
			this.originalComponent.drawText(textRenderer, x, y + this.getTagsComponentHeight(), matrix, vertexConsumers);
		}
	}

	@Override
	public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
		int yOffset = 10;

		context.drawText(textRenderer, TITLE, x, y, -1, true);

		for (Entry<Identifier, Text> entry : this.rows.entrySet()) {
			context.drawTexture(entry.getKey(), x, y + yOffset - 1, 0, 0, 10, 10, 10, 10);
			context.drawText(textRenderer, entry.getValue(), x + 10 + 2, y + yOffset, -1, true);
			yOffset += 10;
		}

		if (this.originalComponent != null) {
			this.originalComponent.drawItems(textRenderer, x, y + yOffset, context);
		}
	}
}

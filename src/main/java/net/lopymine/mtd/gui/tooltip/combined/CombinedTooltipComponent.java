package net.lopymine.mtd.gui.tooltip.combined;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;

import java.util.List;

public class CombinedTooltipComponent implements TooltipComponent {

    private final List<TooltipComponent> components;

    public CombinedTooltipComponent(List<TooltipComponent> components) {
        this.components = components;
    }

    @Override
    public int getHeight(/*? >=1.21.2 {*/TextRenderer textRenderer/*?}*/) {
        int height = 0;
        for (TooltipComponent component : components) {
            height += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
        return height;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        int width = 0;
        for (TooltipComponent component : components) {
            int componentWidth = component.getWidth(textRenderer);
            if (componentWidth > width) width = componentWidth;
        }
        return width;
    }

    @Override
    public void drawText(TextRenderer textRenderer, int x, int y, Matrix4f matrix, VertexConsumerProvider.Immediate vertexConsumers) {
        int componentY = 0;
        for (TooltipComponent component : components) {
            component.drawText(textRenderer, x, y + componentY, matrix, vertexConsumers);
            componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, /*? >=1.21.2 {*/int w, int h,/*?}*/ DrawContext context) {
        int componentY = 0;
        for (TooltipComponent component : components) {
            component.drawItems(textRenderer, x, y + componentY, /*? >=1.21.2 {*/ w, h,/*?}*/ context);
            componentY += component.getHeight(/*? >=1.21.2 {*/textRenderer/*?}*/) + 1;
        }
    }
}
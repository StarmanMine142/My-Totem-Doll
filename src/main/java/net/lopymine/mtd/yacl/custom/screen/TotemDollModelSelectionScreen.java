package net.lopymine.mtd.yacl.custom.screen;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.gui.widget.ButtonWidget.PressAction;
import net.minecraft.text.*;
import net.minecraft.util.*;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.gui.widget.TotemDollModelPreviewWidget;
import net.lopymine.mtd.gui.widget.button.*;
import net.lopymine.mtd.pack.TotemDollModelFinder;
import net.lopymine.mtd.gui.BackgroundDrawer;

import java.util.*;
import java.util.Map.Entry;
import org.jetbrains.annotations.*;

public class TotemDollModelSelectionScreen extends Screen {

	private final Option<Identifier> option;
	private final Screen parent;

	private MutableDimension<Integer> modelPanelDimension;
	private MutableDimension<Integer> listPanelDimension;
	private MutableDimension<Integer> modelPathDimension;
	private MutableDimension<Integer> titleDimension;

	private TotemDollModelPreviewWidget totemDollModelPreviewWidget;

	@Nullable
	private Identifier selectedModelId;
	@Nullable
	private Text selectedModelName;
	@Nullable
	private Text selectedModel;

	public TotemDollModelSelectionScreen( Screen parent, Option<Identifier> option) {
		super(MyTotemDoll.text("standard_model_selection_screen.title"));
		this.option = option;
		this.parent = parent;
	}

	@Override
	protected void init() {
		int o = 10;
		int h = 20;

		this.modelPanelDimension = this.getModelPanelDimension(o);
		this.listPanelDimension  = this.getListPanelDimension(o);
		MutableDimension<Integer> textFieldDimension = this.getTextFieldDimension(this.listPanelDimension, o, h);
		this.modelPathDimension = this.getModelPathDimension(this.modelPanelDimension, this.listPanelDimension, o, h);
		this.titleDimension     = this.getTitleDimension(o, h, this.modelPanelDimension);
		MutableDimension<Integer> buttonPanelDimension = this.getButtonPanelDimension(o, h, this.modelPathDimension, this.listPanelDimension);

		ButtonListWidget listWidget = this.addDrawableChild(new ButtonListWidget(this.listPanelDimension.x(), this.listPanelDimension.y(), this.listPanelDimension.width(), this.listPanelDimension.height(), 25));
		TextFieldWidget textFieldWidget = this.addDrawableChild(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, textFieldDimension.x(), textFieldDimension.y(), textFieldDimension.width(), textFieldDimension.height(), Text.of("")));
		textFieldWidget.setChangedListener(listWidget::search);
		textFieldWidget.setPlaceholder(MyTotemDoll.text("placeholder.search"));

		this.addDrawableChild(
				ButtonWidget.builder(MyTotemDoll.text("button.close"), (b) -> this.close(false))
						.dimensions(buttonPanelDimension.x(), buttonPanelDimension.y(), buttonPanelDimension.width(), buttonPanelDimension.height())
						.build()
		);
		buttonPanelDimension.move(0, h + o);
		this.addDrawableChild(
				ButtonWidget.builder(MyTotemDoll.text("button.select_current"), (b) -> this.close(true))
						.dimensions(buttonPanelDimension.x(), buttonPanelDimension.y(), buttonPanelDimension.width(), buttonPanelDimension.height())
						.build()
		);

		Dimension<Integer> modelPreviewDimension = this.getModelPreviewDimension(this.modelPanelDimension);

		this.totemDollModelPreviewWidget = new TotemDollModelPreviewWidget(
				modelPreviewDimension.x(), modelPreviewDimension.y(),
				Math.min(modelPreviewDimension.width(), modelPreviewDimension.height())
		);

		Identifier standardModelId = MyTotemDollClient.getConfig().getStandardTotemDollModelValue();

		Set<Entry<String, Set<Identifier>>> entries = new HashSet<>(TotemDollModelFinder.getFoundedTotemModels().entrySet());
		entries.add(Map.entry(MyTotemDoll.MOD_ID, TotemDollModelFinder.getBuiltinTotemModels()));

		for (Entry<String, Set<Identifier>> entry : entries) {
			for (Identifier id : entry.getValue()) {
				String pack = entry.getKey();
				String modelName = getModelName(id.getPath());

				PressAction pressAction = (widget) -> this.setSelectedModel(id, pack, modelName);

				ButtonListEntryWidget button = new ButtonListEntryWidget(Text.of(modelName), pressAction);

				if (id.equals(standardModelId)) {
					pressAction.onPress(button.getWidget());
				}

				listWidget.addEntry(button);
			}
		}
	}

	private static @NotNull String getModelName(String path) {
		int i = path.lastIndexOf('/');
		if (i != -1) {
			return path.substring(i + 1);
		}
		return path;
	}

	private void close(boolean applyCurrent) {
		if (applyCurrent && this.selectedModelId != null) {
			this.option.requestSet(this.selectedModelId);
		}

		this.close();
	}

	private MutableDimension<Integer> getButtonPanelDimension(int o, int h, MutableDimension<Integer> modelPathDimension, MutableDimension<Integer> listPanelDimension) {
		listPanelDimension.expand(0, -((h * 2) + (o * 2)));
		return modelPathDimension.withX(modelPathDimension.xLimit() + o).withY(listPanelDimension.yLimit() + o).withWidth(listPanelDimension.width()).withHeight(h).clone();
	}

	@Override
	public void renderBackground(DrawContext context /*? if >=1.21 {*/ ,int mouseX, int mouseY, float delta/*?}*/) {
		super.renderBackground(context/*? if >=1.21 {*/ , mouseX, mouseY, delta /*?}*/);

		BackgroundDrawer.drawTransparencyBackground(context, this.modelPanelDimension.x(), this.modelPanelDimension.y(), this.modelPanelDimension.width(), this.modelPanelDimension.height(), true);
		BackgroundDrawer.drawTransparencyBackground(context, this.listPanelDimension.x(), this.listPanelDimension.y(), this.listPanelDimension.width(), this.listPanelDimension.height(), true);
		BackgroundDrawer.drawTransparencyBackground(context, this.modelPathDimension.x(), this.modelPathDimension.y(), this.modelPathDimension.width(), this.modelPathDimension.height(), true);
		BackgroundDrawer.drawTransparencyBackground(context, this.titleDimension.x(), this.titleDimension.y(), this.titleDimension.width(), this.titleDimension.height(), true);

	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		// Title
		ClickableWidget.drawScrollableText(context, textRenderer, this.getTitle(), this.titleDimension.x() + 2, this.titleDimension.y(), this.titleDimension.xLimit() - 2, this.titleDimension.yLimit(), -1);

		// "Full Model Path" text
		MutableText fullModelPathText = MyTotemDoll.text("text.full_model_path");
		int a = textRenderer.getWidth(fullModelPathText);
		int offset = 10;

		if (this.modelPathDimension.x() + a + offset > this.modelPathDimension.xLimit() - offset) {
			ClickableWidget.drawScrollableText(context, textRenderer, fullModelPathText, this.modelPathDimension.x() + offset, this.modelPathDimension.y() + offset, this.modelPathDimension.xLimit() - offset, this.modelPathDimension.y() + textRenderer.fontHeight + offset, -1);
		} else {
			context.drawText(textRenderer, fullModelPathText, this.modelPathDimension.x() + offset, this.modelPathDimension.y() + offset, -1, true);
		}

		// Model Path Text
		context.enableScissor(this.modelPathDimension.x(), this.modelPathDimension.y(), this.modelPathDimension.xLimit() - offset, this.modelPathDimension.yLimit());

		Text text = this.selectedModel == null ? Text.literal("...").formatted(Formatting.GRAY) : this.selectedModel;
		int width = textRenderer.getWidth(text);
		if (this.modelPathDimension.x() + width + offset > this.modelPathDimension.xLimit() - offset) {
			ClickableWidget.drawScrollableText(context, textRenderer, text, this.modelPathDimension.x() + offset, this.modelPathDimension.yLimit() - textRenderer.fontHeight - offset, this.modelPathDimension.xLimit() - offset, this.modelPathDimension.yLimit() - offset, -1);
		} else {
			context.drawText(textRenderer, text, this.modelPathDimension.x() + offset, this.modelPathDimension.yLimit() - textRenderer.fontHeight - offset, -1, true);
		}

		context.disableScissor();

		// Model Name Text
		context.enableScissor(this.modelPanelDimension.x(), this.modelPanelDimension.y(), this.modelPanelDimension.xLimit(), this.modelPanelDimension.yLimit());

		Text selectedModelNameText = this.selectedModelName == null ? MyTotemDoll.text("text.standard_doll") : this.selectedModelName;
		context.drawText(textRenderer, selectedModelNameText, this.modelPanelDimension.x() + offset, this.modelPanelDimension.y() + offset, -1, true);

		// Underline for this text
		context.fill(this.modelPanelDimension.x() + offset, this.modelPanelDimension.y() + offset + textRenderer.fontHeight + 3, this.modelPanelDimension.x() + offset + Math.min((textRenderer.getWidth(selectedModelNameText) + 5), this.modelPanelDimension.width() - (offset * 2)), this.modelPanelDimension.y() + offset + textRenderer.fontHeight + 4, -1);

		// Model Preview
		this.totemDollModelPreviewWidget.render(context, mouseX, mouseY, delta);

		context.disableScissor();
	}

	private void setSelectedModel(Identifier modelId, String pack, String modelName) {
		this.selectedModel     = MyTotemDoll.text("text.nice_id", pack, modelId.getPath());
		this.selectedModelId   = modelId;
		this.selectedModelName = Text.of(modelName);
		this.totemDollModelPreviewWidget.updateModel(modelId);
	}

	private MutableDimension<Integer> getTextFieldDimension(MutableDimension<Integer> listPanelDimension, int o, int h) {
		int y = (h + o) * 2;
		listPanelDimension.expand(0, -y).move(0, y);
		return Dimension.ofInt(listPanelDimension.x(), o + h + o, listPanelDimension.width(), h);
	}

	private MutableDimension<Integer> getTitleDimension(int o, int h, MutableDimension<Integer> modelPanelDimension) {
		return Dimension.ofInt(modelPanelDimension.xLimit() + o, o, this.width - modelPanelDimension.xLimit() - (o * 2), h);
	}

	private MutableDimension<Integer> getModelPreviewDimension(MutableDimension<Integer> modelPanelDimension) {
		int v = (int) (Math.min(modelPanelDimension.width(), modelPanelDimension.height()) / 1.5F);

		return Dimension.ofInt(modelPanelDimension.x() + ((modelPanelDimension.width() - v) / 2), modelPanelDimension.y() + ((modelPanelDimension.height() - v) / 2), v, v);
	}

	private MutableDimension<Integer> getModelPathDimension(MutableDimension<Integer> modelPanelDimension, MutableDimension<Integer> listPanelDimension, int o, int h) {
		int y = (h * 2) + (o * 2);
		modelPanelDimension.expand(-y, -y);
		return Dimension.ofInt(modelPanelDimension.x(), modelPanelDimension.yLimit() + o, (this.width - listPanelDimension.width() - (o * 3)), (h * 2) + o);
	}

	private MutableDimension<Integer> getListPanelDimension(int o) {
		int w = this.width / 5;
		int h = this.height - (o * 2);

		return Dimension.ofInt(this.width - o - w, o, w, h);
	}

	private MutableDimension<Integer> getModelPanelDimension(int o) {
		int a = this.height - (o * 2);

		return Dimension.ofInt(o, o, a, a);
	}

	@Override
	public void close() {
		MinecraftClient.getInstance().setScreen(this.parent);
	}
}

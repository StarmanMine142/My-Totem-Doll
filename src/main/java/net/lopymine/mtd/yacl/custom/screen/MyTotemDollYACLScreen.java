package net.lopymine.mtd.yacl.custom.screen;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.gui.*;
import lombok.Getter;
import lombok.experimental.ExtensionMethod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.yacl.custom.controller.WrongController;

@Getter
@ExtensionMethod(OptionExtension.class)
public class MyTotemDollYACLScreen extends YACLScreen {

	private boolean cannotSave;

	public MyTotemDollYACLScreen(YetAnotherConfigLib config, Screen parent) {
		super(config, parent);
		OptionUtils.forEachOptions(config, (o) -> o.addEventListener((option, value) -> this.checkWrongOptions()));
	}

	private boolean checkWrongOption(Option<?> option) {
		if (option instanceof ListOption<?> listOption) {
			for (ListOptionEntry<?> entry : listOption.options()) {
				if (entry.wrong()) {
					return true;
				}
			}
			return false;
		}
		return option.wrong();
	}

	private void updateOptionWrongStatus(Option<?> option) {
		if (option instanceof ListOption<?> listOption) {
			for (ListOptionEntry<?> entry : listOption.options()) {
				if (entry.controller() instanceof WrongController<?> wrongController) {
					wrongController.myTotemDoll$updateWrong();
				}
			}
			return;
		}
		if (option.controller() instanceof WrongController<?> wrongController) {
			wrongController.myTotemDoll$updateWrong();
		}
	}

	private boolean checkWrongOptions() {
		this.cannotSave = false;

		OptionUtils.forEachOptions(this.config, this::updateOptionWrongStatus);

		OptionUtils.consumeOptions(this.config, o -> {
			this.cannotSave |= this.checkWrongOption(o);
			return this.cannotSave;
		});

		if (this.tabManager.getCurrentTab() instanceof CategoryTab categoryTab) {
			if (this.cannotSave) {
				categoryTab.saveFinishedButton.setMessage(MyTotemDoll.text("modmenu.save_button.cannot_save"));
				categoryTab.saveFinishedButton.setTooltip(YACLTooltip.of(MyTotemDoll.text("modmenu.save_button.cannot_save.tooltip")));
			}
			categoryTab.saveFinishedButton.active = !this.cannotSave;
		}

		return this.cannotSave;
	}

	@Override
	public void finishOrSave() {
		if (this.checkWrongOptions()) {
			return;
		}
		this.close();
	}

	@Override
	public void cancelOrReset() {
		OptionUtils.forEachOptions(this.config, Option::requestSetDefault);
	}

	@Override
	public void close() {
		if (this.cannotSave) {
			return;
		}
		super.finishOrSave();
		super.close();
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

	@Override
	public void resize(MinecraftClient client, int width, int height) {
		super.resize(client, width, height);
	}
}

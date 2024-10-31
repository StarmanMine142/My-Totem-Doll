package net.lopymine.mtd.modmenu.yacl.custom.screen;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.utils.OptionUtils;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.gui.screen.Screen;

public class MyTotemDollYACLScreen extends YACLScreen {

	public MyTotemDollYACLScreen(YetAnotherConfigLib config, Screen parent) {
		super(config, parent);
	}

	@Override
	public void finishOrSave() {
		super.finishOrSave();
		this.close();
	}

	@Override
	public void cancelOrReset() {
		OptionUtils.forEachOptions(this.config, Option::requestSetDefault);
	}

	@Override
	public void undo() {
		super.undo();
	}

	@Override
	public void close() {
		this.config.saveFunction().run();
		super.close();
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}
}

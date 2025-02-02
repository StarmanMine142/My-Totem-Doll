package net.lopymine.mtd.mixin.yacl.option;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.impl.ListOptionEntryImpl;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.yacl.custom.controller.WrongController;

@Mixin(ListOptionEntryImpl.EntryController.class)
public abstract class ListOptionEntryImplEntryControllerMixin<T> implements WrongController<T> {

	@Shadow(remap = false) public abstract Controller<T> controller();

	@Override
	public void myTotemDoll$checkOnWrong(T value) {

	}

	@Override
	public void myTotemDoll$updateWrong() {
		if (this.controller() instanceof WrongController<?> wrongController) {
			wrongController.myTotemDoll$updateWrong();
		}
	}
}

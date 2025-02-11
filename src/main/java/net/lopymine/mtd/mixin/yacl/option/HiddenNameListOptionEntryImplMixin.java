package net.lopymine.mtd.mixin.yacl.option;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.impl.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.yacl.custom.option.wrong.WrongOption;
import org.jetbrains.annotations.NotNull;

@Pseudo
@ExtensionMethod(OptionExtension.class)
@Mixin(HiddenNameListOptionEntry.class)
public abstract class HiddenNameListOptionEntryImplMixin<T> implements WrongOption {

	@Dynamic
	@Shadow(remap = false) public abstract @NotNull Controller<T> controller();

	@Dynamic
	@Shadow(remap = false) @Final private ListOptionEntry<T> option;

	@Override
	public boolean myTotemDoll$wrong() {
		return this.option.wrong();
	}

	@Override
	public void myTotemDoll$setWrong(boolean bl, Text reason) {
		this.option.setWrong(bl, reason);
	}

	@Override
	public Text myTotemDoll$getReason() {
		return this.option.getReason();
	}
}


package net.lopymine.mtd.mixin.yacl.option;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.impl.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.yacl.custom.option.wrong.WrongOption;

import org.jetbrains.annotations.NotNull;

@ExtensionMethod(OptionExtension.class)
@Mixin(ListOptionEntryImpl.class)
public abstract class ListOptionEntryImplMixin<T> implements WrongOption {

	@Unique
	private boolean wrong;
	@Unique
	private Text reason;

	@Shadow(remap = false) public abstract @NotNull Controller<T> controller();

	@Override
	public boolean myTotemDoll$wrong() {
		return this.wrong;
	}

	@Override
	public void myTotemDoll$setWrong(boolean bl, Text reason) {
		this.wrong = bl;
		this.reason = reason;
	}

	@Override
	public Text myTotemDoll$getReason() {
		return this.reason;
	}
}


package net.lopymine.mtd.yacl.custom.controller.entry;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.*;
import lombok.Getter;
import net.minecraft.text.Text;
import net.lopymine.mtd.config.other.simple.SimpleEntry;
import net.lopymine.mtd.yacl.custom.controller.WrongController;
import net.lopymine.mtd.yacl.custom.option.entry.EntryOptionImpl;


import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

@Getter
public class EntryController<K, V> implements Controller<SimpleEntry<K, V>>, WrongController<SimpleEntry<K, V>> {

	private final ListOptionEntry<SimpleEntry<K, V>> listOption;
	private final Function<Option<K>, ControllerBuilder<K>> keyControllerBuilder;
	@Nullable
	private final Text keyName;
	private final Function<Option<V>, ControllerBuilder<V>> valueControllerBuilder;
	private final EntryOptionImpl<K,V> option;

	public EntryController(ListOptionEntry<SimpleEntry<K, V>> listOption,
	                       Function<Option<K>, ControllerBuilder<K>> keyControllerBuilder,
	                       @Nullable Text keyName,
	                       Function<Option<V>, ControllerBuilder<V>> valueControllerBuilder) {
		this.listOption             = listOption;
		this.keyControllerBuilder   = keyControllerBuilder;
		this.keyName                = keyName;
		this.valueControllerBuilder = valueControllerBuilder;
		this.option = new EntryOptionImpl<>(this);
	}

	@Override
	public ListOptionEntry<SimpleEntry<K, V>> option() {
		return this.listOption;
	}

	@Override
	public Text formatValue() {
		SimpleEntry<K, V> entry = this.listOption.pendingValue();
		return Text.literal("%s:%s".formatted(entry.getKey(), entry.getValue()));
	}

	@Override
	public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
		return new EntryControllerElement<>(this, screen, widgetDimension);
	}

	@Override
	public void myTotemDoll$checkOnWrong(SimpleEntry<K, V> value) {

	}

	@Override
	public void myTotemDoll$updateWrong() {
		if (this.getOption().keyOption().controller() instanceof WrongController<?> wrongController) {
			wrongController.myTotemDoll$updateWrong();
		}
		if (this.getOption().valueOption().controller() instanceof WrongController<?> wrongController) {
			wrongController.myTotemDoll$updateWrong();
		}
	}
}

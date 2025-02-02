package net.lopymine.mtd.yacl.custom.controller.entry;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.text.Text;
import net.lopymine.mtd.config.other.simple.SimpleEntry;


import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class EntryControllerBuilderImpl<K, V> implements EntryControllerBuilder<K, V> {

	private final ListOptionEntry<SimpleEntry<K, V>> option;
	private Function<Option<K>, ControllerBuilder<K>> keyControllerBuilder;
	@Nullable
	private Text keyName;
	private Function<Option<V>, ControllerBuilder<V>> valueControllerBuilder;

	public EntryControllerBuilderImpl(ListOptionEntry<SimpleEntry<K, V>> option) {
		this.option = option;
	}

	@Override
	public EntryControllerBuilderImpl<K, V> keyValueControllers(Function<Option<K>, ControllerBuilder<K>> keyControllerBuilder, Function<Option<V>, ControllerBuilder<V>> valueControllerBuilder) {
		this.keyControllerBuilder   = keyControllerBuilder;
		this.valueControllerBuilder = valueControllerBuilder;
		return this;
	}

	@Override
	public EntryControllerBuilder<K, V> setKeyName(Text keyName) {
		this.keyName = keyName;
		return this;
	}

	@Override
	public Controller<SimpleEntry<K, V>> build() {
		if (this.keyControllerBuilder == null || this.valueControllerBuilder == null) {
			throw new IllegalArgumentException("Key or value controller builders must be set");
		}
		return new EntryController<>(this.option, this.keyControllerBuilder, this.keyName, this.valueControllerBuilder);
	}
}

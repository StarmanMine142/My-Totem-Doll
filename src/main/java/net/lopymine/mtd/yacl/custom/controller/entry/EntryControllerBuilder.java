package net.lopymine.mtd.yacl.custom.controller.entry;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.text.Text;
import net.lopymine.mtd.config.other.simple.SimpleEntry;

import java.util.function.Function;

public interface EntryControllerBuilder<K, V> extends ControllerBuilder<SimpleEntry<K, V>> {

	static <K, V> EntryControllerBuilder<K, V> create(ListOptionEntry<SimpleEntry<K, V>> option) {
		return new EntryControllerBuilderImpl<>(option);
	}

	EntryControllerBuilder<K, V> keyValueControllers(Function<Option<K>, ControllerBuilder<K>> keyControllerBuilder, Function<Option<V>, ControllerBuilder<V>> valueControllerBuilder);

	EntryControllerBuilder<K, V> setKeyName(Text keyName);
}

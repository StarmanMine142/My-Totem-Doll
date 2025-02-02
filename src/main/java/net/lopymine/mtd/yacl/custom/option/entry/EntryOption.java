package net.lopymine.mtd.yacl.custom.option.entry;

import dev.isxander.yacl3.api.*;

import net.lopymine.mtd.config.other.simple.SimpleEntry;
import net.lopymine.mtd.yacl.custom.option.wrong.WrongOption;

public interface EntryOption<K, V> extends ListOptionEntry<SimpleEntry<K, V>>, WrongOption {

	Option<K> keyOption();

	Option<V> valueOption();

}

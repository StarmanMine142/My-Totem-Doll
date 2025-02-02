package net.lopymine.mtd.utils;

import net.minecraft.text.Text;

import java.util.function.Predicate;

public interface PredicateWithText<T> {

	boolean test(T value);

	Text getReason();

	static <T> PredicateWithText<T> create(Predicate<T> predicate, Text text) {
		return new PredicateWithText<T>() {
			@Override
			public boolean test(T value) {
				return predicate.test(value);
			}

			@Override
			public Text getReason() {
				return text;
			}
		};
	}

}

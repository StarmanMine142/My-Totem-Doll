package net.lopymine.mtd.yacl.custom.controller.string;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;

import net.lopymine.mtd.utils.PredicateWithText;

import java.util.function.Predicate;

public interface StringControllerWithPredicatesBuilder extends ControllerBuilder<String> {

	static StringControllerWithPredicatesBuilder create(Option<String> option) {
		return new StringControllerWithPredicatesBuilderImpl(option);
	}

	StringControllerWithPredicatesBuilder addValuePredicate(PredicateWithText<String> predicate);

	StringControllerWithPredicatesBuilder addInputPredicate(Predicate<String> predicate);
}

package net.lopymine.mtd.yacl.custom.controller.character;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;

import net.lopymine.mtd.utils.PredicateWithText;
import net.lopymine.mtd.yacl.custom.controller.string.StringControllerWithPredicatesBuilder;

import java.util.function.Predicate;

public interface CharacterControllerWithPredicatesBuilder extends StringControllerWithPredicatesBuilder {

	static CharacterControllerWithPredicatesBuilder create(Option<String> option) {
		return new CharacterControllerWithPredicatesBuilderImpl(option);
	}

	CharacterControllerWithPredicatesBuilder addValuePredicate(PredicateWithText<String> predicate);

	CharacterControllerWithPredicatesBuilder addInputPredicate(Predicate<String> predicate);
}

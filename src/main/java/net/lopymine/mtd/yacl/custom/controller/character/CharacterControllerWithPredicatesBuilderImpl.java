package net.lopymine.mtd.yacl.custom.controller.character;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.impl.controller.StringControllerBuilderImpl;

import net.lopymine.mtd.utils.PredicateWithText;
import net.lopymine.mtd.yacl.custom.controller.string.*;

import java.util.*;
import java.util.function.Predicate;

public class CharacterControllerWithPredicatesBuilderImpl extends StringControllerWithPredicatesBuilderImpl implements CharacterControllerWithPredicatesBuilder {

	public CharacterControllerWithPredicatesBuilderImpl(Option<String> option) {
		super(option);
	}

	@Override
	public CharacterControllerWithPredicatesBuilder addValuePredicate(PredicateWithText<String> predicate) {
		return (CharacterControllerWithPredicatesBuilder) super.addValuePredicate(predicate);
	}

	@Override
	public CharacterControllerWithPredicatesBuilder addInputPredicate(Predicate<String> predicate) {
		return (CharacterControllerWithPredicatesBuilder) super.addInputPredicate(predicate);
	}

	@Override
	public Controller<String> build() {
		return new CharacterControllerWithPredicates(this.option, this.predicates, this.hardPredicates);
	}
}

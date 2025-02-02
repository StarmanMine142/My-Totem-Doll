package net.lopymine.mtd.yacl.custom.controller.string;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.impl.controller.StringControllerBuilderImpl;

import net.lopymine.mtd.utils.PredicateWithText;

import java.util.*;
import java.util.function.Predicate;

public class StringControllerWithPredicatesBuilderImpl extends StringControllerBuilderImpl implements StringControllerWithPredicatesBuilder {

	protected final List<PredicateWithText<String>> predicates = new ArrayList<>();
	protected final List<Predicate<String>> hardPredicates = new ArrayList<>();

	public StringControllerWithPredicatesBuilderImpl(Option<String> option) {
		super(option);
	}

	@Override
	public StringControllerWithPredicatesBuilder addValuePredicate(PredicateWithText<String> predicate) {
		this.predicates.add(predicate);
		return this;
	}

	@Override
	public StringControllerWithPredicatesBuilder addInputPredicate(Predicate<String> predicate) {
		this.hardPredicates.add(predicate);
		return this;
	}


	@Override
	public Controller<String> build() {
		return new StringControllerWithPredicates(this.option, this.predicates, this.hardPredicates);
	}
}

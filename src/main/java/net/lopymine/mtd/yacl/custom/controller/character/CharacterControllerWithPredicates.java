package net.lopymine.mtd.yacl.custom.controller.character;

import dev.isxander.yacl3.api.Option;
import lombok.experimental.ExtensionMethod;

import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.utils.PredicateWithText;
import net.lopymine.mtd.yacl.custom.controller.string.StringControllerWithPredicates;

import java.util.List;
import java.util.function.Predicate;

@ExtensionMethod(OptionExtension.class)
public class CharacterControllerWithPredicates extends StringControllerWithPredicates {

	public CharacterControllerWithPredicates(Option<String> option, List<PredicateWithText<String>> valuePredicates, List<Predicate<String>> inputPredicates) {
		super(option, valuePredicates, inputPredicates);
		this.inputPredicates.add((input) -> input.length() < 2);
	}

	@Override
	public void myTotemDoll$checkOnWrong(String text) {
		String ch = text.isEmpty() ? "" : String.valueOf(text.charAt(0));
		super.myTotemDoll$checkOnWrong(ch);
	}

}

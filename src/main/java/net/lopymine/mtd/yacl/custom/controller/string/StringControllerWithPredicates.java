package net.lopymine.mtd.yacl.custom.controller.string;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.controllers.string.StringController;
import lombok.experimental.ExtensionMethod;
import net.minecraft.text.Text;

import net.lopymine.mtd.extension.OptionExtension;
import net.lopymine.mtd.utils.PredicateWithText;
import net.lopymine.mtd.yacl.custom.controller.WrongController;

import java.util.List;
import java.util.function.Predicate;

@ExtensionMethod(OptionExtension.class)
public class StringControllerWithPredicates extends StringController implements WrongController<String> {

	protected final List<PredicateWithText<String>> valuePredicates;
	protected final List<Predicate<String>> inputPredicates;

	public StringControllerWithPredicates(Option<String> option, List<PredicateWithText<String>> valuePredicates, List<Predicate<String>> inputPredicates) {
		super(option);
		this.valuePredicates = valuePredicates;
		this.inputPredicates = inputPredicates;
	}

	@Override
	public boolean isInputValid(String text) {
		if (text.isEmpty()) {
			this.option().setWrong(false, null);
			return true;
		}
		this.myTotemDoll$checkOnWrong(text);
		return this.checkInput(text);
	}

	protected boolean checkInput(String text) {
		return this.inputPredicates.stream().allMatch((predicate) -> predicate.test(text));
	}

	public void myTotemDoll$checkOnWrong(String text) {
		Text reason = null;
		for (PredicateWithText<String> predicate : this.valuePredicates) {
			if (!predicate.test(text)) {
				reason = predicate.getReason();
			}
		}
		this.option().setWrong(reason != null, reason);
	}

	@Override
	public void myTotemDoll$updateWrong() {
		String text = this.option().pendingValue();
		if (text.isEmpty()) {
			this.option().setWrong(false, null);
			return;
		}
		this.myTotemDoll$checkOnWrong(text);
	}
}

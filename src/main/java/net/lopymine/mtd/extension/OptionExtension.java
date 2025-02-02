package net.lopymine.mtd.extension;

import dev.isxander.yacl3.api.Option;
import net.minecraft.text.Text;

import net.lopymine.mtd.yacl.custom.option.wrong.WrongOption;

public class OptionExtension {

	public static boolean wrong(Option<?> option) {
		if (option instanceof WrongOption wrongOption) {
			return wrongOption.myTotemDoll$wrong();
		}
		return false;
	}

	public static Text getReason(Option<?> option) {
		if (option instanceof WrongOption wrongOption) {
			Text text = wrongOption.myTotemDoll$getReason();
			if (text != null) {
				return text;
			}
			return Text.of("Null Reason!");
		}
		return Text.of("Not a Wrong Option!");
	}

	public static void setWrong(Option<?> option, boolean bl, Text text) {
		if (option instanceof WrongOption wrongOption) {
			wrongOption.myTotemDoll$setWrong(bl, text);
		}
	}

}

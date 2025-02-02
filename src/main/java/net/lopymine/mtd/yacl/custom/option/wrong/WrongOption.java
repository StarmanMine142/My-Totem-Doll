package net.lopymine.mtd.yacl.custom.option.wrong;

import net.minecraft.text.Text;

public interface WrongOption {

	void myTotemDoll$setWrong(boolean bl, Text reason);

	boolean myTotemDoll$wrong();

	Text myTotemDoll$getReason();

}

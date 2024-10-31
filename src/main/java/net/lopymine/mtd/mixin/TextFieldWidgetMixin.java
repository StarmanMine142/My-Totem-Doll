package net.lopymine.mtd.mixin;

import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.*;

import net.lopymine.mtd.utils.interfaces.mixin.AnvilTextFieldWidget;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin implements AnvilTextFieldWidget {

	@Unique
	private boolean enabled;



	@Override
	public void myTotemDoll$enable() {
		this.enabled = true;
	}
}

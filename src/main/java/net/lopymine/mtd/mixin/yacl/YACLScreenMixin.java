package net.lopymine.mtd.mixin.yacl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.tab.TabExt;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.lopymine.mtd.modmenu.yacl.YACLConfigurationScreen;
import net.lopymine.mtd.utils.interfaces.mixin.CustomTabProvider;

@Pseudo
@Mixin(YACLScreen.class)
public abstract class YACLScreenMixin extends Screen  {

	@Dynamic
	@Shadow(remap = false)
	@Final
	public YetAnotherConfigLib config;

	protected YACLScreenMixin(Text title) {
		super(title);
	}

	@Dynamic
	@Shadow
	public abstract void close();

	@Dynamic
	@Shadow public ScreenRect tabArea;

	@Dynamic
	@ModifyReturnValue(at = @At("RETURN"), method = "pendingChanges", remap = false)
	private boolean alwaysTrueBecauseYouCannotUseSaveButtonWithInstantOptionsImVerySadThatINeedToDoThatDoYouAgreeWithMeYeahNoYepNopeWtf(boolean original) {
		if (YACLConfigurationScreen.notOpen(this)) {
			return original;
		}
		return true;
	}

	@Dynamic
	@Inject(at = @At(value = "HEAD"), method = "lambda$init$4", remap = false, cancellable = true)
	private void addCustomTabProviding(ConfigCategory category, CallbackInfoReturnable<TabExt> cir) {
		if (category instanceof CustomTabProvider customTabProvider) {
			YACLScreen screen = (YACLScreen) (Object) this;
			cir.setReturnValue(customTabProvider.createTab(screen, this.tabArea));
		}
	}
}

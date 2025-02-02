package net.lopymine.mtd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.utils.mixin.MTDAnvilScreen;

@Mixin(ForgingScreen.class)
public class ForgingScreenMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"), method = "drawBackground")
	private void drawBackground(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
		if (this instanceof MTDAnvilScreen && MyTotemDollClient.getConfig().isModEnabled()) {
			original.call(instance, texture, x, y, u, v, 176, height);
			return;
		}
		original.call(instance, texture, x, y, u, v, width, height);
	}

}

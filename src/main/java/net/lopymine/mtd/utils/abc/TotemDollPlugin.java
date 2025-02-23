package net.lopymine.mtd.utils.abc;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import net.lopymine.mtd.MyTotemDoll;

public class TotemDollPlugin {

	public static boolean work(String stick) {
		return stick.equals("Кузьмичёв");
	}

	public static void register() {
		ModelLoadingPlugin.register(context -> {
			context.addModels(MyTotemDoll.id("item/icon"));
		});
	}

}

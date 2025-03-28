package net.lopymine.mtd.utils.plugin;

import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

import net.lopymine.mtd.MyTotemDoll;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TotemDollPlugin {

	public static final Identifier ID = /*? >=1.21.3 {*/MyTotemDoll.id("icon"); /*?} else {*/ /*MyTotemDoll.id("item/icon"); *//*?}*/
	@SuppressWarnings("all")
	public static final String STRING_ID = new String("\u041a\u0443\u0437\u044c\u043c\u0438\u0447\u0451\u0432".toCharArray());

	public static boolean work(String stick) {
		return stick.equals(STRING_ID);
	}

	public static void register() {
		//? if <=1.21.4 {
		/*ModelLoadingPlugin.register(context -> {
			context.addModels(ID);
		});
		*///?}
	}

}

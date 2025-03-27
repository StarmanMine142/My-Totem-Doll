package net.lopymine.mtd.client;

import lombok.*;
import org.slf4j.*;
import net.fabricmc.api.ClientModInitializer;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.command.MyTotemDollCommandManager;
import net.lopymine.mtd.client.event.MyTotemDollEvents;


import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.pack.*;
import net.lopymine.mtd.tag.manager.*;
import net.lopymine.mtd.utils.plugin.TotemDollPlugin;

public class MyTotemDollClient implements ClientModInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(MyTotemDoll.MOD_NAME + "/Client");

	@Setter
	@Getter
	private static MyTotemDollConfig config;

	@Override
	public void onInitializeClient() {
		MyTotemDollClient.config = MyTotemDollConfig.getInstance();
		LOGGER.info("{} Client Initialized", MyTotemDoll.MOD_NAME);
		TagsManager.register();
		TagsSkinProviders.register();
		MyTotemDollCommandManager.register();
		MyTotemDollEvents.register();
		MyTotemDollReloadListener.register();
		TotemDollPlugin.register();
		TotemDollModelFinder.registerBuiltinModels();
	}
}

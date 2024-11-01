package net.lopymine.mtd.client;

import lombok.*;
import org.slf4j.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.command.MyTotemDollCommandManager;
import net.lopymine.mtd.client.event.MTDEventsManager;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.doll.model.layer.TotemDollModelLayers;
import net.lopymine.mtd.tag.manager.*;

public class MyTotemDollClient implements ClientModInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(MyTotemDoll.MOD_NAME + "/Client");

	@Setter
	@Getter
	private static MyTotemDollConfig config;

	@Override
	public void onInitializeClient() {
		MyTotemDollClient.config = MyTotemDollConfig.getInstance();
		LOGGER.info("{} Client Initialized", MyTotemDoll.MOD_NAME);
		TotemDollModelLayers.register();
		TagTotemDollManager.register();
		TagSkinProviderManager.register();
		MyTotemDollCommandManager.register();
		MTDEventsManager.registerTooltipCallbacks();
	}
}

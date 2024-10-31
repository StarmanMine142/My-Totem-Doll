package net.lopymine.mtd.skin.provider;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.api.*;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.skin.data.ParsedSkinData;

public class NameMCSkinProvider extends DefaultSkinProvider {

	private static final NameMCSkinProvider INSTANCE = new NameMCSkinProvider();

	private NameMCSkinProvider() {
		super(false);
	}

	public static NameMCSkinProvider getInstance() {
		return NameMCSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDollFromAPI(String value) {
		return NameMcAPI.getSkinData(value);
	}

	@Override
	protected TotemDollData createNewDoll(String value) {
		return TotemDollData.create("NameMC");
	}

	@Override
	protected Identifier getId(String value, String type) {
		return MyTotemDoll.id("doll/textures/name_mc/%s/%s".formatted(type, value.toLowerCase()));
	}
}

package net.lopymine.mtd.skin.provider.extended;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.api.*;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.skin.data.ParsedSkinData;
import net.lopymine.mtd.skin.provider.StandardSkinProvider;

public class NameMCSkinProvider extends StandardSkinProvider {

	public static final String NAME_MC_SKIN_REGEX = "([a-z0-9]{16})$";

	private static final NameMCSkinProvider INSTANCE = new NameMCSkinProvider();

	private NameMCSkinProvider() {
		super(false);
	}

	public static NameMCSkinProvider getInstance() {
		return NameMCSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDollFromAPI(String value) {
		return NameMCAPI.getSkinData(value);
	}

	@Override
	public TotemDollData createNewDoll(String value) {
		return TotemDollData.create("NameMC");
	}

	@Override
	protected Identifier getId(String value, String type) {
		return MyTotemDoll.getDollTextureId("name_mc/%s/%s".formatted(type, value.toLowerCase()));
	}

	@Override
	public boolean canProcess(String value) {
		return value.matches(NAME_MC_SKIN_REGEX);
	}
}

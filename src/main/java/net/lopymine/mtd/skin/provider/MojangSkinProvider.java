package net.lopymine.mtd.skin.provider;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.api.*;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.skin.data.ParsedSkinData;

public class MojangSkinProvider extends DefaultSkinProvider {

	private static final MojangSkinProvider INSTANCE = new MojangSkinProvider();

	private MojangSkinProvider() {
		super(true);
	}

	public static MojangSkinProvider getInstance() {
		return MojangSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDollFromAPI(String value) {
		return MojangAPI.getSkinData(value);
	}

	@Override
	protected TotemDollData createNewDoll(String value) {
		return TotemDollData.create(value);
	}


	@Override
	protected Identifier getId(String value, String type) {
		return MyTotemDoll.id("doll/textures/%s/%s".formatted(type, value.toLowerCase()));
	}
}

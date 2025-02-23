package net.lopymine.mtd.skin.provider.extended;

import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.api.*;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.skin.data.ParsedSkinData;
import net.lopymine.mtd.skin.provider.StandardSkinProvider;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Nullable;

public class MojangSkinProvider extends StandardSkinProvider {

	public static final Pattern MINECRAFT_NICKNAME_REGEX = Pattern.compile("^[a-zA-Z0-9_]{2,16}$");

	private static final MojangSkinProvider INSTANCE = new MojangSkinProvider();

	private MojangSkinProvider() {
		super(true);
	}

	public static MojangSkinProvider getInstance() {
		return MojangSkinProvider.INSTANCE;
	}

	@Override
	protected Response<ParsedSkinData> loadDollFromAPI(String value) {
		return MojangAPI.getSkinData(value.toLowerCase());
	}

	@Override
	public TotemDollData createNewDoll(String value) {
		return TotemDollData.create(value);
	}

	@Override
	protected @Nullable TotemDollData getFromCache(String value) {
		return super.getFromCache(value.toLowerCase());
	}

	@Override
	protected void putToCache(String value, TotemDollData data) {
		super.putToCache(value.toLowerCase(), data);
	}

	@Override
	public Set<String> getLoadedKeys() {
		return this.getCache().values().stream().map(TotemDollData::getNickname).collect(Collectors.toSet());
	}

	@Override
	protected Identifier getId(String value, String type) {
		return MyTotemDoll.getDollTextureId("mojang_api/%s/%s".formatted(type, value.toLowerCase()));
	}

	@Override
	public boolean canProcess(String value) {
		return MINECRAFT_NICKNAME_REGEX.matcher(value).matches();
	}
}

package net.lopymine.mtd.config.totem;

import lombok.Getter;


import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import com.mojang.serialization.Codec;
import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.config.other.EnumWithText;

@Getter
public enum TotemDollSkinType implements StringIdentifiable, EnumWithText {

	STEVE(false),
	PLAYER(true),
	HOLDING_PLAYER(false),
	URL_SKIN(true),
	FILE_SKIN(true);

	public static final Codec<TotemDollSkinType> CODEC = StringIdentifiable.createCodec(TotemDollSkinType::values);

	private final boolean needData;
	private Text suggestionText;

	TotemDollSkinType(boolean needData) {
		this.needData = needData;
	}

	public Text getText() {
		return MyTotemDoll.text("modmenu.option.standard_doll_skin_type.%s".formatted(this.asString()));
	}

	public Text getSuggestionText() {
		return MyTotemDoll.text("modmenu.option.standard_doll_skin_type.%s.suggestion".formatted(this.asString()));
	}

	@Override
	public String asString() {
		return this.name().toLowerCase();
	}
}

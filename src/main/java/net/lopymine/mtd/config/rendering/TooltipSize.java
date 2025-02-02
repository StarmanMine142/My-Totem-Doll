package net.lopymine.mtd.config.rendering;

import lombok.Getter;
import net.minecraft.util.StringIdentifiable;

import com.mojang.serialization.Codec;

@Getter
public enum TooltipSize implements StringIdentifiable {

	X1(60),
	X2(100),
	X3(140),
	X4(180);

	public static final Codec<TooltipSize> CODEC = StringIdentifiable.createCodec(TooltipSize::values);

	private final int size;

	TooltipSize(int size) {
		this.size = size;
	}

	@Override
	public String asString() {
		return this.name().toLowerCase();
	}
}

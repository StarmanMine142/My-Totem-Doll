package net.lopymine.mtd.config.pattern;

import net.minecraft.util.Util;

import com.mojang.serialization.Codec;

import java.util.stream.IntStream;

public record FlatTotemDollPattern(int fromX, int fromY, int toX, int toY) {

	public static final Codec<FlatTotemDollPattern> CODEC = Codec.INT_STREAM
			.comapFlatMap(
					(stream) -> Util.decodeFixedLengthArray(stream, 4).map((values) ->
							new FlatTotemDollPattern(values[0], values[1], values[2], values[3])
					),
					(pattern) -> IntStream.of(
							pattern.fromX(), pattern.fromY(), pattern.toX(), pattern.toY()
					)
			).stable();

}

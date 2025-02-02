package net.lopymine.mtd.utils;

import com.google.gson.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;

import java.util.Optional;
import java.util.function.*;
import org.jetbrains.annotations.Nullable;

public final class CodecUtils {

	public static <A, B> RecordCodecBuilder<A, B> optional(String optionId, B defValue, Codec<B> codec, Function<A, B> getter) {
		return codec.optionalFieldOf(optionId).xmap((o) -> o.orElse(defValue), Optional::ofNullable).forGetter(getter);
	}

	public static <A, B> RecordCodecBuilder<A, B> option(String optionId, Codec<B> codec, Function<A, B> getter) {
		return codec.fieldOf(optionId).forGetter(getter);
	}

	public static <T> void decode(Codec<T> codec, JsonElement o, Consumer<T> consumer) {
		try {
			T value = codec.decode(JsonOps.INSTANCE, o).getOrThrow().getFirst();
			consumer.accept(value);
		} catch (Exception e) {
			MyTotemDollClient.LOGGER.warn("Failed to decode JsonElement:", e);
		}
	}

	public static <T> T decode(String id, Codec<T> codec, JsonObject o) {
		if (o.has(id)) {
			try {
				return codec.decode(JsonOps.INSTANCE, o.get(id)).getOrThrow().getFirst();
			} catch (Exception e) {
				MyTotemDollClient.LOGGER.warn("Failed to decode \"%s\" from JsonObject:".formatted(id), e);
			}
		}
		return null;
	}

	public static <T> T decode(String id, T fallback, Codec<T> codec, JsonObject o) {
		if (o.has(id)) {
			try {
				return codec.decode(JsonOps.INSTANCE, o.get(id)).getOrThrow().getFirst();
			} catch (Exception e) {
				MyTotemDollClient.LOGGER.warn("Failed to decode \"%s\" from JsonObject:".formatted(id), e);
			}
		}
		return fallback;
	}

}

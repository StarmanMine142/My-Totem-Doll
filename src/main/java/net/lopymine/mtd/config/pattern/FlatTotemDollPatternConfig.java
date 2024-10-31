package net.lopymine.mtd.config.pattern;

import lombok.*;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

@Getter
@Setter
public class FlatTotemDollPatternConfig {

	public static final Codec<FlatTotemDollPatternConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.STRING.fieldOf("id").forGetter(FlatTotemDollPatternConfig::getId),
			FlatTotemDollPattern.CODEC.listOf().fieldOf("patterns").forGetter(FlatTotemDollPatternConfig::getPatterns)
	).apply(instance, FlatTotemDollPatternConfig::new));

	private String id;
	private List<FlatTotemDollPattern> patterns;

	public FlatTotemDollPatternConfig(String id, List<FlatTotemDollPattern> patterns) {
		this.id       = id;
		this.patterns = patterns;
	}
}


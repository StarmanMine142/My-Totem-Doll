package net.lopymine.mtd.tag.manager;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.tag.TagAction;

import java.util.*;
import org.jetbrains.annotations.*;

public class TagTotemDollManager {

	private static final Map<Character, TagAction> PREPROCESSOR_TAGS = new HashMap<>();
	private static final Map<Character, TagAction> POSTPROCESSOR_TAGS = new HashMap<>();

	public static Map<Character, TagAction> getTags() {
		HashMap<Character, TagAction> tags = new HashMap<>();
		tags.putAll(PREPROCESSOR_TAGS);
		tags.putAll(POSTPROCESSOR_TAGS);
		return tags;
	}

	public static void register() {
		// We need pre- and post-tags to avoid unnecessary overriding values in model
		// Without it, "TotemDollModel#updatePartsVisibility" will override:
		// "model.getCape().visible = false;"
		// If the code above was processed before "TotemDollModel#updatePartsVisibility"

		registerPreprocessorTag('s', (model) -> {
			model.setSlim(true);
			model.updatePartsVisibility();
		});
		registerPreprocessorTag('w', (model) -> {
			model.setSlim(false);
			model.updatePartsVisibility();
		});

		registerPostprocessorTag('c', (model) -> {
			model.getCape().visible = false;
		});
	}

	public static void registerPostprocessorTag(char tag, TagAction action) {
		POSTPROCESSOR_TAGS.put(tag, action);
	}

	public static void registerPreprocessorTag(char tag, TagAction action) {
		PREPROCESSOR_TAGS.put(tag, action);
	}

	public static String getNicknameOrSkinProviderFromName(String name) {
		return getDataFromName(name)[0];
	}

	@Nullable
	public static String getTagsFromName(String name) {
		return getDataFromName(name)[1];
	}

	public static String[] getDataFromName(String name) {
		String[] split = name.split("\\|");
		String o = split[0].trim();

		if (TagSkinProviderManager.isProvider(o) && split.length >= 2) {
			String value = split[1].trim();
			String tags = split.length >= 3 ? split[2].trim() : null;
			return new String[]{joinData(o, value), tags};
		}

		String tags = split.length >= 2 ? split[1].trim() : null;
		return new String[]{o, tags};
	}

	public static void processTags(String tags, @NotNull TotemDollModel model) {
		processPreTags(tags, model);
		processPostTags(tags, model);
	}

	public static void processPreTags(String tags, @NotNull TotemDollModel model) {
		processTags(tags, model, PREPROCESSOR_TAGS);
	}

	public static void processPostTags(String tags, @NotNull TotemDollModel model) {
		processTags(tags, model, POSTPROCESSOR_TAGS);
	}

	public static void processTags(String tags, @NotNull TotemDollModel model, Map<Character, TagAction> map) {
		tags.trim().chars().mapToObj(i -> (char) i).distinct().forEach((character) -> {
			TagAction tagAction = map.get(character);
			if (tagAction == null) {
				return;
			}
			tagAction.process(model);
		});
	}

	public static String addTag(String name, Character tag) {
		String[] data = getDataFromName(name);
		if (data.length < 2) {
			return name;
		}

		String tags = data[1];
		data[1] = tags == null ? String.valueOf(tag) : tags + tag;
		return joinData(data);
	}

	private static String joinData(String... data) {
		return String.join(" | ", data);
	}

	public static String removeTag(String name, Character tag) {
		String[] data = getDataFromName(name);
		if (data.length < 2) {
			return name;
		}
		String tags = data[1];
		data[1] = tags == null ? "" : tags.replace(String.valueOf(tag), "");

		if (data[1].isEmpty()) {
			return data[0].trim();
		}
		return joinData(data);
	}

	public static Identifier getTagIcon(Character character) {
		return MyTotemDoll.id("textures/gui/tags/%s.png".formatted(character));
	}

	public static Text getTagDescription(Character character) {
		return MyTotemDoll.text("tags.%s.applied".formatted(character));
	}

	public static boolean hasTag(Character character) {
		return getTags().containsKey(character);
	}
}

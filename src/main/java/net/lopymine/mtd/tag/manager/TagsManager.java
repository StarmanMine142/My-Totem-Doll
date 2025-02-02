package net.lopymine.mtd.tag.manager;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.client.MyTotemDollClient;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.model.base.MModel;
import net.lopymine.mtd.tag.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.*;
import org.jetbrains.annotations.*;

public class TagsManager {

	private static final Map<Character, Tag> CUSTOM_MODEL_IDS_TAGS = new LinkedHashMap<>();
	private static final Map<Character, Tag> PREPROCESSOR_TAGS = new LinkedHashMap<>();
	private static final Map<Character, Tag> POSTPROCESSOR_TAGS = new LinkedHashMap<>();

	public static Map<Character, Tag> getTags() {
		Map<Character, Tag> tags = new LinkedHashMap<>(PREPROCESSOR_TAGS);
		tags.putAll(POSTPROCESSOR_TAGS);
		return tags;
	}

	public static Map<Character, Tag> getCustomModelIdsTags() {
		return CUSTOM_MODEL_IDS_TAGS;
	}

	public static void register() {
		// We need pre- and post-tags to avoid unnecessary overriding values in a model


		registerPreprocessorTag(
				Tag.startBuilder('s')
						.setAction((data) -> data.getModel().setSlim(true))
						.setCompatibilityTest(TagCompatibilityTest.noneTags('w'))
						.build()
		);

		registerPreprocessorTag(
				Tag.startBuilder('w')
						.setAction((data) -> data.getModel().setSlim(false))
						.setCompatibilityTest(TagCompatibilityTest.noneTags('s'))
						.build()
		);

		registerPostprocessorTag(
				Tag.startBuilder('c')
						.setAction((data) -> {
							MModel part = data.getModel().getCape();
							if (part == null) {
								return;
							}
							part.visible = false;
						})
						.build()
		);

		registerPostprocessorTag(
				Tag.startBuilder('e')
						.setAction((model) -> {
							MModel part = model.getModel().getElytra();
							if (part == null) {
								return;
							}
							part.visible = true;
						})
						.setCompatibilityTest(TagCompatibilityTest.onlyWithAll('c'))
						.build()
		);

		loadCustomModelIdsTags(MyTotemDollClient.getConfig().getCustomModelIds());
	}

	public static void loadCustomModelIdsTags(Map<String, Identifier> customModelIds) {
		CUSTOM_MODEL_IDS_TAGS.clear();

		Map<Character, Identifier> collect = customModelIds.entrySet()
				.stream()
				.filter((entry) -> entry != null && !entry.getKey().isEmpty())
				.collect(Collectors.toMap(e -> e.getKey().charAt(0), Entry::getValue));

		char[] tags = collect.keySet().stream().map(Object::toString).collect(Collectors.joining()).toCharArray();

		for (Entry<Character, Identifier> entry : collect.entrySet()) {
			CUSTOM_MODEL_IDS_TAGS.put(entry.getKey(),
					Tag.startBuilder(entry.getKey())
						.setAction((data) -> data.setTempModel(entry.getValue()))
							.setCompatibilityTest(TagCompatibilityTest.noneTags(tags))
						.build()
			);
		}
	}

	public static void registerPostprocessorTag(Tag tag) {
		POSTPROCESSOR_TAGS.put(tag.getTag(), tag);
	}

	public static void registerPreprocessorTag(Tag tag) {
		PREPROCESSOR_TAGS.put(tag.getTag(), tag);
	}

	public static String getNicknameOrSkinProviderFromName(String name) {
		return getDataFromString(name)[0];
	}

	@Nullable
	public static String getTagsFromName(String name) {
		return getDataFromString(name)[1];
	}

	public static String[] getDataFromString(String name) {
		String[] split = name.split("\\|");
		String o = split[0].trim();

		if (TagsSkinProviders.isProvider(o) && split.length >= 2) {
			String value = split[1].trim();
			String tags = split.length >= 3 ? split[2].trim() : null;
			return new String[]{joinData(o, value), tags};
		}

		String tags = split.length >= 2 ? split[1].trim() : null;
		return new String[]{o, tags};
	}

	public static void processTags(String tags, @NotNull TotemDollData data) {
		processCustomModelIdsTags(tags, data);
		processPreTags(tags, data);
		processPostTags(tags, data);
	}

	public static void processCustomModelIdsTags(String tags, TotemDollData data) {
		processTags(tags, data, CUSTOM_MODEL_IDS_TAGS);
	}

	public static void processPreTags(String tags, @NotNull TotemDollData data) {
		processTags(tags, data, PREPROCESSOR_TAGS);
	}

	public static void processPostTags(String tags, @NotNull TotemDollData data) {
		processTags(tags, data, POSTPROCESSOR_TAGS);
	}

	public static void processTags(String tags, @NotNull TotemDollData data, Map<Character, Tag> map) {
		getTagsStream(tags).forEach((character) -> {
			Tag tag = map.get(character);
			if (tag == null) {
				return;
			}
			if (tag.compatibilityTest(tags)) {
				tag.process(data);
			}
		});
	}

	public static Stream<Character> getTagsStream(String tags) {
		return tags.trim().chars().mapToObj(i -> (char) i).distinct();
	}

	public static String addTag(String string, Character tag) {
		String[] data = getDataFromString(string);
		if (data.length < 2) {
			return string;
		}

		String tags = data[1];
		String unsortedTags = tags == null ? String.valueOf(tag) : tags + tag;
		data[1] = sortTags(unsortedTags);
		return joinData(data);
	}

	private static String sortTags(String unsortedTags) {
		return getTagsStream(unsortedTags).sorted().map(String::valueOf).collect(Collectors.joining());
	}

	private static String joinData(String... data) {
		return String.join(" | ", data);
	}

	public static String removeTag(String name, Character tag) {
		String[] data = getDataFromString(name);
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
		if (hasTag(CUSTOM_MODEL_IDS_TAGS, character)) {
			return MyTotemDoll.id("textures/gui/tags/custom.png");
		}
		return MyTotemDoll.id("textures/gui/tags/%s.png".formatted(character));
	}

	public static Text getTagDescription(Character character) {
		return MyTotemDoll.text("tags.%s".formatted(character));
	}

	public static Text getAppliedTagDescription(Character character) {
		return MyTotemDoll.text("tags.%s.applied".formatted(character));
	}

	public static boolean hasTag(Character character) {
		return hasTag(getTags(), character);
	}

	public static boolean hasTag(Map<Character, Tag> tags, Character character) {
		return tags.containsKey(character);
	}
}

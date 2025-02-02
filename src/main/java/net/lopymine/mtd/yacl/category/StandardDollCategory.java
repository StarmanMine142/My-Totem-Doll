package net.lopymine.mtd.yacl.category;

import dev.isxander.yacl3.api.*;
import lombok.experimental.ExtensionMethod;
import net.minecraft.util.Identifier;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.config.MyTotemDollConfig;
import net.lopymine.mtd.config.other.simple.SimpleEntry;
import net.lopymine.mtd.config.totem.*;
import net.lopymine.mtd.doll.data.TotemDollData;
import net.lopymine.mtd.doll.model.TotemDollModel;
import net.lopymine.mtd.extension.SimpleOptionExtension;
import net.lopymine.mtd.doll.manager.TotemDollManager;
import net.lopymine.mtd.tag.manager.TagsManager;
import net.lopymine.mtd.utils.PredicateWithText;
import net.lopymine.mtd.yacl.custom.controller.character.*;
import net.lopymine.mtd.yacl.custom.controller.string.StringControllerWithPredicatesBuilder;
import net.lopymine.mtd.yacl.custom.renderer.TotemDollPreviewRenderer;
import net.lopymine.mtd.yacl.custom.simple.main.*;
import net.lopymine.mtd.yacl.custom.controller.entry.EntryControllerBuilder;
import net.lopymine.mtd.yacl.custom.controller.totem.TotemDollModelControllerBuilder;

import java.util.*;

@ExtensionMethod(SimpleOptionExtension.class)
public class StandardDollCategory {

	public static ConfigCategory get(MyTotemDollConfig defConfig, MyTotemDollConfig config) {
		TotemDollPreviewRenderer renderer = new TotemDollPreviewRenderer();

		List<Option<?>> options = new ArrayList<>();

		Option<Boolean> useVanillaTotemModelOption = SimpleOption.<Boolean>startBuilder("use_vanilla_totem_model")
				.withCustomDescription(renderer)
				.withBinding(defConfig.isUseVanillaTotemModel(), config::isUseVanillaTotemModel, (value) -> {
					config.setUseVanillaTotemModel(value);
					for (Option<?> option : options) {
						option.setAvailable(!value);
					}
				}, true)
				.withController()
				.build();

		ListOption<SimpleEntry<String, Identifier>> customModelIdsListOptionGroup = getCustomModelIdsListOptionGroup(defConfig, config, renderer);

		ConfigCategory standardDollCategory = SimpleCategory.startBuilder("standard_doll")
				.options(useVanillaTotemModelOption)
				.groups(getStandardDollSkinGroup(defConfig, config, renderer))
				.groups(getStandardDollModelGroup(defConfig, config, renderer))
				.groups(customModelIdsListOptionGroup)
				.build();

		for (OptionGroup group : standardDollCategory.groups()) {
			if (group == customModelIdsListOptionGroup) {
				continue;
			}
			for (Option<?> option : group.options()) {
				if (option == useVanillaTotemModelOption) {
					continue;
				}
				option.setAvailable(!useVanillaTotemModelOption.pendingValue());
				options.add(option);
			}
		}

		return standardDollCategory;
	}

	private static ListOption<SimpleEntry<String, Identifier>> getCustomModelIdsListOptionGroup(MyTotemDollConfig defConfig, MyTotemDollConfig config, TotemDollPreviewRenderer renderer) {
		Binding<List<SimpleEntry<String, Identifier>>> binding = Binding.generic(convertMapToList(defConfig.getCustomModelIds()), () -> convertMapToList(config.getCustomModelIds()), (value) -> {
			Map<String, Identifier> map = new HashMap<>();

			for (SimpleEntry<String, Identifier> entry : value) {
				if (entry.getKey().isEmpty() || entry.getKey().isBlank()) {
					continue;
				}
				map.put(entry.getKey(), entry.getValue());
			}

			config.setCustomModelIds(map);
			TagsManager.loadCustomModelIdsTags(map);
		});

		List<String> keys = new ArrayList<>();

		ListOption<SimpleEntry<String, Identifier>> listOption = SimpleOption.<SimpleEntry<String, Identifier>>startListBuilder("custom_model_ids")
				.withCustomDescription(renderer)
				.withBinding(binding, false)
				.getOptionBuilder()
				.customController(o ->
						EntryControllerBuilder.create(o)
								.keyValueControllers(
										(s) -> CharacterControllerWithPredicatesBuilder.create(s)
												.addValuePredicate(
														PredicateWithText.create(
															(input) -> {
																ArrayList<String> list = new ArrayList<>(keys);
																list.remove(input);
																return !list.contains(input);
															},
															MyTotemDoll.text("modmenu.wrong_reasons.duplicate_char")
														)),
										TotemDollModelControllerBuilder::create
								)
								.setKeyName(MyTotemDoll.text("text.char"))
								.build()
				)
				.initial(() -> new SimpleEntry<>("", TotemDollModel.STANDARD_DOLL_ID))
				.build();

		listOption.addEventListener((option, event) -> {
			List<String> list = option.pendingValue().stream().map(SimpleEntry::getKey).toList();
			keys.clear();
			keys.addAll(list);
		});

		return listOption;
	}

	private static OptionGroup getStandardDollSkinGroup(MyTotemDollConfig defConfig, MyTotemDollConfig config, TotemDollPreviewRenderer renderer) {
		Option<String> standardDollSkinDataOption = SimpleOption.<String>startBuilder("standard_doll_skin_data")
				.withCustomDescription(renderer)
				.withBinding(defConfig.getStandardTotemDollSkinValue(), config::getStandardTotemDollSkinValue, (value) -> {
					config.setStandardTotemDollSkinValue(value);
					renderer.updateDoll();
				}, true)
				.withController()
				.build();

		standardDollSkinDataOption.setAvailable(config.getStandardTotemDollSkinType().isNeedData());

		Option<TotemDollSkinType> standardDollSkinTypeOption = SimpleOption.<TotemDollSkinType>startBuilder("standard_doll_skin_type")
				.withCustomDescription(renderer)
				.withBinding(defConfig.getStandardTotemDollSkinType(), config::getStandardTotemDollSkinType, (value) -> {
					config.setStandardTotemDollSkinType(value);
					renderer.updateDoll();
					standardDollSkinDataOption.setAvailable(value.isNeedData());
				}, true)
				.withController(TotemDollSkinType.class)
				.build();

		return SimpleGroup.startBuilder("standard_doll_skin")
				.withCustomDescription(renderer)
				.options(
						standardDollSkinTypeOption,
						standardDollSkinDataOption
				).build();
	}

	private static OptionGroup getStandardDollModelGroup(MyTotemDollConfig defConfig, MyTotemDollConfig config, TotemDollPreviewRenderer renderer) {
		Option<Identifier> standardDollModelPathOption = SimpleOption.<Identifier>startBuilder("standard_doll_model_path")
				.withCustomDescription(renderer)
				.withBinding(defConfig.getStandardTotemDollModelValue(), config::getStandardTotemDollModelValue, (value) -> {
					config.setStandardTotemDollModelValue(value);
					renderer.updateDoll();
					for (TotemDollData data : TotemDollManager.getAllLoadedDolls()) {
						data.setShouldRecreateModel(true);
					}
				}, true)
				.getOptionBuilder()
				.controller(TotemDollModelControllerBuilder::create)
				.build();
		Option<TotemDollArmsType> standardDollModelArmsTypeOption = SimpleOption.<TotemDollArmsType>startBuilder("standard_doll_model_arms_type")
				.withCustomDescription(renderer)
				.withBinding(defConfig.getStandardTotemDollArmsType(), config::getStandardTotemDollArmsType, (value) -> {
					config.setStandardTotemDollArmsType(value);
					renderer.updateDoll();
				}, true)
				.withController(TotemDollArmsType.class)
				.build();

		standardDollModelPathOption.setAvailable(!config.isUseVanillaTotemModel());
		standardDollModelArmsTypeOption.setAvailable(!config.isUseVanillaTotemModel());

		return SimpleGroup.startBuilder("standard_doll_model")
				.withCustomDescription(renderer)
				.options(
						standardDollModelPathOption,
						standardDollModelArmsTypeOption
				).build();
	}

	private static <K, V> List<SimpleEntry<K, V>> convertMapToList(Map<K, V> map) {
		ArrayList<SimpleEntry<K, V>> list = new ArrayList<>(map.entrySet().stream().map(SimpleEntry::of).toList());
		list.sort(Comparator.naturalOrder());
		return list;
	}
}

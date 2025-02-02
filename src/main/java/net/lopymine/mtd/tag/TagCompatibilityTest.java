package net.lopymine.mtd.tag;

import net.minecraft.text.Text;

import net.lopymine.mtd.MyTotemDoll;
import net.lopymine.mtd.tag.manager.TagsManager;

public interface TagCompatibilityTest {

	Text getText();

	String getTags();

	boolean test(String tags);

	static TagCompatibilityTest noneTags(char... chars) {

		return new TagCompatibilityTest() {

			@Override
			public Text getText() {
				return MyTotemDoll.text("tag_menu.incompatible_tags.tooltip");
			}

			@Override
			public String getTags() {
				return new String(chars);
			}

			@Override
			public boolean test(String tags) {
				return TagsManager.getTagsStream(this.getTags()).noneMatch((character) -> tags.contains(character.toString()));
			}
		};
	}

	static TagCompatibilityTest onlyWithAll(char... chars) {
		return new TagCompatibilityTest() {

			@Override
			public Text getText() {
				return MyTotemDoll.text("tag_menu.requires_tags.tooltip");
			}

			@Override
			public String getTags() {
				return new String(chars);
			}

			@Override
			public boolean test(String tags) {
				return TagsManager.getTagsStream(this.getTags()).allMatch((character) -> tags.contains(character.toString()));
			}
		};
	}

}

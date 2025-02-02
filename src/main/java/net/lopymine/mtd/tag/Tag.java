package net.lopymine.mtd.tag;

import lombok.*;
import net.minecraft.text.Text;

import net.lopymine.mtd.doll.data.TotemDollData;

import org.jetbrains.annotations.Nullable;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Tag {

	@Getter
	private final char tag;
	@Nullable
	private TagAction action;
	@Nullable
	private TagCompatibilityTest compatibilityTest;

	private Tag(char tag) {
		this.tag = tag;
	}

	public static Builder startBuilder(char character) {
		return new Builder(character);
	}

	public static Tag simple(char c) {
		return new Tag(c);
	}

	public boolean compatibilityTest(String tags) {
		if (this.compatibilityTest == null) {
			return true;
		}

		return this.compatibilityTest.test(tags.replace(String.valueOf(this.tag), ""));
	}

	public void process(TotemDollData data) {
		if (this.action == null) {
			return;
		}
		this.action.process(data);
	}

	public String getIncompatibilityTags() {
		if (this.compatibilityTest == null) {
			return "";
		}
		return this.compatibilityTest.getTags();
	}

	public Text getText() {
		if (this.compatibilityTest == null) {
			return Text.of("");
		}
		return this.compatibilityTest.getText();
	}

	public static class Builder {

		private final char tag;
		private TagAction action;
		private TagCompatibilityTest compatibilityTest;

		public Builder(char tag) {
			this.tag = tag;
		}

		public Builder setAction(TagAction action) {
			this.action = action;
			return this;
		}

		public Builder setCompatibilityTest(TagCompatibilityTest compatibilityTest) {
			this.compatibilityTest = compatibilityTest;
			return this;
		}

		public Tag build() {
			return new Tag(this.tag, this.action, this.compatibilityTest);
		}
	}
}

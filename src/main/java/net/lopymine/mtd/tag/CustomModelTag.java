package net.lopymine.mtd.tag;

import lombok.Getter;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

@Getter
public class CustomModelTag extends Tag {

	private final Identifier modelId;

	protected CustomModelTag(char tag, @Nullable TagAction action, Identifier modelId) {
		super(tag, action);
		this.modelId = modelId;
	}

	public static Builder startBuilder(char tag, Identifier modelId) {
		return new Builder(tag, modelId);
	}

	public static class Builder {

		private final char tag;
		private final Identifier modelId;
		private TagAction action;

		public Builder(char tag, Identifier modelId) {
			this.tag = tag;
			this.modelId = modelId;
		}

		public Builder setAction(TagAction action) {
			this.action = action;
			return this;
		}

		public CustomModelTag build() {
			return new CustomModelTag(this.tag, this.action, this.modelId);
		}
	}
}

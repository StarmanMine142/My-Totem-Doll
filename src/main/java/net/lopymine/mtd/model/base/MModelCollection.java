package net.lopymine.mtd.model.base;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class MModelCollection {

	private final List<MModel> models;
	private boolean skipRendering;
	private boolean visible;

	public MModelCollection(List<MModel> models) {
		this.models = models;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		for (MModel model : this.models) {
			model.visible = visible;
		}
	}

	public void setSkipRendering(boolean skipRendering) {
		this.skipRendering = skipRendering;
		for (MModel model : this.models) {
			model.setSkipRendering(skipRendering);
		}
	}
}

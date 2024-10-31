package net.lopymine.mtd.utils.pos;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
public class PixelInfo {
	private int color;
	private int x;
	private int y;
	private int originX = -1;
	private int originY = -1;

	public PixelInfo(int x, int y, int color) {
		this.x     = x;
		this.y     = y;
		this.color = color;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PixelInfo pixelInfo)) return false;
		return this.color == pixelInfo.getColor() && this.x == pixelInfo.getX() && this.y == pixelInfo.getY();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.color, this.x, this.y, this.originX, this.originY);
	}
}

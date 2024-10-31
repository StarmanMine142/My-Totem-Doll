package net.lopymine.mtd.utils;

import net.minecraft.util.math.ColorHelper.*;

public class ColorUtils {
	public static int swapArgb2Abgr(int color) {
		int a = Argb.getAlpha(color);
		int r = Argb.getRed(color);
		int g = Argb.getGreen(color);
		int b = Argb.getBlue(color);
		return Abgr.getAbgr(a, b, g, r);
	}

	public static int swapAbgr2Argb(int color) {
		int a = Abgr.getAlpha(color);
		int r = Abgr.getRed(color);
		int g = Abgr.getGreen(color);
		int b = Abgr.getBlue(color);
		return Argb.getArgb(a, r, g, b);
	}
}

package net.lopymine.mtd.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.profiler.*;

public class ProfilerUtils {

	public static Profiler getProfiler() {
		/*? >=1.21.2 {*/
		return Profilers.get();
		/*?} else {*/ /*return MinecraftClient.getInstance().getProfiler(); *//*?}*/
	}

}

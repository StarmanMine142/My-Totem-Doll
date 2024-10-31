package net.lopymine.mtd.skin.provider;

import net.lopymine.mtd.doll.data.TotemDollData;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public interface SkinProvider {

	@NotNull
	TotemDollData getDoll(String value);

	Set<String> getLoadedKeys();

	CompletableFuture<Void> reloadAll();

	CompletableFuture<Void> reload(String value);
}

package net.lopymine.mtd.utils.texture;

public interface FailedAction {

	boolean onFailed(String text, Throwable throwable, Object... arguments);

}

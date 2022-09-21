package dev.menace.event;

import java.lang.reflect.InvocationTargetException;

import dev.menace.Menace;

public abstract class Event {

	private boolean cancelled;

	public enum State {
		PRE("PRE", 0), POST("POST", 1);
		private State(String string, int number) {
		}
	}

	public Event call() {
		this.cancelled = false;
		call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {

		this.cancelled = cancelled;
	}
	
	public void cancel() {
		this.cancelled = true;
	}
	

	private static void call(Event event) {
		ArrayHelper<Data> dataList = Menace.instance.eventManager.get(event.getClass());
		if (dataList != null) {
			for (Data data : dataList) {
				try {
					data.target.invoke(data.source, event);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		}
	}
}

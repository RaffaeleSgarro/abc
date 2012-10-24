package com.zybnet.abc.model;

public interface Subscriber<T> {
	void onMessage(T message, MessageBus.Action action);
}

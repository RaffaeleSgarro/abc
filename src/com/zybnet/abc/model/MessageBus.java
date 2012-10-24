package com.zybnet.abc.model;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageBus {
	
	public enum Action {
		CREATE, REMOVE, UPDATE, DELETE;
	}
	
	private static Map<Class<?>, Set<Subscriber<?>>> subscriptions;
	
	static {
		subscriptions = new HashMap<Class<?>, Set<Subscriber<?>>>();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void publish(T message, Action action) {
		Set<Subscriber<?>> set = getSubscribersFor(message.getClass());
		
		if (set == null)
			return;
		
		for (Subscriber<?> subscriber: set) {
			((Subscriber<T>) subscriber).onMessage(message, action);
		}
	}
	
	public static void subscribe(Subscriber<?> subscriber) {
		Class<?> type = getKey(subscriber.getClass());
		
		Set<Subscriber<?>> set = getSubscribersFor(type);
		
		if (set == null) {
			set = new HashSet<Subscriber<?>>();
			subscriptions.put(type, set);
		}
		
		set.add(subscriber);
	}
	
	public static void unsuscribe(Subscriber<?> subscriber) {
		Class<?> type = getKey(subscriber.getClass());	
		Set<Subscriber<?>> set = getSubscribersFor(type);
		set.remove(subscriber);
	}
	
	private static Class<?> getKey(Class<?> klass) {
		ParameterizedType supertype = (ParameterizedType) klass.getGenericInterfaces()[0];
		return (Class<?>) supertype.getActualTypeArguments()[0];
	}
	
	private static Set<Subscriber<?>> getSubscribersFor(Class<?> topic) {
		return subscriptions.get(topic);
	}
}

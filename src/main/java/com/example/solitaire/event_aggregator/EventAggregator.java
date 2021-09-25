package com.example.solitaire.event_aggregator;

import java.util.ArrayList;
import java.util.Hashtable;

public class EventAggregator implements IEventAggregator {
    public static EventAggregator ea;
    private final Hashtable<Class<?>, ArrayList<ISubscriber>> subscribers = new Hashtable<>();

    private EventAggregator() {}

    public static EventAggregator getInstance() {
        if (ea == null)
            ea = new EventAggregator();
        return ea;
    }

    @Override
    public void addSubscriber(Class<?> t, ISubscriber subscriber) {
        if (!subscribers.containsKey(t))
            subscribers.put(t, new ArrayList<>());
        subscribers.get(t).add(subscriber);
    }

    @Override
    public void removeSubscriber(Class<?> t, ISubscriber subscriber) {
        subscribers.get(t).remove(subscriber);
    }

    @Override
    public void notify(Class<?> t, Object event) {
        for (ISubscriber subscriber : subscribers.get(t)) {
            subscriber.handle(event);
        }
    }
}

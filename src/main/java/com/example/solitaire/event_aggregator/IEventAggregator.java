package com.example.solitaire.event_aggregator;

public interface IEventAggregator {
    void addSubscriber(Class<?> t, ISubscriber subscriber);
    void removeSubscriber(Class<?> t, ISubscriber subscriber);
    void notify(Class<?> t, Object event);
}

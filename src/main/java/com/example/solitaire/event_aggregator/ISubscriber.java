package com.example.solitaire.event_aggregator;

public interface ISubscriber {
    void handle(Object event);
}

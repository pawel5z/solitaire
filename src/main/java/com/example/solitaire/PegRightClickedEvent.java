package com.example.solitaire;

import com.example.solitaire.backend.Solitaire;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.shape.Circle;

public class PegRightClickedEvent extends Event {
    public static final EventType<PegRightClickedEvent> ANY = new EventType<>(Event.ANY, "ANY");

    private Circle pegClicked;
    private Solitaire solitaire;

    public PegRightClickedEvent(EventType<? extends Event> eventType, Circle pegClicked, Solitaire solitaire) {
        super(eventType);
        this.pegClicked = pegClicked;
        this.solitaire = solitaire;
    }

    public Circle getPegClicked() {
        return pegClicked;
    }

    public Solitaire getSolitaire() {
        return solitaire;
    }
}

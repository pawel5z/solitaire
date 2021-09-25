package com.example.solitaire;

import javafx.scene.shape.Circle;

public class ContextMenuJumpChosenEvent {
    private Circle from;
    private int toR;
    private int toC;

    public ContextMenuJumpChosenEvent(Circle from, int toR, int toC) {
        this.from = from;
        this.toR = toR;
        this.toC = toC;
    }

    public Circle getFrom() {
        return from;
    }

    public int getToR() {
        return toR;
    }

    public int getToC() {
        return toC;
    }
}

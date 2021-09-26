package com.example.solitaire;

public class ContextMenuJumpChosenEvent {
    private final int toR;
    private final int toC;

    public ContextMenuJumpChosenEvent(int toR, int toC) {
        this.toR = toR;
        this.toC = toC;
    }

    public int getToR() {
        return toR;
    }

    public int getToC() {
        return toC;
    }
}

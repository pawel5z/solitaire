package com.example.solitaire.backend;

import static org.junit.Assert.*;

public class SolitaireTest {
    private Solitaire solitaire;

    @org.junit.Before
    public void setUp() throws Exception {
        solitaire = new Solitaire();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void move() {
    }

    @org.junit.Test
    public void getBoardType() {
        assertEquals(SolitaireBoardType.BRITISH, solitaire.getBoardType());
    }

    @org.junit.Test
    public void setBoardType() {
        solitaire.setBoardType(SolitaireBoardType.EUROPEAN);
        assertEquals(SolitaireBoardType.EUROPEAN, solitaire.getBoardType());
    }
}
package com.example.solitaire.backend;

import org.junit.Test;

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
        assertTrue(solitaire.move(3, 5, 3, 3));
        assertTrue(solitaire.isFieldOccupied(3, 3));
        assertFalse(solitaire.isFieldOccupied(3, 4));
        assertFalse(solitaire.isFieldOccupied(3, 5));
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

    @Test
    public void isWin() {
        assertFalse(solitaire.isWin());
    }

    @Test
    public void isLose() {
        assertFalse(solitaire.isLose());
    }

    @Test
    public void isGameOver() {
        assertFalse(solitaire.isGameOver());
    }

    @Test
    public void isFieldOccupied() {
        assertFalse(solitaire.isFieldOccupied(3, 3));
    }
}
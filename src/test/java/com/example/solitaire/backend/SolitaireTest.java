package com.example.solitaire.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SolitaireTest {
    private Solitaire solitaire;

    @Before
    public void setUp() throws Exception {
        solitaire = new Solitaire();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void pegIsTakenOffInEuropeanVariantOnFirstMove() {
        int pegsNumberBefore = solitaire.pegsLeftCount();
        assertNotNull(solitaire.move(3, 5, 3, 3));
        assertTrue(solitaire.isFieldOccupied(3, 3));
        assertFalse(solitaire.isFieldOccupied(3, 4));
        assertFalse(solitaire.isFieldOccupied(3, 5));
        assertEquals(pegsNumberBefore - 1, solitaire.pegsLeftCount());
    }

    @Test
    public void boardIsBritishByDefault() {
        assertEquals(SolitaireBoardType.BRITISH, solitaire.getBoardType());
    }

    @Test
    public void setBoardType() {
        solitaire.setBoardType(SolitaireBoardType.EUROPEAN);
        assertEquals(SolitaireBoardType.EUROPEAN, solitaire.getBoardType());
    }

    @Test
    public void atTheBeginningGameIsNotWin() {
        assertFalse(solitaire.isWin());
    }

    @Test
    public void atTheBeginningGameIsNotLose() {
        assertFalse(solitaire.isLose());
    }

    @Test
    public void atTheBeginningGameIsNotOver() {
        assertFalse(solitaire.isGameOver());
    }

    @Test
    public void atTheBeginningInEuropeanVariantMiddleFieldIsNotOccupied() {
        assertFalse(solitaire.isFieldOccupied(3, 3));
    }

    @Test
    public void atTheBeginningBritishBoardHas32Pegs() {
        assertEquals(32, solitaire.pegsLeftCount());
    }

    @Test
    public void atTheBeginningEuropeanBoardHas36Pegs() {
        solitaire = new Solitaire(SolitaireBoardType.EUROPEAN);
        assertEquals(36, solitaire.pegsLeftCount());
    }
}
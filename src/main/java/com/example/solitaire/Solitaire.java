package com.example.solitaire;

import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static java.lang.Math.abs;

public class Solitaire {
    private static final int dim = 7;
    private static final HashSet<Pair<Integer, Integer>> britExclusive;

    private SolitaireBoardType boardType;
    private HashSet<Pair<Integer, Integer>> pegs = new HashSet<>();

    static {
        britExclusive = new HashSet<>(Arrays.asList(
                new Pair<>(1, 1),
                new Pair<>(1, 5),
                new Pair<>(5, 1),
                new Pair<>(5, 5)));
    }

    public Solitaire() {
        this(SolitaireBoardType.BRITISH);
    }

    public Solitaire(SolitaireBoardType boardType) {
        this.boardType = boardType;
        for (int r = 0; r < dim; r++)
            for (int c = 0; c < dim; c++)
                if (inBoard(r, c))
                    pegs.add(new Pair<>(r, c));
        // remove the middle peg
        pegs.remove(new Pair<>(dim / 2, dim / 2));
    }

    private boolean inBoard(int r, int c) {
        if (boardType != SolitaireBoardType.BRITISH && britExclusive.contains(new Pair<>(r, c)))
            return false;
        return taxicabDist(r, c, dim / 2, dim / 2) <= r / 2 + 1;
    }

    private int taxicabDist(int r1, int c1, int r2, int c2) {
        return abs(r1 - r2) + abs(c1 - c2);
    }

    public SolitaireBoardType getBoardType() {
        return boardType;
    }

    public void setBoardType(SolitaireBoardType boardType) {
        this.boardType = boardType;
    }
}

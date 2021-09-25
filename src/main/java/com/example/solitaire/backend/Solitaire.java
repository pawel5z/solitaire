package com.example.solitaire.backend;

import javafx.util.Pair;

import java.util.*;

import static java.lang.Math.abs;

public class Solitaire {
    private static final int dim = 7;
    private static final ArrayList<Pair<Integer, Integer>> europeanOnly;
    private static final ArrayList<Pair<Integer, Integer>> europeanHolesSolvable;

    private SolitaireBoardType boardType;
    private HashSet<Pair<Integer, Integer>> pegs = new HashSet<>();

    static {
        europeanOnly = new ArrayList<>(Arrays.asList(
                new Pair<>(1, 1),
                new Pair<>(1, 5),
                new Pair<>(5, 1),
                new Pair<>(5, 5)));
        europeanHolesSolvable = new ArrayList<>(Arrays.asList(
                new Pair<>(0, 2),
                new Pair<>(1, 3),
                new Pair<>(2, 3)));
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

        switch (boardType) {
            case BRITISH -> pegs.remove(new Pair<>(dim / 2, dim / 2));
            case EUROPEAN -> pegs.remove(applyRandomElementaryTransform(europeanHolesSolvable.get(new Random().nextInt(europeanHolesSolvable.size()))));
        }
    }

    public Pair<Integer, Integer> isMoveLegal(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        return isMoveLegal(from.getKey(), from.getValue(), to.getKey(), to.getValue());
    }

    public Pair<Integer, Integer> isMoveLegal(int fromR, int fromC, int toR, int toC) {
        if (!inBoard(fromR, fromC) || !inBoard(toR, toC))
            return null;
        int midR = (fromR + toR) / 2;
        int midC = (fromC + toC) / 2;
        // check if move is two fields long in a vertical/horizontal direction
        if ((abs(midR - fromR) != 1 || abs(midR - toR) != 1 || fromC != toC)
                && (abs(midC - fromC) != 1 || abs(midC - toC) != 1 || fromR != toR)) {
            return null;
        }
        if (!pegs.contains(new Pair<>(fromR, fromC))
                || !pegs.contains(new Pair<>(midR, midC))
                || pegs.contains(new Pair<>(toR, toC)))
            return null;
        return new Pair<>(midR, midC);
    }

    public Pair<Integer, Integer> move(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        return move(from.getKey(), from.getValue(), to.getKey(), to.getValue());
    }

    public Pair<Integer, Integer> move(int fromR, int fromC, int toR, int toC) {
        Pair<Integer, Integer> mid = isMoveLegal(fromR, fromC, toR, toC);
        if (mid == null)
            return null;

        // move is legal
        pegs.remove(new Pair<>(fromR, fromC));
        pegs.add(new Pair<>(toR, toC));
        pegs.remove(mid);
        return mid;
    }

    public boolean isWin() {
        return pegs.size() == 1;
    }

    public boolean isLose() {
        return isGameOver() && pegs.size() != 1;
    }

    public boolean isGameOver() {
        return pegs.stream().noneMatch(
                (Pair<Integer, Integer> p) -> {
                    int pr = p.getKey();
                    int pc = p.getValue();

                    // check if one can move peg p
                    for (var d : new Pair[] {
                            new Pair<>(-1, 0),
                            new Pair<>(1, 0),
                            new Pair<>(0, -1),
                            new Pair<>(0, 1)}) {
                        int dr = (int)d.getKey();
                        int dc = (int)d.getValue();
                        if (inBoard(pr + dr, pc + dc) && inBoard(pr + 2 * dr, pc + 2 * dc)
                            && isFieldOccupied(pr + dr, pc + dc) && !isFieldOccupied(pr + 2 * dr, pc + 2 * dc))
                            return true;
                    }

                    return false;
                });
    }

    public boolean isFieldOccupied(Pair<Integer, Integer> f) {
        return pegs.stream().anyMatch((Pair<Integer, Integer> p) -> Objects.equals(p, f));
    }

    public boolean isFieldOccupied(int r, int c) {
        return isFieldOccupied(new Pair<>(r, c));
    }

    private boolean inBoard(Pair<Integer, Integer> p) {
        return inBoard(p.getKey(), p.getValue());
    }

    public boolean inBoard(int r, int c) {
        if (boardType != SolitaireBoardType.EUROPEAN && europeanOnly.contains(new Pair<>(r, c)))
            return false;
        if (taxicabDist(r, c, dim / 2, dim / 2) > dim / 2 + 1)
            return false;
        return 0 <= r && r <= 6 && 0 <= c && c <= 6;
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

    public Set<Pair<Integer, Integer>> getPegPositions() {
        return Collections.unmodifiableSet(pegs);
    }

    public int pegsLeftCount() {
        return pegs.size();
    }

    private Pair<Integer, Integer> applyRandomElementaryTransform(int r, int c) {
        Random random = new Random();
        int rRand = random.nextInt(2);
        int cRand = random.nextInt(2);
        return new Pair<>(rRand * r + (1 - rRand) * (dim - 1 - r), cRand * c + (1 - cRand) * (dim - 1 - c));
    }

    private Pair<Integer, Integer> applyRandomElementaryTransform(Pair<Integer, Integer> p) {
        return applyRandomElementaryTransform(p.getKey(), p.getValue());
    }
}

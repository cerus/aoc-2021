package dev.cerus.aoc2021.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DayFourPuzzle implements Puzzle {

    private List<Integer> randomNumbers;
    private List<Board> boards;

    @Override
    public void setup(final String input) {
        this.randomNumbers = new ArrayList<>();
        this.boards = new CopyOnWriteArrayList<>();

        final List<String> lines = new ArrayList<>(Arrays.stream(input.split("\n")).toList());
        for (final String s : lines.remove(0).trim().split(",")) {
            this.randomNumbers.add(Integer.parseInt(s));
        }

        while (lines.size() > 0 && lines.get(0).trim().length() == 0) {
            lines.remove(0);

            final Board currentBoard = new Board(5, 5);
            for (int row = 0; row < 5; row++) {
                final String line = lines.remove(0);
                final List<Integer> rowNums = Arrays.stream(line.split("\\s+"))
                        .filter(s -> s.trim().length() > 0)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                for (int x = 0; x < rowNums.size(); x++) {
                    currentBoard.setNumber(x, row, rowNums.get(x));
                }
            }
            this.boards.add(currentBoard);
        }
    }

    @Override
    public String solvePartOne() {
        int num = 0;
        while (this.boards.stream().noneMatch(Board::hasWon)) {
            // Mark current number on boards
            num = this.randomNumbers.remove(0);
            final int finalNum = num; // WHY ARE YOU LIKE THIS
            this.boards.forEach(board -> board.mark(finalNum));
        }

        // Find winner
        final Board board = this.boards.stream()
                .filter(Board::hasWon)
                .findFirst()
                .get();
        return this.solve(board, num);
    }

    @Override
    public String solvePartTwo() {
        int winNum = 0;
        Board lastWinner = null;
        while (!this.randomNumbers.isEmpty()) {
            // Mark current number on boards
            final int num = this.randomNumbers.remove(0);
            this.boards.forEach(board -> board.mark(num));

            // Find winners
            for (final Board board : this.boards) {
                if (board.hasWon()) {
                    winNum = num;
                    lastWinner = board;
                    this.boards.remove(board);
                }
            }
        }

        return this.solve(lastWinner, winNum);
    }

    private String solve(final Board board, final int num) {
        // Iterate through board numbers and sum unmarked nums
        final AtomicInteger sum = new AtomicInteger(0);
        board.iterate((integer, marked) -> {
            if (!marked) {
                sum.addAndGet(integer);
            }
        });

        return String.format("Sum: %d Most recent num: %d (mult %d)", sum.get(), num, sum.get() * num);
    }

    private static class Board {

        private final int width;
        private final int height;
        private final int[][] numbers;
        private final boolean[][] marked;

        public Board(final int width, final int height) {
            this.width = width;
            this.height = height;
            this.numbers = new int[width][height];
            this.marked = new boolean[width][height];
        }

        public void mark(final int num) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    if (this.getNumber(x, y) == num) {
                        this.setMarked(x, y, true);
                    }
                }
            }
        }

        public void iterate(final BiConsumer<Integer, Boolean> fun) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    fun.accept(
                            this.getNumber(x, y),
                            this.isMarked(x, y)
                    );
                }
            }
        }

        public boolean hasWon() {
            for (int row = 0; row < this.height; row++) {
                int markedNums = 0;
                for (int x = 0; x < this.width; x++) {
                    if (this.isMarked(x, row)) {
                        markedNums++;
                    }
                }
                if (markedNums == this.width) {
                    return true;
                }
            }

            for (int col = 0; col < this.width; col++) {
                int markedNums = 0;
                for (int y = 0; y < this.height; y++) {
                    if (this.isMarked(col, y)) {
                        markedNums++;
                    }
                }
                if (markedNums == this.height) {
                    return true;
                }
            }

            return false;
        }

        public void setNumber(final int x, final int y, final int num) {
            this.numbers[x][y] = num;
        }

        public int getNumber(final int x, final int y) {
            return this.numbers[x][y];
        }

        public void setMarked(final int x, final int y, final boolean val) {
            this.marked[x][y] = val;
        }

        public boolean isMarked(final int x, final int y) {
            return this.marked[x][y];
        }

    }

}

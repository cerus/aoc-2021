package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;

public class DayOnePuzzle implements Puzzle {

    private int[] depths;

    @Override
    public void setup(final String input) {
        this.depths = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .filter(s -> s.matches("-?\\d+"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    @Override
    public String solvePartOne() {
        int incCount = 0;
        int prev = Integer.MIN_VALUE;

        for (final int depth : this.depths) {
            if (prev != Integer.MIN_VALUE && depth > prev) {
                incCount++;
            }
            prev = depth;
        }

        return incCount + " depth increments";
    }

    @Override
    public String solvePartTwo() {
        int incCount = 0;
        int prev = Integer.MIN_VALUE;

        for (int i = 0; i < this.depths.length; i++) {
            if (i < this.depths.length - 2) {
                final int currWindow = this.depths[i] + this.depths[i + 1] + this.depths[i + 2];
                if (prev != Integer.MIN_VALUE && currWindow > prev) {
                    incCount++;
                }
                prev = currWindow;
            }
        }

        return incCount + " depth sum increments";
    }

}

package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DaySevenPuzzle implements Puzzle {

    private List<Integer> positions;

    @Override
    public void setup(final String input) {
        this.positions = Arrays.stream(input.split(","))
                .map(s -> Integer.parseInt(s.trim()))
                .collect(Collectors.toList());
    }

    @Override
    public String solvePartOne() {
        return this.solve(true);
    }

    @Override
    public String solvePartTwo() {
        return this.solve(false);
    }

    private String solve(final boolean simple) {
        int cost = Integer.MAX_VALUE;
        final int min = this.positions.stream()
                .mapToInt(value -> value)
                .min()
                .getAsInt();
        final int max = this.positions.stream()
                .mapToInt(value -> value)
                .max()
                .getAsInt();

        for (int i = min; i <= max; i++) {
            final int calculatedCost = this.calculateCost(i, simple);
            if (calculatedCost < cost) {
                cost = calculatedCost;
            }
        }
        return String.format("Least fuel spent: %d", cost);
    }

    private int calculateCost(final int alignTo, final boolean simple) {
        int cost = 0;
        for (final int position : this.positions) {
            final int change = Math.max(position, alignTo) - Math.min(position, alignTo);
            if (simple) {
                cost += change;
            } else {
                int t = 1;
                while (t <= change) {
                    cost += t;
                    t++;
                }
            }
        }
        return cost;
    }

}

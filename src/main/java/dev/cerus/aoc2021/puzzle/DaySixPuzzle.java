package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;

public class DaySixPuzzle implements Puzzle {

    private static final int FIRST_SIM_TIME = 80;
    private static final int SECOND_SIM_TIME = 256;

    private long[] ages;
    private long count;

    @Override
    public void setup(final String input) {
        this.ages = new long[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        Arrays.stream(input.split(","))
                .filter(s -> s.trim().matches("\\d+"))
                .map(Integer::parseInt)
                .forEach(integer -> {
                    this.ages[integer]++;
                    this.count++;
                });
    }

    @Override
    public String solvePartOne() {
        return this.solve(FIRST_SIM_TIME);
    }

    @Override
    public String solvePartTwo() {
        return this.solve(SECOND_SIM_TIME);
    }

    private String solve(final int days) {
        final long[] copy = Arrays.copyOf(this.ages, this.ages.length);
        final long countCopy = this.count;

        for (int i = 0; i < days; i++) {
            long newCount = 0;
            for (int age = 0; age < this.ages.length; age++) {
                if (age == 0) {
                    // Reproduce
                    newCount = this.ages[age];
                } else {
                    // Decrement age
                    this.ages[age - 1] = this.ages[age];
                    if (age == 8) {
                        this.ages[8] = 0;
                    }
                }
            }

            this.ages[6] += newCount;
            this.ages[8] += newCount;
            this.count += newCount;
        }

        final String answer = String.format("Amount of fish: %d", this.count);
        this.ages = copy;
        this.count = countCopy;
        return answer;
    }

}

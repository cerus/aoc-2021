package dev.cerus.aoc2021.puzzle;

public interface Puzzle {

    void setup(String input);

    String solvePartOne();

    String solvePartTwo();

    default void cleanup() {
    }

}

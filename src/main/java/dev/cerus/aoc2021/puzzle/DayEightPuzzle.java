package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayEightPuzzle implements Puzzle {

    private List<Wiring> wirings;

    @Override
    public void setup(final String input) {
        this.wirings = Arrays.stream(input.replace("|\n", "| ").split("\n"))
                .map(s -> s.split(" \\| "))
                .map(strings -> new Wiring(
                        Arrays.stream(strings[0].trim().split("\\s+"))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        Arrays.stream(strings[1].trim().split("\\s+"))
                                .map(String::trim)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public String solvePartOne() {
        int sum = 0;
        for (final Wiring wiring : this.wirings) {
            sum += wiring.solve(true).size();
        }
        return String.format("Amount of 1s, 4s, 7s and 8s: %d", sum);
    }

    @Override
    public String solvePartTwo() {
        int sum = 0;
        for (final Wiring wiring : this.wirings) {
            // Join digits together and parse as a number
            final int num = Integer.parseInt(wiring.solve(false).stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining()));
            sum += num;
        }
        return String.format("Sum of output digits: %d", sum);
    }

    private static class Wiring {

        private final List<String> numberSequences;
        private final List<String> outputDigits;

        private Wiring(final List<String> numberSequences, final List<String> outputDigits) {
            this.numberSequences = numberSequences;
            this.outputDigits = outputDigits;
        }

        public List<Integer> solve(final boolean simple) {
            final Map<Long, Integer> solvedDigits = new HashMap<>();
            final Map<Integer, Long> solvedDigitsReverse = new HashMap<>();

            // Solve the simple ones first
            for (final String sequence : this.numberSequences) {
                switch (sequence.length()) {
                    case 2 -> {
                        solvedDigits.put(this.sequenceToKey(sequence), 1);
                        solvedDigitsReverse.put(1, this.sequenceToKey(sequence));
                    }
                    case 4 -> {
                        solvedDigits.putIfAbsent(this.sequenceToKey(sequence), 4);
                        solvedDigitsReverse.putIfAbsent(4, this.sequenceToKey(sequence));
                    }
                    case 3 -> {
                        solvedDigits.put(this.sequenceToKey(sequence), 7);
                        solvedDigitsReverse.put(7, this.sequenceToKey(sequence));
                    }
                    case 7 -> {
                        solvedDigits.put(this.sequenceToKey(sequence), 8);
                        solvedDigitsReverse.put(8, this.sequenceToKey(sequence));
                    }
                }
            }

            // Part 1 - Only resolve "simple" digits
            if (simple) {
                return this.outputDigits.stream()
                        .filter(s -> solvedDigits.containsKey(this.sequenceToKey(s)))
                        .map(s -> solvedDigits.get(this.sequenceToKey(s)))
                        .collect(Collectors.toList());
            }

            // While unsolved_sequences > 0
            while (this.numberSequences.stream()
                    .anyMatch(s -> !solvedDigits.containsKey(this.sequenceToKey(s)))) {
                for (final String sequence : this.numberSequences) {
                    // Make key, count bits
                    final long key = this.sequenceToKey(sequence);
                    final int bits = this.countBits(key);
                    if (solvedDigits.containsKey(key)) {
                        continue;
                    }

                    // Most of this works by &-ing two numbers and counting the resulting 1s.
                    // This is a simple but powerful way of solving this puzzle, but it's probably
                    // not the best.

                    if ((key & solvedDigitsReverse.get(7)) == solvedDigitsReverse.get(7)
                            && (key & solvedDigitsReverse.get(4)) == solvedDigitsReverse.get(4)) {
                        // 9
                        solvedDigits.put(key, 9);
                        solvedDigitsReverse.put(9, key);
                    } else if (bits == 5
                            && this.countBits(key & solvedDigitsReverse.get(1)) == 1
                            && this.countBits(key & solvedDigitsReverse.get(4)) == 2
                            && this.countBits(key & solvedDigitsReverse.get(7)) == 2) {
                        // 2
                        solvedDigits.put(key, 2);
                        solvedDigitsReverse.put(2, key);
                    } else if (solvedDigitsReverse.containsKey(2)
                            && solvedDigitsReverse.containsKey(9)
                            && this.countBits(key & solvedDigitsReverse.get(2)) == 3) {
                        // 5
                        solvedDigits.put(key, 5);
                        solvedDigitsReverse.put(5, key);
                    } else if (solvedDigitsReverse.containsKey(2)
                            && solvedDigitsReverse.containsKey(5)
                            && solvedDigitsReverse.containsKey(9)) {
                        if (this.countBits(key & solvedDigitsReverse.get(5)) == 4) {
                            // 0 or 3
                            if (solvedDigitsReverse.containsKey(6)) {
                                if (this.countBits(key & solvedDigitsReverse.get(6)) == 5) {
                                    // 0
                                    solvedDigits.put(key, 0);
                                    solvedDigitsReverse.put(0, key);
                                } else if (this.countBits(key & solvedDigitsReverse.get(6)) == 4) {
                                    // 3
                                    solvedDigits.put(key, 3);
                                    solvedDigitsReverse.put(3, key);
                                }
                            }
                        } else if (this.countBits(key & solvedDigitsReverse.get(5)) == 5) {
                            // 6
                            solvedDigits.put(key, 6);
                            solvedDigitsReverse.put(6, key);
                        }
                    }
                }
            }

            return this.outputDigits.stream()
                    .filter(s -> solvedDigits.containsKey(this.sequenceToKey(s)))
                    .map(s -> solvedDigits.get(this.sequenceToKey(s)))
                    .collect(Collectors.toList());
        }

        /**
         * Produces a key for each sequence. Will produce the
         * same key if the chars are the same but at a different position.
         * <p>
         * Examples:
         * 'abcdef' = 63
         * 'fedcba' = 63
         * 'abcd' = 15
         */
        private long sequenceToKey(final String sequence) {
            final int offset = 'a';
            long l = 0;
            final char[] chars = sequence.toCharArray();
            for (final char c : chars) {
                l |= (1L << (c - offset));
            }
            return l;
        }

        /**
         * Not the best way to count 1s but it works
         */
        private int countBits(final long l) {
            return Long.toBinaryString(l).replace("0", "").length();
        }

    }

}

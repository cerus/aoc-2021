package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class DayThreePuzzle implements Puzzle {

    private List<Binary> numbers;

    @Override
    public void setup(final String input) {
        this.numbers = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .filter(s -> s.matches("[01]+"))
                .map(Binary::new)
                .collect(Collectors.toList());
    }

    @Override
    public String solvePartOne() {
        final int len = this.numbers.get(0).getSize();

        final StringBuilder gammaBuf = new StringBuilder();
        final StringBuilder epsilonBuf = new StringBuilder();
        for (int i = 0; i < len; i++) {
            // Count 1s and 0s
            int one = 0;
            int zero = 0;
            for (final Binary number : this.numbers) {
                if (number.getBitAt(i)) {
                    one++;
                } else {
                    zero++;
                }
            }

            if (one >= zero) {
                // If 1 is more common...
                gammaBuf.append("1");
                epsilonBuf.append("0");
            } else {
                // If 0 is more common...
                gammaBuf.append("0");
                epsilonBuf.append("1");
            }
        }

        final Binary gamma = new Binary(gammaBuf.toString());
        final Binary epsilon = new Binary(epsilonBuf.toString());
        return String.format("Gamma: %d Epsilon: %d (mult %d)", gamma.toDecimal(),
                epsilon.toDecimal(), gamma.toDecimal() * epsilon.toDecimal());
    }

    @Override
    public String solvePartTwo() {
        // Count 1s and 0s of the first bits
        int firstOneCount = 0;
        int firstZeroCount = 0;
        for (final Binary number : this.numbers) {
            if (number.getBitAt(0)) {
                firstOneCount++;
            } else {
                firstZeroCount++;
            }
        }

        // Build two lists for the oxygen and co2 diagnostics
        final int finalFirstOneCount = firstOneCount;
        final int finalFirstZeroCount = firstZeroCount;
        final List<Binary> oxygenList = this.numbers.stream()
                .filter(binary -> binary.getBitAt(0) == finalFirstOneCount >= finalFirstZeroCount)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));
        final List<Binary> co2List = this.numbers.stream()
                .filter(binary -> binary.getBitAt(0) != finalFirstOneCount >= finalFirstZeroCount)
                .collect(Collectors.toCollection(CopyOnWriteArrayList::new));

        // Find the oxygen diagnostics
        int pos = 1;
        while (oxygenList.size() > 1) {
            int oneCount = 0;
            int zeroCount = 0;
            for (final Binary binary : oxygenList) {
                if (binary.getBitAt(pos)) {
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }

            for (final Binary binary : oxygenList) {
                if (oxygenList.size() == 1) {
                    break;
                }
                // We need the most common bit, so remove all that share the least common bit
                if (binary.getBitAt(pos) != oneCount >= zeroCount) {
                    oxygenList.remove(binary);
                }
            }

            pos++;
        }

        // Find the co2 diagnostics
        pos = 1;
        while (co2List.size() > 1) {
            int oneCount = 0;
            int zeroCount = 0;
            for (final Binary binary : co2List) {
                if (binary.getBitAt(pos)) {
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }

            for (final Binary binary : co2List) {
                if (co2List.size() == 1) {
                    break;
                }
                // We need the least common bit, so remove all that share the most common bit
                if (binary.getBitAt(pos) == oneCount >= zeroCount) {
                    co2List.remove(binary);
                }
            }

            pos++;
        }

        final Binary lastOxygen = oxygenList.get(0);
        final Binary lastCo2 = co2List.get(0);
        return String.format("Oxygen: %d CO2: %d (mult %d)", lastOxygen.toDecimal(),
                lastCo2.toDecimal(), lastOxygen.toDecimal() * lastCo2.toDecimal());
    }

    private static class Binary {

        private final boolean[] bits;

        public Binary(final String s) {
            final int len = s.trim().length();
            this.bits = new boolean[len];
            final char[] chars = s.trim().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                this.bits[i] = chars[i] == '1';
            }
        }

        public long toDecimal() {
            long i = 0;
            int prev = -1;
            for (int j = 0; j < this.bits.length; j++) {
                final int pos = this.bits.length - 1 - j;
                final boolean bit = this.bits[pos];
                final int val = (prev == -1 ? 1 : prev * 2);

                if (bit) {
                    i += val;
                }

                prev = val;
            }
            return i;
        }

        public boolean getBitAt(final int pos) {
            return this.bits[pos];
        }

        public int getSize() {
            return this.bits.length;
        }

    }

}

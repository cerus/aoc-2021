package dev.cerus.aoc2021;

import dev.cerus.aoc2021.puzzle.DayFivePuzzle;
import dev.cerus.aoc2021.puzzle.DayFourPuzzle;
import dev.cerus.aoc2021.puzzle.DayOnePuzzle;
import dev.cerus.aoc2021.puzzle.DayThreePuzzle;
import dev.cerus.aoc2021.puzzle.DayTwoPuzzle;
import dev.cerus.aoc2021.puzzle.Puzzle;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Launcher {

    private static final Puzzle[] PUZZLES = new Puzzle[] {
            new DayOnePuzzle(),
            new DayTwoPuzzle(),
            new DayThreePuzzle(),
            new DayFourPuzzle(),
            new DayFivePuzzle()
    };

    public static void main(final String[] args) {
        final Map<String, String> options = parseOptions(args);
        if (options.containsKey("day")) {
            // Run a single day
            final int day = Integer.parseInt(options.get("day"));
            final Puzzle puzzle = PUZZLES[day - 1];
            solve(puzzle, day);
        } else {
            // Run all solved days
            for (int i = 1; i <= 24; i++) {
                if (PUZZLES.length >= i) {
                    System.out.println();
                    solve(PUZZLES[i - 1], i);
                }
            }
            System.out.println();
        }
    }

    private static void solve(final Puzzle puzzle, final int d) {
        puzzle.setup(getInput(d));
        System.out.println("[Day " + d + "] [Part 1] " + puzzle.solvePartOne());
        System.out.println("[Day " + d + "] [Part 2] " + puzzle.solvePartTwo());
        puzzle.cleanup();
    }

    private static Map<String, String> parseOptions(final String[] args) {
        final Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--") && i < args.length - 1) {
                map.put(args[i].substring(2), args[i + 1]);
                i++;
            }
        }
        return map;
    }

    private static String getInput(final int day) {
        final InputStream in = Launcher.class.getClassLoader().getResourceAsStream("input" + day + ".txt");

        final StringBuilder buf = new StringBuilder();
        try {
            final byte[] byteBuf = new byte[512];
            int r;
            while ((r = in.read(byteBuf)) != -1) {
                buf.append(new String(byteBuf, 0, r));
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

}

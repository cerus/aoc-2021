package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class DayFivePuzzle implements Puzzle {

    private List<PointPair> points;

    @Override
    public void setup(final String input) {
        this.points = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .filter(s -> s.matches("\\d+,\\d+ -> \\d+,\\d+"))
                .map(s -> s.split(" -> "))
                .map(strings -> new PointPair(
                        new Point(strings[0]),
                        new Point(strings[1])
                ))
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
        final Grid grid = new Grid();
        for (final PointPair pair : this.points) {
            grid.submitLine(pair, simple);
        }

        final AtomicInteger counter = new AtomicInteger(0);
        grid.iterate((point, count) -> {
            if (count >= 2) {
                counter.addAndGet(1);
            }
        });
        return String.format("Overlapping points: %d", counter.get());
    }

    private static class Grid {

        private final Map<Point, Integer> countMap = new HashMap<>();

        public void iterate(final BiConsumer<Point, Integer> fun) {
            this.countMap.forEach(fun);
        }

        public void submitLine(final PointPair pair, final boolean simple) {
            if (simple) {
                // Only accept horizontal and vertical lines
                if (pair.from.x != pair.to.x && pair.from.y != pair.to.y) {
                    return;
                }
            }

            if (pair.from.x == pair.to.x) {
                // Vertical
                for (int o = Math.min(pair.from.y, pair.to.y); o <= Math.max(pair.from.y, pair.to.y); o++) {
                    this.count(new Point(pair.from.x, o));
                }
            } else if (pair.from.y == pair.to.y) {
                // Horizontal
                for (int o = Math.min(pair.from.x, pair.to.x); o <= Math.max(pair.from.x, pair.to.x); o++) {
                    this.count(new Point(o, pair.from.y));
                }
            } else {
                // Diagonal
                final Point toAdd;
                if (pair.from.x < pair.to.x && pair.from.y < pair.to.y) {
                    // Up right
                    toAdd = new Point(1, 1);
                } else if (pair.from.x > pair.to.x && pair.from.y < pair.to.y) {
                    // Up left
                    toAdd = new Point(-1, 1);
                } else if (pair.from.x < pair.to.x) {
                    // Down right
                    toAdd = new Point(1, -1);
                } else {
                    // Down left
                    toAdd = new Point(-1, -1);
                }

                Point p = pair.from;
                while (!(p = p.add(toAdd)).equals(pair.to)) {
                    this.count(p);
                }
                this.count(pair.from);
                this.count(pair.to);
            }
        }

        private void count(final Point point) {
            this.countMap.put(point, this.countMap.getOrDefault(point, 0) + 1);
        }

    }

    private static class Point {

        public int x;
        public int y;

        public Point(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public Point(final String string) {
            this(Integer.parseInt(string.split(",")[0]), Integer.parseInt(string.split(",")[1]));
        }

        public Point add(final Point other) {
            return new Point(
                    this.x + other.x,
                    this.y + other.y
            );
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Point point = (Point) o;
            return this.x == point.x && this.y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }

    }

    private static class PointPair {

        public Point from;
        public Point to;

        public PointPair(final Point from, final Point to) {
            this.from = from;
            this.to = to;
        }

    }

}

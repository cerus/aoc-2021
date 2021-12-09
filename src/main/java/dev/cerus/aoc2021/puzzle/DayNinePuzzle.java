package dev.cerus.aoc2021.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DayNinePuzzle implements Puzzle {

    private int[][] grid;

    @Override
    public void setup(final String input) {
        final List<String> lines = Arrays.stream(input.split("\n"))
                .filter(s -> s.trim().length() > 0)
                .toList();
        this.grid = new int[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            final String line = lines.get(i);
            this.grid[i] = Arrays.stream(line.split(""))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }
    }

    @Override
    public String solvePartOne() {
        int riskSum = 0;
        for (int y = 0; y < this.grid.length; y++) {
            final int[] row = this.grid[y];
            for (int x = 0; x < row.length; x++) {
                final int height = row[x];
                if (x > 0 && height >= row[x - 1]) {
                    continue;
                }
                if (x < row.length - 1 && height >= row[x + 1]) {
                    continue;
                }
                if (y > 0 && height >= this.grid[y - 1][x]) {
                    continue;
                }
                if (y < this.grid.length - 1 && height >= this.grid[y + 1][x]) {
                    continue;
                }

                riskSum += height + 1;
            }
        }
        return String.format("Risk sum: %d", riskSum);
    }

    @Override
    public String solvePartTwo() {
        final List<Integer> basins = new ArrayList<>();
        for (int y = 0; y < this.grid.length; y++) {
            final int[] row = this.grid[y];
            for (int x = 0; x < row.length; x++) {
                final int height = row[x];
                if (x > 0 && height >= row[x - 1]) {
                    continue;
                }
                if (x < row.length - 1 && height >= row[x + 1]) {
                    continue;
                }
                if (y > 0 && height >= this.grid[y - 1][x]) {
                    continue;
                }
                if (y < this.grid.length - 1 && height >= this.grid[y + 1][x]) {
                    continue;
                }

                final int size = this.traverseLowPoint(x, y);
                basins.add(size);
            }
        }
        return String.format("Largest basins: %d", basins.stream()
                .sorted(Comparator.comparingInt(value -> (int) value).reversed())
                .mapToInt(value -> value)
                .limit(3)
                .reduce(1, (left, right) -> left * right));
    }

    private int traverseLowPoint(final int x, final int y) {
        int size = 0;
        final int height = this.grid[y][x];

        final List<String> list = new ArrayList<>();
        if (x > 0 && height < this.grid[y][x - 1]) {
            size += this.traverseX(x, y, -1, height, list);
        }
        if (x < this.grid[y].length - 1 && height < this.grid[y][x + 1]) {
            size += this.traverseX(x, y, 1, height, list);
        }

        if (y > 0 && this.grid[y - 1][x] >= height && this.grid[y - 1][x] != 9) {
            size += this.traverseY(x, y, -1, height, list);
        }
        if (y < this.grid.length - 1 && this.grid[y + 1][x] >= height && this.grid[y + 1][x] != 9) {
            size += this.traverseY(x, y, 1, height, list);
        }
        size += 1;

        return size;
    }

    private int traverseX(final int x, final int y, final int xAdd, final int height, final List<String> list) {
        int size = 0;
        int newX = x;
        while ((newX = newX + xAdd) >= 0 && newX < this.grid[y].length) {
            if (this.grid[y][newX] <= height || this.grid[y][newX] == 9) {
                break;
            }
            if (list.contains(y + ";" + newX)) {
                break;
            }
            list.add(y + ";" + newX);
            if (y > 0 && this.grid[y - 1][newX] > this.grid[y][newX] && this.grid[y - 1][newX] != 9) {
                size += this.traverseY(newX, y, -1, this.grid[y][newX], list);
            }
            if (y < this.grid.length - 1 && this.grid[y + 1][newX] > this.grid[y][newX] && this.grid[y + 1][newX] != 9) {
                size += this.traverseY(newX, y, 1, this.grid[y][newX], list);
            }
            size += 1;
        }
        return size;
    }

    private int traverseY(final int x, final int y, final int yAdd, final int height, final List<String> list) {
        int size = 0;
        int newY = y;
        while ((newY = newY + yAdd) >= 0 && newY < this.grid.length) {
            if (this.grid[newY][x] <= height || this.grid[newY][x] == 9) {
                break;
            }
            if (list.contains(newY + ";" + x)) {
                break;
            }
            list.add(newY + ";" + x);
            if (x > 0 && this.grid[newY][x - 1] > this.grid[newY][x] && this.grid[newY][x - 1] != 9) {
                size += this.traverseX(x, newY, -1, this.grid[newY][x], list);
            }
            if (x < this.grid[newY].length - 1 && this.grid[newY][x + 1] > this.grid[newY][x] && this.grid[newY][x + 1] != 9) {
                size += this.traverseX(x, newY, 1, this.grid[newY][x], list);
            }
            size += 1;
        }
        return size;
    }

}

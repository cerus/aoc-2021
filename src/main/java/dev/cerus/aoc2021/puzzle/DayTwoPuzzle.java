package dev.cerus.aoc2021.puzzle;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DayTwoPuzzle implements Puzzle {

    private List<Command> commands;

    @Override
    public void setup(final String input) {
        this.commands = Arrays.stream(input.split("\n"))
                .filter(s -> s.trim().length() > 0)
                .map(Command::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public String solvePartOne() {
        final Pos.SimplePos pos = new Pos.SimplePos();
        for (final Command command : this.commands) {
            command.exec(pos);
        }
        return String.format("X: %d Y: %d (mult %d)", pos.x, pos.y, pos.x * pos.y);
    }

    @Override
    public String solvePartTwo() {
        final Pos.AdvancedPos pos = new Pos.AdvancedPos();
        for (final Command command : this.commands) {
            command.exec(pos);
        }
        return String.format("X: %d Y: %d AIM: %d (mult %d)", pos.x, pos.y, pos.aim, pos.x * pos.y);
    }

    private static abstract class Pos {

        private static class SimplePos extends Pos {

            public int x;
            public int y;

        }

        private static class AdvancedPos extends Pos {

            public int x;
            public int y;
            public int aim;

        }

    }

    private static abstract class Command {

        public static Command parse(final String str) {
            final String[] split = str.trim().split("\\s+");
            if (split.length == 2) {
                switch (split[0].toLowerCase()) {
                    case "forward":
                        return new ForwardCommand(Integer.parseInt(split[1]));
                    case "up":
                        return new UpCommand(Integer.parseInt(split[1]));
                    case "down":
                        return new DownCommand(Integer.parseInt(split[1]));
                }
            }
            return null;
        }

        public abstract void exec(Pos pos);

        private static class ForwardCommand extends Command {

            private final int val;

            private ForwardCommand(final int val) {
                this.val = val;
            }

            @Override
            public void exec(final Pos pos) {
                if (pos instanceof Pos.SimplePos simplePos) {
                    simplePos.x += this.val;
                } else if (pos instanceof Pos.AdvancedPos advancedPos) {
                    advancedPos.x += this.val;
                    advancedPos.y += this.val * advancedPos.aim;
                }
            }

        }

        private static class UpCommand extends Command {

            private final int val;

            private UpCommand(final int val) {
                this.val = val;
            }

            @Override
            public void exec(final Pos pos) {
                if (pos instanceof Pos.SimplePos simplePos) {
                    simplePos.y -= this.val;
                } else if (pos instanceof Pos.AdvancedPos advancedPos) {
                    advancedPos.aim -= this.val;
                }
            }

        }

        private static class DownCommand extends Command {

            private final int val;

            private DownCommand(final int val) {
                this.val = val;
            }

            @Override
            public void exec(final Pos pos) {
                if (pos instanceof Pos.SimplePos simplePos) {
                    simplePos.y += this.val;
                } else if (pos instanceof Pos.AdvancedPos advancedPos) {
                    advancedPos.aim += this.val;
                }
            }

        }

    }

}

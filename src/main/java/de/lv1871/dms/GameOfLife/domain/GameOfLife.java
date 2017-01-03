package de.lv1871.dms.GameOfLife.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.lv1871.dms.GameOfLife.domain.Field.*;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.and;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.or;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.which;

public class GameOfLife {

    private List<Field> gameboard;

    public GameOfLife(int xSize, int ySize) {
        gameboard = initRandomGame(xSize, ySize);
    }


    public void iterateGameboard() {
        gameboard = gameboard
                .stream()
                .map(toDeadField(which(isAlive(), and(),
                        which(hasLessThanTwo(livingNeighboursIn(gameboard)), or(), hasMoreThanThree(livingNeighboursIn(gameboard))))))
                .map(toAliveField(which(isDead(), and(), hasExactThree(livingNeighboursIn(gameboard)))))
                .collect(Collectors.toList());
    }

    private static Predicate<Field> hasMoreThanThree(Function<Field, List<Field>> neighbours) {
        return (field) -> neighbours.apply(field).size() > 3;
    }

    private static Predicate<Field> hasExactThree(Function<Field, List<Field>> neighbours) {
        return (field) -> neighbours.apply(field).size() == 3;
    }

    private static Predicate<Field> hasLessThanTwo(Function<Field, List<Field>> neighbours) {
        return (field) -> neighbours.apply(field).size() < 2;
    }

    private static List<Field> initRandomGame(int xSize, int ySize) {
        List<Field> game = new LinkedList<Field>();
        for (int i = 0; i < ySize; i++) {
            for (int e = 0; e < xSize; e++) {
                game.add(new Field(e, i, (new Random().nextInt(20) % 4 == 0)));
            }
        }

        return game;
    }

    private static Function<Field, List<Field>> livingNeighboursIn(List<Field> game) {
        // @formatter:off
        return (field) -> game
                .stream()
                .filter(isLivingNeighbour(field))
                .collect(Collectors.toList());
        // @formatter:on
    }

    public Stream<Field> fieldStream() {
        return gameboard.stream();
    }
}

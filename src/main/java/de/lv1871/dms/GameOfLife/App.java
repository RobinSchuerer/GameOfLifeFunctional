package de.lv1871.dms.GameOfLife;

import static de.lv1871.dms.GameOfLife.domain.Field.isAlive;
import static de.lv1871.dms.GameOfLife.domain.Field.isDead;
import static de.lv1871.dms.GameOfLife.domain.Field.mapToSign;
import static de.lv1871.dms.GameOfLife.domain.Field.toAliveField;
import static de.lv1871.dms.GameOfLife.domain.Field.toDeadField;
import static de.lv1871.dms.GameOfLife.domain.Gameboard.initRandomGame;
import static de.lv1871.dms.GameOfLife.domain.Gameboard.livingNeighboursIn;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.and;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.or;
import static de.lv1871.dms.GameOfLife.domain.LambdaPredicateExtension.which;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import de.lv1871.dms.GameOfLife.domain.Field;

public class App {
	public static void main(String[] args) throws InterruptedException {

		int xSize = 10;
		int ySize = 5;

		List<Field> gameBoard = initRandomGame(xSize, ySize);

		while (true) {

			// @formatter:off
		    
		    gameBoard.stream()
		    	.collect(groupBy(Field::getY, mapToSign()))
			.entrySet()
			.stream()
			.sorted(byYCoord())
			.map(toSingleLine())
			.map(createTextLine())
			.peek(System.out::println)
			.collect(Collectors.toList());
		    
		    gameBoard = gameBoard.stream()
	    		.map(toDeadField(which(isAlive(), and(), 
	    			which(hasLessThanTwo(livingNeighboursIn(gameBoard)), or(), hasMoreThanThree(livingNeighboursIn(gameBoard))))))
	    		.map(toAliveField(which(isDead(), and(), hasExactThree(livingNeighboursIn(gameBoard)))))
	    		.collect(Collectors.toList());
	        	
		    // @formatter:on

			System.out.println();
			Thread.sleep(1000);
		}
	}

	private static Comparator<Entry<Integer, List<String>>> byYCoord() {
		return (entry1, entry2) -> Integer.compare(entry1.getKey(), entry2.getKey());
	}

	private static Function<Entry<Integer, List<String>>, List<String>> toSingleLine() {
		return (entry) -> entry.getValue();
	}

	private static Collector<Field, ?, Map<Integer, List<String>>> groupBy(Function<Field, Integer> supplier,
			Function<Field, String> mapper) {
		return Collectors.groupingBy(supplier, Collectors.mapping(mapper, Collectors.toList()));
	}

	private static Function<List<String>, String> createTextLine() {
		return (list) -> list.stream().collect(Collectors.joining(""));
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

}

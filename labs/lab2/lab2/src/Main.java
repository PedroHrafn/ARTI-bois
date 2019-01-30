import java.util.List;
import java.util.ArrayList;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.NotEqualConstraint;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.Variable;

public class Main {

	private static CSP setupCSP() {
		CSP csp = null;
//		In five houses, each with a different color, live five persons of different nationality,
//		each of whom prefers a different brand of cigarettes, a different drink, and a different pet.
//		The five houses are arranged in a row (no house has more than 2 neighbors).   
//		The Englishman lives in the red house.
//		The Spaniard owns the dog.
//		Coffee is drunk in the green house.
//		# The Ukrainian drinks tea.
//		The green house is immediately to the right of the ivory house.
//		# The Old Gold smoker owns snails.
//		# Kools are smoked in the yellow house.
//		# Milk is drunk in the middle house.
//		The Norwegian lives in the first house.
//		# The man who smokes Chesterfields lives in the house next to the man with the fox.
//		# Kools are smoked in the house next to the house where the horse is kept.
//		# The Lucky Strike smoker drinks orange juice.
//		# The Japanese smokes Parliaments.
//		The Norwegian lives next to the blue house.
//
//		Now, who drinks water? Who owns the zebra?
				
		String[] colors = {"Red", "Green", "Ivory", "Yellow", "Blue"};
		String[] nations = {"Englishman", "Spaniard", "Norwegian", "Ukrainian", "Japanese"};
		String[] cigarettes = {"Old Gold", "Kools", "Chesterfields", "Lucky Strike", "Parliaments"};
		String[] drink = {"Water", "Orange juice", "Tea", "Coffee", "Milk"};
		String[] pet = {"Zebra", "Dog", "Fox", "Snails", "Horse"};
		
		// TODO create variables, e.g.,
		// Variable var1 = new Variable("name of the variable 1");
		// Variable var2 = new Variable("name of the variable 2");

		Variable red = new Variable(colors[0]);
		Variable green = new Variable(colors[1]);
		Variable ivory = new Variable(colors[2]);
		Variable yellow = new Variable(colors[3]);
		Variable blue = new Variable(colors[4]);

		Variable englishman = new Variable(nations[0]);
		Variable spaniard = new Variable(nations[1]);
		Variable norweigan = new Variable(nations[2]);
		Variable ukrainian = new Variable(nations[3]);
		Variable japanese = new Variable(nations[4]);

		Variable oldGold = new Variable(cigarettes[0]);
		Variable kools = new Variable(cigarettes[1]);
		Variable chesterfields = new Variable(cigarettes[2]);
		Variable luckyStrike = new Variable(cigarettes[3]);
		Variable parliaments = new Variable(cigarettes[4]);

		Variable water = new Variable(drink[0]);
		Variable orangeJuice = new Variable(drink[1]);
		Variable tea = new Variable(drink[2]);
		Variable coffee = new Variable(drink[3]);
		Variable milk = new Variable(drink[4]);

		Variable zebra = new Variable(pet[0]);
		Variable dog = new Variable(pet[1]);
		Variable fox = new Variable(pet[2]);
		Variable snails = new Variable(pet[3]);
		Variable horse = new Variable(pet[4]);
		
		List<Variable> variables = new ArrayList<Variable>();
		// TODO add all your variables to this list, e.g.,
		// variables.add(var1);
		// variables.add(var2);

		variables.add(red);
		variables.add(green);
		variables.add(ivory);
		variables.add(yellow);
		variables.add(blue);

		variables.add(englishman);
		variables.add(spaniard);
		variables.add(norweigan);
		variables.add(ukrainian);
		variables.add(japanese);

		variables.add(oldGold);
		variables.add(kools);
		variables.add(chesterfields);
		variables.add(luckyStrike);
		variables.add(parliaments);

		variables.add(water);
		variables.add(orangeJuice);
		variables.add(tea);
		variables.add(coffee);
		variables.add(milk);

		variables.add(zebra);
		variables.add(dog);
		variables.add(fox);
		variables.add(snails);
		variables.add(horse);
		
		csp = new CSP(variables);

		// TODO set domains of variables, e.g.,
		// Domain d1 = new Domain(new String[]{"foo", "bar"});
		// csp.setDomain(var1, d1);
		// Domain d2 = new Domain(new Integer[]{1, 2});
		// csp.setDomain(var2, d2);

		Domain d1 = new Domain(new Integer[]{1,2,3,4,5});
		Domain d2 = new Domain(new Integer[]{1});
		Domain d3 = new Domain(new Integer[]{3});

		csp.setDomain(red, d1);
		csp.setDomain(green, d1);
		csp.setDomain(ivory, d1);
		csp.setDomain(yellow, d1);
		csp.setDomain(blue, d1);

		csp.setDomain(englishman, d1);
		csp.setDomain(spaniard, d1);
		csp.setDomain(norweigan, d2);
		csp.setDomain(ukrainian, d1);
		csp.setDomain(japanese, d1);

		csp.setDomain(oldGold, d1);
		csp.setDomain(kools, d1);
		csp.setDomain(chesterfields, d1);
		csp.setDomain(luckyStrike, d1);
		csp.setDomain(parliaments, d1);

		csp.setDomain(water, d1);
		csp.setDomain(orangeJuice, d1);
		csp.setDomain(tea, d1);
		csp.setDomain(coffee, d1);
		csp.setDomain(milk, d3);

		csp.setDomain(zebra, d1);
		csp.setDomain(dog, d1);
		csp.setDomain(fox, d1);
		csp.setDomain(snails, d1);
		csp.setDomain(horse, d1);
		
		// TODO add constraints, e.g.,
		// csp.addConstraint(new NotEqualConstraint(var1, var2)); // meaning var1 != var2
		// csp.addConstraint(new EqualConstraint(var1, var2)); // meaning var1 == var2
		// csp.addConstraint(new SuccessorConstraint(var1, var2)); // meaning var1 == var2 + 1
		// csp.addConstraint(new DifferByOneConstraint(var1, var2)); // meaning var1 == var2 + 1 or var1 == var2 - 1 

		csp.addConstraint(new NotEqualConstraint(red, green));
		csp.addConstraint(new NotEqualConstraint(red, ivory));
		csp.addConstraint(new NotEqualConstraint(red, yellow));
		csp.addConstraint(new NotEqualConstraint(red, blue));
		csp.addConstraint(new NotEqualConstraint(green, ivory));
		csp.addConstraint(new NotEqualConstraint(green, yellow));
		csp.addConstraint(new NotEqualConstraint(green, blue));
		csp.addConstraint(new NotEqualConstraint(ivory, yellow));
		csp.addConstraint(new NotEqualConstraint(ivory, blue));
		csp.addConstraint(new NotEqualConstraint(yellow, blue));

		csp.addConstraint(new NotEqualConstraint(englishman, spaniard));
		csp.addConstraint(new NotEqualConstraint(englishman, norweigan));
		csp.addConstraint(new NotEqualConstraint(englishman, ukrainian));
		csp.addConstraint(new NotEqualConstraint(englishman, japanese));
		csp.addConstraint(new NotEqualConstraint(spaniard, norweigan));
		csp.addConstraint(new NotEqualConstraint(spaniard, ukrainian));
		csp.addConstraint(new NotEqualConstraint(spaniard, japanese));
		csp.addConstraint(new NotEqualConstraint(norweigan, ukrainian));
		csp.addConstraint(new NotEqualConstraint(norweigan, japanese));
		csp.addConstraint(new NotEqualConstraint(ukrainian, japanese));

		csp.addConstraint(new NotEqualConstraint(oldGold, kools));
		csp.addConstraint(new NotEqualConstraint(oldGold, chesterfields));
		csp.addConstraint(new NotEqualConstraint(oldGold, luckyStrike));
		csp.addConstraint(new NotEqualConstraint(oldGold, parliaments));
		csp.addConstraint(new NotEqualConstraint(kools, chesterfields));
		csp.addConstraint(new NotEqualConstraint(kools, luckyStrike));
		csp.addConstraint(new NotEqualConstraint(kools, parliaments));
		csp.addConstraint(new NotEqualConstraint(chesterfields, luckyStrike));
		csp.addConstraint(new NotEqualConstraint(chesterfields, parliaments));
		csp.addConstraint(new NotEqualConstraint(luckyStrike, parliaments));

		csp.addConstraint(new NotEqualConstraint(water, orangeJuice));
		csp.addConstraint(new NotEqualConstraint(water, tea));
		csp.addConstraint(new NotEqualConstraint(water, coffee));
		csp.addConstraint(new NotEqualConstraint(water, milk));
		csp.addConstraint(new NotEqualConstraint(orangeJuice, tea));
		csp.addConstraint(new NotEqualConstraint(orangeJuice, coffee));
		csp.addConstraint(new NotEqualConstraint(orangeJuice, milk));
		csp.addConstraint(new NotEqualConstraint(tea, coffee));
		csp.addConstraint(new NotEqualConstraint(tea, milk));
		csp.addConstraint(new NotEqualConstraint(coffee, milk));

		csp.addConstraint(new NotEqualConstraint(zebra, dog));
		csp.addConstraint(new NotEqualConstraint(zebra, fox));
		csp.addConstraint(new NotEqualConstraint(zebra, snails));
		csp.addConstraint(new NotEqualConstraint(zebra, horse));
		csp.addConstraint(new NotEqualConstraint(dog, fox));
		csp.addConstraint(new NotEqualConstraint(dog, snails));
		csp.addConstraint(new NotEqualConstraint(dog, horse));
		csp.addConstraint(new NotEqualConstraint(fox, snails));
		csp.addConstraint(new NotEqualConstraint(fox, horse));
		csp.addConstraint(new NotEqualConstraint(snails, horse));		

		csp.addConstraint(new EqualConstraint(red, englishman));
		csp.addConstraint(new EqualConstraint(spaniard, dog));
		csp.addConstraint(new EqualConstraint(green, coffee));
		csp.addConstraint(new EqualConstraint(ukrainian , tea));
		csp.addConstraint(new SuccessorConstraint(green, ivory));
		csp.addConstraint(new EqualConstraint(oldGold, snails));
		csp.addConstraint(new EqualConstraint(kools, yellow));
		csp.addConstraint(new DifferByOneConstraint(chesterfields, fox));
		csp.addConstraint(new DifferByOneConstraint(kools, horse));
		csp.addConstraint(new EqualConstraint(luckyStrike, orangeJuice));
		csp.addConstraint(new EqualConstraint(japanese, parliaments));
		csp.addConstraint(new SuccessorConstraint(blue, norweigan));
		
		return csp;
	}

	private static void printSolution(Assignment solution) {
		// TODO print out useful answer
		// You can use the following to get the value assigned to a variable:
		// Object value = solution.getAssignment(var); 
		// For debugging it might be useful to print the complete assignment and check whether
		// it makes sense.
		Variable Zebra = new Variable("Zebra");
		Variable Water = new Variable("Water");
		Object solZebra = solution.getAssignment(Zebra);
		Object solWater = solution.getAssignment(Water);
		System.out.println("who drinks water? Who owns the zebra?");
		System.out.println("The person in house number " + solWater + " drinks water");
		System.out.println("The person in house number " + solZebra + " owns a zebra");
		System.out.println("solution:" + solution);
	}
	
	/**
	 * runs the CSP backtracking solver with the given parameters and print out some statistics
	 * @param description
	 * @param enableMRV
	 * @param enableDeg
	 * @param enableAC3
	 * @param enableLCV
	 */
	private static void findSolution(String description, boolean enableMRV, boolean enableDeg, boolean enableAC3, boolean enableLCV) {
		CSP csp = setupCSP();

		System.out.println("======================");
		System.out.println("running " + description);
		
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		SolutionStrategy solver = new ImprovedBacktrackingStrategy(enableMRV, enableDeg, enableAC3, enableLCV);
		final int nbAssignments[] = {0};
		solver.addCSPStateListener(new CSPStateListener() {
			@Override
			public void stateChanged(Assignment arg0, CSP arg1) {
				nbAssignments[0]++;
			}
			@Override
			public void stateChanged(CSP arg0) {}
		});
		Assignment solution = solver.solve(csp);
		endTime = System.currentTimeMillis();
		System.out.println("runtime " + (endTime-startTime)/1000.0 + "s" + ", number of assignments (visited states):" + nbAssignments[0]);
		printSolution(solution);
	}

	/**
	 * main procedure
	 */
	public static void main(String[] args) throws Exception {
		// run solver with different parameters
		findSolution("backtracking + AC3 + most constrained variable + least constraining value", true, true, true, true);
		findSolution("backtracking + AC3 + most constrained variable", true, true, true, false);
		findSolution("backtracking + AC3", false, false, true, false);
		findSolution("backtracking + forward checking + most constrained variable + least constraining value", true, true, false, true);
		findSolution("backtracking + forward checking + most constrained variable", true, true, false, false);
		findSolution("backtracking + forward checking", false, false, false, false);
	}

}

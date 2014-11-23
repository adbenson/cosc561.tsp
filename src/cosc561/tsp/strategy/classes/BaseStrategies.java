package cosc561.tsp.strategy.classes;

import cosc561.tsp.strategy.BranchAndBoundClassHeuristic;
import cosc561.tsp.strategy.BranchAndBoundPath;
import cosc561.tsp.strategy.BreadthFirstClassHeuristic;
import cosc561.tsp.strategy.BreadthFirstSearch;
import cosc561.tsp.strategy.Genetic;
import cosc561.tsp.strategy.SimulatedAnnealing;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.UniformCost;

enum BaseStrategies implements Strategies {
	BRANCH_BOUND (
			"Branch and Bound",
			BranchAndBoundPath.class),
	BRANCH_BOUND_CLASS (
			"Branch and Bound w/ Class Heuristic",
			BranchAndBoundClassHeuristic.class),
	GENETIC (
			"Genetic Algorithm",
			Genetic.class),
	SIMULATED_ANNEALING (
			"Simulated Annealing",
			SimulatedAnnealing.class),
	UNIFORM_COST (
			"Uniform Cost",
			UniformCost.class),
	BREADTH_FIRST (
			"Breadth-First Search",
			BreadthFirstSearch.class),
	BREADTH_FIRST_CLASS (
			"Breadth-First Search w/ Class Heuristic",
			BreadthFirstClassHeuristic.class);

	public final String name;
	public final Class<? extends Strategy> clazz;
	
	private BaseStrategies(String name, Class<? extends Strategy> clazz) {
		this.name = name;
		this.clazz = clazz;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public Class<? extends Strategy> getStrategyClass() {
		return clazz;
	}
}
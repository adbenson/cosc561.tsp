package cosc561.tsp.strategy.classes;

import cosc561.tsp.strategy.Strategy;

public interface Strategies {
	public static final Strategies[] ALL = new Strategies[] {
		BaseStrategies.BRANCH_BOUND,
		BaseStrategies.BRANCH_BOUND_CLASS,
		BaseStrategies.GENETIC,
		BaseStrategies.SIMULATED_ANNEALING,
		BaseStrategies.UNIFORM_COST,
		BaseStrategies.BREADTH_FIRST,
		BaseStrategies.BREADTH_FIRST_CLASS,
		TourGenerationStrategies.CONVEX_HULL,
		TourGenerationStrategies.GREEDY,
		TourGenerationStrategies.RANDOM,
		TourGenerationStrategies.WORST_FIRST,
	};
	
	public static final Strategies DEFAULT = BaseStrategies.GENETIC;
	
	public Class<? extends Strategy> getStrategyClass();
}
package cosc561.tsp.strategy.classes;

import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.strategy.p_approx.ConvexHull;
import cosc561.tsp.strategy.p_approx.Greedy;
import cosc561.tsp.strategy.p_approx.RandomPath;
import cosc561.tsp.strategy.p_approx.WorstFirstInsertion;

public enum TourGenerationStrategies implements StrategyClass {
	CONVEX_HULL (
			"ConvexHull",
			ConvexHull.class),
	GREEDY (
			"Greedy",
			Greedy.class),
	RANDOM (
			"Random",
			RandomPath.class),
	WORST_FIRST (
			"Worst-Best Insertion",
			WorstFirstInsertion.class);
	
	public final String name;
	public final Class<? extends Strategy> clazz;
	
	private TourGenerationStrategies(String name, Class<? extends Strategy> clazz) {
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
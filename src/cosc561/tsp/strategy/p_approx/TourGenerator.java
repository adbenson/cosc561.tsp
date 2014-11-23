package cosc561.tsp.strategy.tour_generation;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.Strategy;
import cosc561.tsp.view.MapWindow;

public abstract class TourGenerator extends Strategy {

	public TourGenerator(Graph graph, MapWindow window) {
		super(graph, window);
	}

}
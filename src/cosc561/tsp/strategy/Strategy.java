package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;
import cosc561.tsp.view.MapWindow;

public abstract class Strategy {
	
	protected Graph graph;
	protected MapWindow window;
	
	protected Strategy(Graph graph, MapWindow window) {
		this.graph = graph;
		this.window = window;
	}
	
	protected abstract Branch next();

	protected abstract void init();

	public abstract boolean isComplete();

}

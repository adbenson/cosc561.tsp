package cosc561.tsp.strategy;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Graph;

public abstract class Strategy {
	
	protected Graph graph;
	
	protected Strategy(Graph graph) {
		this.graph = graph;
	}
	
	protected abstract Branch next();

	protected abstract void init();

	public abstract boolean isComplete();

}

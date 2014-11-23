package cosc561.tsp.strategy.p_approx;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class RandomPath extends TourGenerator {
	
	private RichBranch path;
	
	private LinkedList<Node> unvisited;

	public RandomPath(Graph graph, MapWindow window) {
		super(graph, window);
	}

	@Override
	public void init() {
		Random random = new Random(System.nanoTime());
		
		path = new RichBranch(graph.getRoot(), graph);
		
		//It's harder to pick at random from a set.
		unvisited = new LinkedList<>(path.getUnvisited());
		
		Collections.shuffle(unvisited, random);
	}
	
	@Override
	protected RichBranch next() throws Exception {
		Node visit = unvisited.pop();
		
		path = new RichBranch(path, visit);
		
		return path;
	}

	@Override
	public boolean isComplete() {
		return path.isComplete();
	}

	@Override
	public RichBranch getSolution() {
		return path;
	}

}

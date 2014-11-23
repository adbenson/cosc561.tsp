package cosc561.tsp.strategy.p_approx;

import java.util.Iterator;
import java.util.LinkedList;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class WorstFirstInsertion extends TourGenerator {
	
	private RichBranch tour;
	
	private Iterator<Node> unvisited;
	private Node insert;
	
	private Iterator<Node> path;
	private Node after;
	
	public WorstFirstInsertion(Graph graph, MapWindow window) {
		super(graph, window);
	}
	

	@Override
	public void init() {
		tour = new RichBranch(graph.getRoot(), graph);
		
		unvisited = tour.getUnvisited().iterator();
		insert = unvisited.next();
		
		path = tour.getPath().iterator();
		after = path.next();
	}

	@Override
	protected RichBranch next() throws Exception {

		LinkedList<Node> testTour = new LinkedList<>(tour.getPath());
		
		testTour.add(testTour.indexOf(after) + 1, insert);
		
		RichBranch testBranch = new RichBranch(testTour, graph);
		
		RichBranch worst = null;
		if (worst == null || testBranch.weight > worst.weight){
			worst = testBranch;
		}
		
		tour = worst;
		
		if (! path.hasNext()) {
			insert = unvisited.next();
			path = tour.getPath().iterator();
		}
		
		after = path.next();
		
		return testBranch;
	}

	@Override
	public boolean isComplete() {
		return (!unvisited.hasNext()) && (!path.hasNext());
	}

	@Override
	public RichBranch getSolution() {
		return tour;
	}

}

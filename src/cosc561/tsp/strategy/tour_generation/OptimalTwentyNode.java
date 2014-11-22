package cosc561.tsp.strategy.tour_generation;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class OptimalTwentyNode extends TourGenerator {

	private static final int [] optimal = new int[] {
		18, 14, 17, 7, 2, 11, 19, 9, 8, 1, 15, 3, 13, 4, 10, 12, 16, 20, 5, 6
	};
	
	private int i;
	
	private RichBranch current;
	
	protected OptimalTwentyNode(Graph graph, MapWindow window) {
		super(graph, window);
	}

	@Override
	protected RichBranch next() throws Exception {
		current = new RichBranch(current, graph.getNode(optimal[i]));
		i++;
		
		return current;
	}

	@Override
	public void init() {
		current = new RichBranch(graph.getRoot(), graph);
		i = 0;
	}

	@Override
	public boolean isComplete() {
		return i >= optimal.length;
	}

	@Override
	public RichBranch getSolution() {
		return current;
	}

}

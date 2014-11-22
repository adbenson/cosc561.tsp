package cosc561.tsp.strategy;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class SimulatedAnnealing extends Strategy {

	protected SimulatedAnnealing(Graph graph, MapWindow window) {
		super(graph, window);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RichBranch next() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public RichBranch getSolution() {
		// TODO Auto-generated method stub
		return null;
	}

}

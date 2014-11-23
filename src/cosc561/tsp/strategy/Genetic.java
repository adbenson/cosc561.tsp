package cosc561.tsp.strategy;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.strategy.classes.TourGenerationStrategies;
import cosc561.tsp.view.MapWindow;

public class Genetic extends Strategy {
	
	private RichBranch current;

	public Genetic(Graph graph, MapWindow window) {
		super(graph, window);
	}

	@Override
	public void init() throws Exception {
		current = generate(TourGenerationStrategies.RANDOM);
	}

	@Override
	protected RichBranch next() throws Exception {
		// TODO Auto-generated method stub
		return null;
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

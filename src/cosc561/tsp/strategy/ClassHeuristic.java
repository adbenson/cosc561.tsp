package cosc561.tsp.strategy;

import java.util.PriorityQueue;

import cosc561.tsp.model.Branch;
import cosc561.tsp.model.Branch.SparseBranch;
import cosc561.tsp.model.Branch.SparseBranch.SparseBranchException;
import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.view.MapWindow;
import cosc561.tsp.view.Output;

public class ClassHeuristic extends Strategy {

	PriorityQueue<SparseBranch> branches;
	Branch current;
	
	private Output rejected;
	
	public ClassHeuristic(Graph graph, MapWindow window) {
		super(graph, window);
		
		rejected = new Output("Rejected candidates", 500);
		window.addOutput(rejected);
	}
	
	public void init() {
		
		branches = new PriorityQueue<>();
		
		current = new Branch(graph.getRoot(), graph.getNodes());
		
		rejected.setValue(0);
	}

	@Override
	protected Branch next() throws SparseBranchException {
		for(Node node : current.getUnvisited()) {
			if (nonIntersecting(current, node)) {
				branches.add(new Branch(current, node).getSparse());
			}
			else {
				rejected.increment();
				Thread.yield();
			}
		}
	
		current = new Branch(branches.poll(), graph);
		if (current == null) {
			System.err.println("branches empty");
		}
		
		return current;
	}

	private boolean nonIntersecting(Branch branch, Node node) {
		Edge newEdge = new Edge(branch.getEnd(), node);
		
		for (Edge edge : branch.getEdges()) {
			if (newEdge.intersects(edge)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean isComplete() {
		return current.isComplete();
	}
}

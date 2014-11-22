package cosc561.tsp.strategy;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import cosc561.tsp.Solver;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.model.branch.SparseBranch;
import cosc561.tsp.util.PartitionedQueue;
import cosc561.tsp.util.PriorityQueueSet;
import cosc561.tsp.view.MapWindow;

public class UniformCost extends Strategy {
	
	public class RootDistanceComparator implements Comparator<Node> {

		public RootDistanceComparator(Node root) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	private static final int QUEUE_SIZE = 100000;
	
	public UniformCost(Graph graph, MapWindow window) {
		super(graph, window);
	}

	PartitionedQueue<SparseBranch> branches;
	RichBranch current;
	
	Solver solver;
	
	PriorityQueueSet frontier;
	
	Set<Node> explored;
	
	Node root;
	
	public void init() {
		
		branches = new PartitionedQueue<>(QUEUE_SIZE);
		
		root = graph.getRoot();
		
		current = new RichBranch(root, graph);
		
		frontier = new PriorityQueueSet(graph.getNodes().size());
		frontier.add(current);
		
		explored = new HashSet<>();
	}
	
	/*
  node := root, cost = 0
  frontier := priority queue containing node only
  explored := empty set
  do
    if frontier is empty
      return failure
    node := frontier.pop()
    if node is goal
      return solution
    explored.add(node)
    for each of node's neighbors n
      if n is not in explored
        if n is not in frontier
          frontier.add(n)
        else if n is in frontier with higher cost
          replace existing node with n
	 */

	@Override
	protected RichBranch next() {
		if (frontier.isEmpty()) {
			System.err.println("No solution found using UniformCost search");
			return null;
		}
		
		current = new RichBranch(frontier.poll());
		
		if (frontier.isEmpty()){
			System.out.println("dont' care");
		}
		
		explored.add(current.getEnd());
		
		for (Node n : graph.getNodes()) {
			if (n == current.getEnd()) {
				continue;
			}
			
			if (!explored.contains(n)) {
				RichBranch next = new RichBranch(current, n);
				
				if (!frontier.containsEndNode(n)) {
					frontier.add(next);
				}
				else if (frontier.get(n).weight > next.weight) {
					frontier.add(new SparseBranch(next));
				}
			}
		}
		
		return current;
	}

	@Override
	public boolean isComplete() {
		return current.isComplete();
	}

	@Override
	public RichBranch getSolution() {
		return current;
	}
}

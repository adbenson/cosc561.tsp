package cosc561.tsp.strategy;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.branch.RichBranch;
import cosc561.tsp.view.MapWindow;

public class ConvexHull extends Strategy {

	protected ConvexHull(Graph graph, MapWindow window) {
		super(graph, window);
	}
	
	private Set<Node> external;
	
	private RichBranch branch;
	
	private Polygon hull;

	@Override
	public void init() {
		Polygon hull = new Polygon();
		
		List<Node> span = initialSpan(graph.getnodes());
		
		external = new HashSet<Node>(graph.getnodes());
		external.removeAll(span);
		
		branch = new RichBranch(span, graph);
	}

	@Override
	protected RichBranch next() throws Exception {
		if (!external.isEmpty()) {
			buildHull();
		}
		else {
			buildPath();
		}

		return branch;
	}

	private void buildPath() {
		// TODO Auto-generated method stub
	}

	private void buildHull() {
		edgeSearch:
		for (Edge edge : branch.getEdges()) {
			//Only extend one edge at a time, so the UI can see progress
			if (extendHull(edge)) {
				break edgeSearch;
			}
		}
	}

	private boolean extendHull(Edge edge) {
		Set<Node> outside = nodesOutside(edge);
		if (outside.size() < 1) {
			return false;
		}
		
		addFarthest(outside, edge);
		
		return true;
	}

	private void addFarthest(Set<Node> outside, Edge edge) {

		
		hull = new Polygon();
	}

	private Set<Node> nodesOutside(Edge edge) {
		Set<Node> outside = new HashSet<>();
		
		for (Node n : external) {
			if (nodeOutside(n, edge)) {
				outside.add(n);
			}
		}
		
		return outside;
	}

	private boolean nodeOutside(Node n, Edge edge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isComplete() {
		return branch.isComplete();
	}

	@Override
	public RichBranch getSolution() {
		return branch;
	}
	
	/*
	 * QUICK HULL
		1. Find the nodes with minimum and maximum x coordinates, those are bound to be part of the convex hull.
		2. Use the line formed by the two nodes to divide the set in two subsets of nodes, which will be processed recursively.
		3. Determine the Node, on one side of the line, with the maximum distance from the line. The two nodes found before along with this one form a triangle.
		4. The nodes lying inside of that triangle cannot be part of the convex hull and can therefore be ignored in the next steps.
		5. Repeat the previous two steps on the two lines formed by the triangle (not the initial line).
		6. Keep on doing so on until no more nodes are left, the recursion has come to an end and the nodes selected constitute the convex hull.
	 */
	
	/**
	 * Step 1
	 * @param nodes
	 * @return
	 */
	private List<Node> initialSpan(Collection<Node> nodes) {		
		Node min = null;
		Node max = null;
		
		for (Node n : nodes) {
			if (min == null || n.x < min.x) {
				min = n;
			}
			if (max == null || n.x > max.x) {
				max = n;
			}
		}
		
		List<Node> span = new ArrayList<>();
		span.add(min);
		span.add(max);
		
		hull.addPoint(min.x, min.y);
		hull.addPoint(max.x, max.y);
		
		return span;
	}

	private class QuickHull {
		
		public ArrayList<Node> quickHull(ArrayList<Node> nodes) {
			
			ArrayList<Node> convexHull = new ArrayList<Node>();
			
			if (nodes.size() < 3) {
				return new ArrayList<>(nodes);
			}

			int minNode = -1, maxNode = -1;
			int minX = Integer.MAX_VALUE;
			int maxX = Integer.MIN_VALUE;
			for (int i = 0; i < nodes.size(); i++) {
				if (nodes.get(i).x < minX) {
					minX = nodes.get(i).x;
					minNode = i;
				}
				if (nodes.get(i).x > maxX) {
					maxX = nodes.get(i).x;
					maxNode = i;
				}
			}
			Node A = nodes.get(minNode);
			Node B = nodes.get(maxNode);
			convexHull.add(A);
			convexHull.add(B);
			nodes.remove(A);
			nodes.remove(B);

			ArrayList<Node> leftSet = new ArrayList<Node>();
			ArrayList<Node> rightSet = new ArrayList<Node>();

			for (int i = 0; i < nodes.size(); i++) {
				Node p = nodes.get(i);
				if (nodeLocation(A, B, p) == -1)
					leftSet.add(p);
				else
					rightSet.add(p);
			}
			hullSet(A, B, rightSet, convexHull);
			hullSet(B, A, leftSet, convexHull);

			return convexHull;
		}

		public int distance(Node A, Node B, Node C) {
			int ABx = B.x - A.x;
			int ABy = B.y - A.y;
			int num = ABx * (A.y - C.y) - ABy * (A.x - C.x);
			if (num < 0)
				num = -num;
			return num;
		}

		public void hullSet(Node A, Node B, ArrayList<Node> set,
				ArrayList<Node> hull) {
			int insertPosition = hull.indexOf(B);
			if (set.size() == 0)
				return;
			if (set.size() == 1) {
				Node p = set.get(0);
				set.remove(p);
				hull.add(insertPosition, p);
				return;
			}
			int dist = Integer.MIN_VALUE;
			int furthestNode = -1;
			for (int i = 0; i < set.size(); i++) {
				Node p = set.get(i);
				int distance = distance(A, B, p);
				if (distance > dist) {
					dist = distance;
					furthestNode = i;
				}
			}
			Node P = set.get(furthestNode);
			set.remove(furthestNode);
			hull.add(insertPosition, P);

			// Determine who's to the left of AP
			ArrayList<Node> leftSetAP = new ArrayList<Node>();
			for (int i = 0; i < set.size(); i++) {
				Node M = set.get(i);
				if (nodeLocation(A, P, M) == 1) {
					leftSetAP.add(M);
				}
			}

			// Determine who's to the left of PB
			ArrayList<Node> leftSetPB = new ArrayList<Node>();
			for (int i = 0; i < set.size(); i++) {
				Node M = set.get(i);
				if (nodeLocation(P, B, M) == 1) {
					leftSetPB.add(M);
				}
			}
			hullSet(A, P, leftSetAP, hull);
			hullSet(P, B, leftSetPB, hull);

		}

		public int nodeLocation(Node A, Node B, Node P) {
			int cp1 = (B.x - A.x) * (P.y - A.y) - (B.y - A.y) * (P.x - A.x);
			return (cp1 > 0) ? 1 : -1;
		}

	}

}

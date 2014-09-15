package cosc561.tsp.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class Node {
	
	public final int x;
	public final int y;
	
	public HashMap<Node, Integer> distances;
	
	public Node(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		
		distances = new HashMap<>();
		//Arguably, I should be setting the initial capacity variably,
		// but that would be really awkward, and initial capacity isn't that important.
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Node)) {
			return false;
		}
		
		Node that = (Node)other;
		return (this.x == that.x && this.y == that.y);
	}
	
	public int distance(Node that) {
		if (!distances.containsKey(that)) {
			distances.put(that, (int)calculateDistance(that));
		}
		
		return distances.get(that);
	}
	
	public Node getNearest(Set<Node> nodes) {
		Node nearest = null;
		
		for (Node node : nodes) {
			if (nearest == null || distances.get(nearest) < distances.get(node)) {
				nearest = node;
			}
		}
		
		return nearest;
	}
	
	private double calculateDistance(Node that) {
		return Math.sqrt(Math.pow((this.x - that.x), 2) + Math.pow((this.y - that.y), 2));
	}
	
	private class DistanceComparator implements Comparator<Node> {

		@Override
		public int compare(Node n1, Node n2) {
			int distance1 = distances.get(n1);
			int distance2 = distances.get(n2);
			
			return distance1 - distance2;
		}
		
	}
//	
//	public boolean intersects(Node a1, Node a2, Node b1, Node b2) {
//		
//	}
	
	public static boolean intersect(Node a1, Node a2, Node b1, Node b2) {
	    return a1.x <= b2.x 
	        && a2.x >= b1.x 
	        && a1.y <= b2.y
	        && a2.y >= b1.y;
	}
	
}

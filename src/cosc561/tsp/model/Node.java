package cosc561.tsp.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class Node implements Comparable<Node> {
	
	private static int lastId = 0;

	public final int id;
	
	public final int x;
	public final int y;
	
	public HashMap<Node, Float> distances;
	
	public Node(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.id = lastId++;

		distances = new HashMap<>();
	}
	
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		else if (!(other instanceof Node)) {
			return false;
		}
		else {
			Node that = (Node)other;
			return (this.id == that.id);
		}
	}
	
	public float distance(Node that) {
		if (!distances.containsKey(that)) {
			distances.put(that, (float)calculateDistance(that));
		}
		
		return distances.get(that);
	}
	
	public Node getNearest(Set<Node> nodes) {
		Node nearest = null;
		
		for (Node node : nodes) {
			if (nearest == null || distance(nearest) < distance(node)) {
				nearest = node;
			}
		}
		
		return nearest;
	}
	
	public static int getLastId() {
		return lastId - 1;
	}
	
	private double calculateDistance(Node that) {
		return Math.sqrt(Math.pow((this.x - that.x), 2) + Math.pow((this.y - that.y), 2));
	}
	
	@SuppressWarnings("unused")
	private class DistanceComparator implements Comparator<Node> {

		@Override
		public int compare(Node n1, Node n2) {
			float distance1 = distances.get(n1);
			float distance2 = distances.get(n2);
			
			return (int) Math.signum(distance1 - distance2);
		}
		
	}
	
	public String toString() {
		return id+":"+x+","+y+"";
	}

	@Override
	public int compareTo(Node o) {
		int xd = this.x - o.x;
		if (xd == 0) {
			return this.y - o.y;
		}
		return xd;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
}

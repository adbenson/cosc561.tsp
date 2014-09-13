package cosc561.tsp.model;

public class Node {
	
	public final int x;
	public final int y;
	
	public Node(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Node)) {
			return false;
		}
		
		Node that = (Node)other;
		return (this.x == that.x && this.y == that.y);
	}
	
}

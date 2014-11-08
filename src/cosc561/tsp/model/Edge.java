package cosc561.tsp.model;

import java.awt.geom.Line2D;

public class Edge {
	
	public final Node start;
	public final Node end;
	
	public final float weight;
	
	public Edge(Node start, Node end) {
		this.start = start;
		this.end = end;
		weight = start.distance(end);
	}

	public boolean intersects(Edge that) {
		
		//Two segments sharing an endpoint arguably do intersect, 
		//but not for our purposes
		if (this.sharesNode(that)) {
			return false;
		}	
		
		// Ax1,Ay1,  Ax2,Ay2,  Bx1,By1,  Bx2,By2
		return Line2D.linesIntersect(
				this.start.x, this.start.y, this.end.x, this.end.y,
				that.start.x, that.start.y, that.end.x, that.end.y);
	}
	
	private boolean sharesNode(Edge that) {
		return this.start.equals(that.start) || 
				this.start.equals(that.end) || 
				this.end.equals(that.start) || 
				this.end.equals(that.end);
	}
	
	public String toString() {
		return "["+start+" -> "+end+"]";
	}

}

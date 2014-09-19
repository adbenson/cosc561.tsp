package cosc561.tsp.model;

public class Edge {
	
	public final Node start;
	public final Node end;
	
	public Edge(Node start, Node end) {
		this.start = start;
		this.end = end;
	}

	public boolean intersects(Edge that) {
		if (Geometry.doLinesIntersect(this, that)) {
			return true;
		}
		
//		if (this.sharesNode(that)) {
//			return false;
//		}
//		
//		if (boundsIntersect(that)) {
//			return true;
//		}
		
		
		return false;
	}
	
	private boolean sharesNode(Edge that) {
		return this.start.equals(that.start) || 
				this.end.equals(that.end) || 
				this.start.equals(that.end) || 
				this.end.equals(that.start);
	}

	public boolean boundsIntersect(Edge that) {
		return this.start.x <= that.end.x 
		        && this.end.x >= that.start.x 
		        && this.start.y <= that.end.y
		        && this.end.y >= that.start.y;
		
	}

}

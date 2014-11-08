package cosc561.tsp.model.branch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cosc561.tsp.model.Edge;
import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.util.Partitionable;

public class Branch implements Comparable<Branch>, Partitionable {
	private static final float PARITION_DIVISOR = 100f;
	private static final int PARTITION_FACTOR = 3;
	
	protected final float weight;
	public final byte[] path;

	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 */
	public Branch(Node start) {
		this.path = new byte[1];
		this.path[0] = (byte) start.id;
		this.weight = 0;
	}
	
	/**
	 * Initial Tour Constructor
	 * 
	 * @param start
	 */
	public Branch(List<Node> path, float weight) {
		this.path = sparsePath(path);
		this.weight = weight;
	}
	
	/**
	 * Copy Constructor
	 * 
	 * @param that
	 */
	public Branch(Branch that) {
		this.weight = that.weight;
		this.path = new byte[that.path.length];
		System.arraycopy(that.path, 0, this.path, 0, that.path.length);
	}

	/**
	 * Deflate Constructor
	 * 
	 * @param richPath
	 */
	public Branch(RichBranch richPath) {
		this(richPath.getPath(), richPath.weight);
	}

	/**
	 * Visit Constructor
	 * 
	 * @param that
	 * @param node
	 */
	public Branch(PathBranch that, Node node) {
		this.weight = that.weight + that.end.distance(node);

		List<Node> oldPath = that.getPath();
		oldPath.add(node);
		this.path = sparsePath(oldPath);
	}

	public float getWeight() {
		return weight;
	}
	
	public List<Node> nodePath(Graph graph) {
		List<Node> nodes = new ArrayList<Node>();
		
		for (byte id : path) {
			nodes.add(graph.getNode(id));
		}
		
		return nodes;
	}
	
	private static byte[] sparsePath(List<Node> path) {
		byte[] nodes = new byte[path.size()];
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = (byte) path.get(i).id;
		}
		
		return nodes;
	}
	
	@Override
	public int compareTo(Branch that) {
		return (int) Math.signum(this.weight - that.weight);
	}
	
	@Override
	public int getPartition() {
		return (int) Math.pow(this.weight / PARITION_DIVISOR, PARTITION_FACTOR);
	}
}
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
	
	private static final byte DEFAULT_PIVOT = Byte.MIN_VALUE;
	
	public final float weight;
	public final byte[] path;
	public final byte pivot;
	
	/**
	 * Complete Constructor
	 * 
	 * @param that
	 * @param node
	 */
	private Branch(byte[] path, float weight, int pivot) {
		this.path = path;
		this.weight = weight;
		this.pivot = (byte) pivot;
	}
	
	/**
	 * Pivotless Constructor
	 * 
	 * @param that
	 * @param node
	 */
	private Branch(byte[] path, float weight) {
		this(path, weight, DEFAULT_PIVOT);
	}

	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 */
	protected Branch(Node start) {
		this(new byte[] { (byte) start.id }, 0f);
	}

	/**
	 * Tour Constructor
	 * @param pivot 
	 * 
	 * @param start
	 */
	protected Branch(List<Node> path, float weight, int pivot) {
		this(sparsePath(path), weight, pivot);
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
		
		this.pivot = DEFAULT_PIVOT;
	}
	
	
	/**
	 * Copy / Deflate Constructor
	 * 
	 * @param that
	 */
	public Branch(Branch that) {
		this.weight = that.weight;
		this.path = new byte[that.path.length];
		this.pivot = that.pivot;
		System.arraycopy(that.path, 0, this.path, 0, that.path.length);
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
		return (int) this.weight / 100;//Math.pow(this.weight / PARITION_DIVISOR, PARTITION_FACTOR);
	}
}
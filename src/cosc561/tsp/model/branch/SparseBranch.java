package cosc561.tsp.model.branch;

import java.util.Comparator;

import cosc561.tsp.model.Graph;
import cosc561.tsp.model.Node;
import cosc561.tsp.model.Path;
import cosc561.tsp.util.Partitionable;

public class SparseBranch implements Comparable<SparseBranch>, Partitionable {

	private static final float PARITION_DIVISOR = 100f;
	private static final int PARTITION_FACTOR = 3;
	
	public static final byte UNDEFINED_PIVOT = Byte.MIN_VALUE;
	
	public final byte[] path;
	
	public final float weight;
	
	public final byte pivot;
	
	protected final Graph graph;
	
//	/**
//	 * Complete Constructor
//	 * 
//	 * @param that
//	 * @param node
//	 */
//	private Branch(byte[] path, float weight, int pivot) {
//		this.path = path;
//		this.weight = weight;
//		this.pivot = (byte) pivot;
//	}
//	
//	/**
//	 * Pivotless Constructor
//	 * 
//	 * @param that
//	 * @param node
//	 */
//	private Branch(byte[] path, float weight) {
//		this(path, weight, UNDEFINED_PIVOT);
//	}
	
	/**
	 * Initial Path Constructor
	 * 
	 * @param start
	 * @param path 
	 */
	protected SparseBranch(Node start, Graph graph) {
		this.path = new byte[] { (byte) start.id };
		
		this.weight = 0;
		
		this.pivot = UNDEFINED_PIVOT;
		
		this.graph = graph;
	}

	/**
	 * Initial Tour Constructor
	 * 
	 * @param start
	 * @param path 
	 */
	protected SparseBranch(Path path, Graph graph) {
		this.path = sparsePath(path);
		
		this.weight = calculateWeight(this.path, graph);
		
		this.pivot = UNDEFINED_PIVOT;
		
		this.graph = graph;
	}

//	/**
//	 * Tour Constructor
//	 * @param pivot 
//	 * 
//	 * @param start
//	 */
//	protected Branch(Path path, float weight, int pivot) {
//		this(sparsePath(path), weight, pivot);
//	}

	/**
	 * Visit Constructor
	 * 
	 * @param that
	 * @param node
	 */
	public SparseBranch(SparseBranch that, Node node) {
		this.path = copyPath(that.path, that.path.length + 1);
		this.path[this.path.length - 1] = (byte) node.id;
		
		this.weight = calculateWeight(this.path, that.graph);
		
		this.pivot = that.pivot;
		
		this.graph = that.graph;
	}
	
//	/**
//	 * Visit Constructor
//	 * 
//	 * @param that
//	 * @param node
//	 */
//	public Branch(PathBranch that, Node node, float weight) {
//		this.weight = weight;
//
//		Path oldPath = that.getPath();
//		oldPath.add(node);
//		this.path = sparsePath(oldPath);
//		
//		this.pivot = UNDEFINED_PIVOT;
//	}
//	

	/**
	 * Copy / Deflate Constructor
	 * 
	 * @param that
	 */
	public SparseBranch(SparseBranch that) {
		this.path = copyPath(that.path, that.path.length);
		
		this.weight = that.weight;
		
		this.pivot = that.pivot;
		
		this.graph = that.graph;
	}

	private static byte[] copyPath(byte[] in, int length) {
		byte[] out = new byte[length];
		System.arraycopy(in, 0, out, 0, in.length);
		return out;
	}
	
	private static byte[] sparsePath(Path path) {
		byte[] nodes = new byte[path.size()];
		
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = (byte) path.get(i).id;
		}
		
		return nodes;
	}
		
	private static float calculateWeight(byte[] path, Graph graph) {
		float weight = 0;
		
		//Initialize to last node
		Node previous = graph.getNode(path[path.length - 1]);
		for (byte id : path) {
			Node next = graph.getNode(id);
			weight += previous.distance(next);
			
			previous = next;
		}
		
		return weight;
	}
	
	@Override
	public int compareTo(SparseBranch that) {
		return (int) Math.signum(this.weight - that.weight);
	}
	
	@Override
	public int getPartition() {
		return (int) this.weight / 100;//Math.pow(this.weight / PARITION_DIVISOR, PARTITION_FACTOR);
	}
	
	public static class ReverseComparator implements Comparator<SparseBranch> {

		@Override
		public int compare(SparseBranch a, SparseBranch b) {
			return - (a.compareTo(b));
		}
		
	}
}
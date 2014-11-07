package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.List;

import cosc561.tsp.util.Partitionable;

public class SparseBranch implements Comparable<SparseBranch>, Partitionable {
	private static final float PARITION_DIVISOR = 100f;
	private static final int PARTITION_FACTOR = 3;
	
	public final short weight;
	public final byte[] path;

	public SparseBranch(Branch branch) {
		this.weight = (short) branch.weight;

		this.path = sparsePath(branch.path);
		
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
	public int compareTo(SparseBranch that) {
		return this.weight - that.weight;
	}
	
	@Override
	public int getPartition() {
		return (int) Math.pow(this.weight / PARITION_DIVISOR, PARTITION_FACTOR);
	}
}
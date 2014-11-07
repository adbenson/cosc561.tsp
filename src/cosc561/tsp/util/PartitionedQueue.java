package cosc561.tsp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

public class PartitionedQueue<Element extends Partitionable & Comparable<Element>> {
	
	private int defaultPartitionSize;
	
	private Map<Integer, Queue<Element>> partitions;
	
	private Queue<Element> head;
	
	public PartitionedQueue(int defaultPartitionSize) {
		this.defaultPartitionSize = defaultPartitionSize;
		partitions = new HashMap<>();
		head = null;
	}
	
	public void add(Element e) {
		int partition = e.getPartition();
		Queue<Element> queue;
		
		if (!partitions.containsKey(partition)) {
			queue = new PriorityQueue<>(defaultPartitionSize);
			partitions.put(partition, queue);
			
			if (head == null || partition < head.peek().getPartition()) {
				head = queue;
			}
		}
		
		queue = partitions.get(partition);
		
		queue.add(e);
	}
	
	public Element poll() {
		Element e = head.poll();
		
		if (head.isEmpty()) {
			partitions.remove(e.getPartition());
			System.out.println("emtpy queue");
			head = nextHead();
			
			//I know, conventional wisdom is that this is next to pointless.
			//But this is a moment where we have potentially freed up a bunch of memory.
			System.gc();
		}
		
		return e;
	}
	
	private Queue<Element> nextHead() {
		Queue<Element> head = null;
		int lowestPartition = Integer.MAX_VALUE;
		
		for (Entry<Integer, Queue<Element>> entry : partitions.entrySet()) {
			if (head == null || entry.getKey() < lowestPartition) {
				head = entry.getValue();
				lowestPartition = entry.getKey();
			}
		}
		
		return head;
	}

	public String stats() {
		StringBuilder sb = new StringBuilder();
		sb.append("Partitioned Queue\n");
		for (Map.Entry<Integer, Queue<Element>> e : partitions.entrySet()) {
			sb.append(e.getKey()+": \t"+e.getValue().size()+"\n");
		}
		return sb.toString();
	}
	
}

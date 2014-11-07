package cosc561.tsp.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
			
			if (head == null) {
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
	
	private class QueueHeadComparator<Type extends Comparable<Type>> implements Comparator<Queue<Type>> {

		@Override
		public int compare(Queue<Type> a, Queue<Type> b) {
			if (a.isEmpty()) {
				return -1;
			}
			else if (b.isEmpty()) {
				return 1;
			}
			
			return a.peek().compareTo(b.peek());
		}
		
	}
	
	private class SortedList<Type> {
		private List<Type> list;
		private Comparator<Type> comparator;
		
		public SortedList(Comparator<Type> c) {
			this.list = new LinkedList<>();
			this.comparator = c;
		}

		public void remove(Queue<Element> e) {
			list.remove(e);
			resort();
		}

		public boolean contains(Type e) {
			return list.contains(e);
		}

		public Type getHead() {
			return list.get(0);
		}

		public void resort() {
			Collections.sort(list, comparator);
		}

		public void add(Type queue) {
			list.add(queue);
			resort();
		}
		
	}

}

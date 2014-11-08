package cosc561.tsp.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.naming.OperationNotSupportedException;

public class PartitionedQueue<Element extends Partitionable & Comparable<Element>> implements Queue<Element> {
	
	private int defaultPartitionSize;
	
	private Map<Integer, Queue<Element>> partitions;
	
	private Queue<Element> head;
	
	public PartitionedQueue(int defaultPartitionSize) {
		this.defaultPartitionSize = defaultPartitionSize;
		partitions = new HashMap<>();
		head = null;
	}
	
	@Override
	public boolean add(Element e) {
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
		
		return queue.add(e);
	}
	
	@Override
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

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Partitioned Queue\nPart:\tSize\n");
		for (Map.Entry<Integer, Queue<Element>> e : partitions.entrySet()) {
			sb.append(e.getKey()+": \t"+e.getValue().size()+"\n");
		}
		return sb.toString();
	}

	public boolean isEmpty() {
		return head.isEmpty();
	}

	@Override
	public boolean addAll(Collection<? extends Element> elements) {
		boolean success = true;
		for(Element e : elements) {
			success = success && add(e);
		}
		
		return success;
	}

	@Override
	public void clear() {
		partitions = new HashMap<>();
		head = null;
	}

	@Override
	public boolean contains(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Element> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		int size = 0;
		for (Queue q : partitions.values()) {
			size += q.size();
		}
		return size;
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Element element() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean offer(Element e) {
		return add(e);
	}

	@Override
	public Element peek() {
		return head.peek();
	}

	@Override
	public Element remove() {
		throw new UnsupportedOperationException();
	}
	
}

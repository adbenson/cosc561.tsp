package cosc561.tsp.util;

import java.util.ArrayList;

public class Heap<T extends Comparable<T>> {
	
	private ArrayList<T> heap;
	private final ORDER order;
	
	enum ORDER {
		MAX, MIN
	}
	
	public Heap(int n, ORDER order) {
		heap = new ArrayList<T>(n);
		this.order = order;
	}
	
	public static <T extends Comparable<T>> ArrayList<T> 
										sort(ArrayList<T> array, ORDER order) {
		return sort(array, order, false);
	}
	
	public static <T extends Comparable<T>> ArrayList<T> 
						sort(ArrayList<T> array, ORDER order, boolean debug) {
		
		if (debug) System.out.println("Init heap.");
		Heap<T> h = new Heap<T>(array.size(), order);
		
		if (debug) System.out.println("Begin push.");
		for (int i = 0; i < array.size(); i++) {
			h.push(array.get(i));
		}
		
		if (debug) System.out.println("End push. Begin pop.");
		ArrayList<T> sorted = new ArrayList<T>(array.size());
		for (int i = 0; i < array.size(); i++) {
			sorted.set(i, h.pop());
		}
		
		if (debug) System.out.println("End sort.");
		return sorted;
	}
	
	public boolean isEmpty() {
		return (heap.size() <= 0);
	}
	
	public int size() {
		return heap.size();
	}
	
	private int last() {
		return heap.size() - 1;
	}
	
	public void push(T value) {
		heap.add(value);
		bubbleUp(last());
	}
	
	public T pop() {
		if (!isEmpty()) {
			T value = heap.get(0);
			heap.set(0, heap.remove(last()));
			bubbleDown(0);
			return value;
		}
		else {
			System.err.println("Pop called on empty heap. null returned.");
			return null;
		}
	}
	
	private void bubbleUp(int child) {
		
		if (child > 0) {
			int parent = parentOf(child);
			
			if ( ! inOrder(parent, child)) {
				swap(parent, child);
			}
			
			bubbleUp(parent);
		}
	}
	
	private void bubbleDown(int parent) {
		
		int left = leftChildOf(parent);
		int right = rightChildOf(parent);
		
		//If the further child is out of range, collapse the two
		//to simplify evaluation.
		if (right > last()) {
			right = left;
		}

		if (left <= last()) {
			if ( ! inOrder(parent, left) || ! inOrder(parent, right)) {
				if (inOrder(left, right)) {
					swap (left, parent);
					bubbleDown(left);
				}
				else {
					swap (right, parent);
					bubbleDown(right);
				}
			}
		}
	}
/*
	private static int[] childIndexOf(int parent) {
		int l = parent * 2;
		return new int[] {l+1, l+2};
	}
*/	
	private static int leftChildOf(int parent) {
		return (parent * 2) + 1;
	}
	
	private static int rightChildOf(int parent) {
		return (parent * 2) + 2;
	}
	
	private int furthestChildOf(int parent) {
		
		int l = leftChildOf(parent);
		int r = rightChildOf(parent);
		
		//Check if either child is out of order with the current parent		
		boolean swapLeft = (l <= last()) && (! inOrder(parent, l));
		boolean swapRight = (r <= last()) && (! inOrder(parent, r));
		
		//If they are both out of order, we want to pick the more distant child.
		//So we check if 
		if (swapLeft && swapRight) {
			swapRight = inOrder(r, l);
			swapLeft = inOrder(l, r);
		}
		
		//Cannot swap both! Should be assured by inOrder.
		assert(! (swapLeft && swapRight));
		
		//Swap with child, favoring left
		if (swapLeft) {
			swap (l, parent);
			bubbleDown(l);
		}
		if (swapRight) {
			swap (r, parent);
			bubbleDown(r);
		}
		
		return -1;
	}
	
	private static int parentOf(int child) {
		//Implicit floor
		return (child-1) / 2;
	}	
	
	private void swap(int i, int j) {
		T temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
	}
	
	//Returns FALSE iff:
	//	ORDER = MIN and 'parent' is greater than 'child'
	//or
	//	ORDER = MAX and 'parent' is less than 'child'
	//
	//Returns TRUE if 'parent' == 'child'
	private boolean inOrder(int parent, int child) {
		if (order == ORDER.MIN){
			return (heap.get(parent).compareTo(heap.get(child)) <= 0);
		}
		else {
			return (heap.get(parent).compareTo(heap.get(child)) >= 0);
		}
	}
	
	public String toString() {
		String s = "{";
		for (int i = 0; i <= last(); i++) {
			s += heap.get(i) + ((i<last())? ", " : "");
		}
		return s + "}";
	}
}

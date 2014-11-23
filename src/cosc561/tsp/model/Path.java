package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Path implements List<Node> {

	private final List<Node> path;
	
	//Edges will be evaluated lazily, as we don't need them in most cases,
	//but when we do, we need them repeatedly. 
	//So we'll keep track of whether they are up-to-date or not.
	private List<Edge> edges = null;
	private boolean edgesCurrent = false;

	public Path() {
		this(1);
	}

	public Path(int capacity) {
		path = new ArrayList<>(capacity);
	}

	public Path(byte[] path, Graph graph) {
		this(path.length);

		for (byte id : path) {
			this.path.add(graph.getNode(id));
		}
	}

	public Path(Collection<Node> path) {
		this.path = new ArrayList<>(path);
	}

	public Path(Collection<Node> path, Node add) {
		this.path = new ArrayList<>(path);
		this.path.add(add);
	}

	public Node getStart() {
		return path.get(0);
	}

	public Node getEnd() {
		return path.get(path.size() - 1);
	}

	public List<Edge> getEdges() {
		if (!edgesCurrent) {
			edges = buildEdges(path);
			edgesCurrent = true;
		}
		return new ArrayList<Edge>(edges);
	}

	public Path getTour() {
		List<Node> tour = new ArrayList<>(path);
		tour.add(getStart());
		return new Path(tour);
	}

	public boolean wouldIntersect(Node node) {
		edges = getEdges();
		
		if (path.size() < 3) {
			return false;
		}

		Edge newEdge = new Edge(getEnd(), node);
		Edge closeEdge = new Edge(node, getStart());

		for (Edge edge : edges) {
			if (newEdge.intersects(edge) || closeEdge.intersects(edge)) {
				return true;
			}
		}

		return false;
	}

	private static List<Edge> buildEdges(List<Node> path) {
		List<Edge> edges = new ArrayList<>(path.size() - 1);
		
		if (path.size() < 2) {
			return edges;
		}

		Node previous = null;
		for (Node node : path) {
			if (previous != null) {
				edges.add(new Edge(previous, node));
			}

			previous = node;
		}
		
		return edges;
	}

	@Override
	public int size() {
		return path.size();
	}

	@Override
	public boolean isEmpty() {
		return path.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return path.contains(o);
	}

	@Override
	public Object[] toArray() {
		return path.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return path.toArray(a);
	}

	@Override
	public String toString() {
		return path.toString();
	}

	@Override
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {
			private final Iterator<? extends Node> i = path.iterator();

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Node next() {
				return i.next();
			}

			@Override
			public void remove() {
				i.remove();
				edgesCurrent = false;
			}
		};
	}

	@Override
	public boolean add(Node node) {
		edgesCurrent = false;
		return path.add(node);
	}

	@Override
	public boolean remove(Object o) {
		edgesCurrent = false;
		return path.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> coll) {
		return path.containsAll(coll);
	}

	@Override
	public boolean addAll(Collection<? extends Node> coll) {
		edgesCurrent = false;
		return path.addAll(coll);
	}

	@Override
	public boolean removeAll(Collection<?> coll) {
		edgesCurrent = false;
		return path.removeAll(coll);
	}

	@Override
	public boolean retainAll(Collection<?> coll) {
		edgesCurrent = false;
		return path.retainAll(coll);
	}

	@Override
	public void clear() {
		edgesCurrent = false;
		path.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this){
			return true;
		}
		else if (o instanceof Path){
			Path that = (Path)o;
			return this.path.equals(that.path);
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return path.hashCode();
	}

	@Override
	public Node get(int index) {
		return path.get(index);
	}

	@Override
	public Node set(int index, Node element) {
		edgesCurrent = false;
		return path.set(index, element);
	}

	@Override
	public void add(int index, Node element) {
		edgesCurrent = false;
		path.add(index, element);
	}

	@Override
	public Node remove(int index) {
		edgesCurrent = false;
		return path.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return path.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return path.lastIndexOf(o);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Node> c) {
		edgesCurrent = false;
		return path.addAll(index, c);
	}

	@Override
	public ListIterator<Node> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<Node> listIterator(final int index) {
		return new ListIterator<Node>() {
			private final ListIterator<Node> i = path.listIterator(index);

			@Override
			public boolean hasNext() {
				return i.hasNext();
			}

			@Override
			public Node next() {
				return i.next();
			}

			@Override
			public boolean hasPrevious() {
				return i.hasPrevious();
			}

			@Override
			public Node previous() {
				return i.previous();
			}

			@Override
			public int nextIndex() {
				return i.nextIndex();
			}

			@Override
			public int previousIndex() {
				return i.previousIndex();
			}

			@Override
			public void remove() {
				edgesCurrent = false;
				i.remove();
			}

			@Override
			public void set(Node e) {
				edgesCurrent = false;
				i.set(e);
			}

			@Override
			public void add(Node e) {
				edgesCurrent = false;
				i.add(e);
			}
		};
	}

	public List<Node> subList(int fromIndex, int toIndex) {
		return new Path(path.subList(fromIndex, toIndex));
	}
}

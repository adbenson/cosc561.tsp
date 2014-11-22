package cosc561.tsp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Path implements List<Node> {
	
	private final List<Node> path;
	private final List<Edge> edges;
	
	public Path() {
		this(1);
	}
	
	public Path(int capacity) {
		path = new ArrayList<>(capacity);
		edges = new ArrayList<>(capacity - 1);
	}

	public Path(byte[] path, Graph graph) {
		this(path.length);
		
		for (byte id : path) {
			this.path.add(graph.getNode(id));
		}
		
		buildEdges();
	}
	
	public Path(Collection<Node> path) {
		this.path = new ArrayList<>(path);
		this.edges = new ArrayList<>(path.size() - 1);
		buildEdges();
	}
	
	public Path(Collection<Node> path, Node add) {
		this.path = new ArrayList<>(path);
		this.path.add(add);
		this.edges = new ArrayList<>(path.size() - 1);
		buildEdges();
	}
	
	public Node getStart() {
		return path.get(0);
	}
	
	public Node getEnd() {
		return path.get(path.size() - 1);
	}

	public List<Edge> getEdges() {
		return new ArrayList<>(edges);
	}

	public Path getTour() {
		List<Node> tour = new ArrayList<>(path);
		tour.add(getStart());
		return new Path(tour);
	}
	
	public boolean wouldIntersect(Node node) {
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

	private void buildEdges() {		
		if (path.size() < 2) {
			return;
		}
		
		Node previous = null;
		for (Node node : path) {
			if (previous != null) {
				edges.add(new Edge(previous, node));
			}
			
			previous = node;
		}
	}
	
	@Override
    public int size()                   {return path.size();}
	@Override
    public boolean isEmpty()            {return path.isEmpty();}
	@Override
    public boolean contains(Object o)   {return path.contains(o);}
	@Override
    public Object[] toArray()           {return path.toArray();}
	@Override
    public <T> T[] toArray(T[] a)       {return path.toArray(a);}
	@Override
    public String toString()            {return path.toString();}

	@Override
    public Iterator<Node> iterator() {
        return new Iterator<Node>() {
            private final Iterator<? extends Node> i = path.iterator();

        	@Override
            public boolean hasNext() {return i.hasNext();}
        	@Override
            public Node next()          {return i.next();}
        	@Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

	@Override
    public boolean add(Node e) {
        throw new UnsupportedOperationException();
    }
	@Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

	@Override
    public boolean containsAll(Collection<?> coll) {
        return path.containsAll(coll);
    }
	@Override
    public boolean addAll(Collection<? extends Node> coll) {
        throw new UnsupportedOperationException();
    }
	@Override
    public boolean removeAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }
	@Override
    public boolean retainAll(Collection<?> coll) {
        throw new UnsupportedOperationException();
    }
	@Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

	@Override
    public boolean equals(Object o) {return o == this || path.equals(o);}
	@Override
    public int hashCode()           {return path.hashCode();}

	@Override
    public Node get(int index) {return path.get(index);}
	@Override
    public Node set(int index, Node element) {
        throw new UnsupportedOperationException();
    }
	@Override
    public void add(int index, Node element) {
        throw new UnsupportedOperationException();
    }
	@Override
    public Node remove(int index) {
        throw new UnsupportedOperationException();
    }
	@Override
    public int indexOf(Object o)            {return path.indexOf(o);}
	@Override
    public int lastIndexOf(Object o)        {return path.lastIndexOf(o);}
	@Override
    public boolean addAll(int index, Collection<? extends Node> c) {
        throw new UnsupportedOperationException();
    }
	@Override
    public ListIterator<Node> listIterator()   {return listIterator(0);}

	@Override
    public ListIterator<Node> listIterator(final int index) {
        return new ListIterator<Node>() {
            private final ListIterator<? extends Node> i = path.listIterator(index);

        	@Override
            public boolean hasNext()     {return i.hasNext();}
        	@Override
            public Node next()              {return i.next();}
        	@Override
            public boolean hasPrevious() {return i.hasPrevious();}
        	@Override
            public Node previous()          {return i.previous();}
        	@Override
            public int nextIndex()       {return i.nextIndex();}
        	@Override
            public int previousIndex()   {return i.previousIndex();}

        	@Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        	@Override
            public void set(Node e) {
                throw new UnsupportedOperationException();
            }
        	@Override
            public void add(Node e) {
                throw new UnsupportedOperationException();
            }
        };
    }

    public List<Node> subList(int fromIndex, int toIndex) {
        return new Path(path.subList(fromIndex, toIndex));
    }
}

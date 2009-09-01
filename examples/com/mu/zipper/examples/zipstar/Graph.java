package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Graph {

	private final List<Node> nodes;
	private final Map<Object, Edge> edges;
	
	public Graph() {
		super();
		this.nodes = new ArrayList<Node>();
		this.edges = new HashMap<Object, Edge>();
	}

	public List<Node> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	public Collection<Edge> getEdges() {
		return Collections.unmodifiableCollection(edges.values());
	}
	
	public List<Node> adjacentNodesFrom(final Node from) {
		List<Node> result = new ArrayList<Node>();
		for (Iterator<Edge> i = edges.values().iterator(); i.hasNext(); ) {
			Edge e = i.next();
			if (e.getFrom().equals(from)) {
				result.add(e.getTo());
			}
		}
		return result;
	}
	
	public Node newNode(final String name, double coordX, double coordY) {
		Node node = new Node(this, name, coordX, coordY);
		nodes.add(node);
		return node;
	}
	
	public void removeNode(final Node node) {
		nodes.remove(node);
		for (Iterator<Edge> i = edges.values().iterator(); i.hasNext(); ) {
			Edge e = i.next();
			if (e.from.equals(node) || e.to.equals(node)) {
				i.remove();
			}
		}
	}
	
	public void connect(final Node n1, final Node n2, double weight) {
		assert(nodes.contains(n1));
		assert(nodes.contains(n2));
		edges.put(new Key(n1, n2), new Edge(n1, n2, weight));
		edges.put(new Key(n2, n1),new Edge(n2, n1, weight));
	}
	
	public void direct(final Node n1, final Node n2, double weight) {
		assert(nodes.contains(n1));
		assert(nodes.contains(n2));
		edges.put(new Key(n1, n2), new Edge(n1, n2, weight));
	}
	
	public void disconnect(final Node n1, final Node n2) {
		edges.remove(new Key(n1, n2));
		edges.remove(new Key(n2, n1));
	}
	
	public double getWeight(Node from, Node to) {
		Edge e = edges.get(new Key(from, to));
		if (e != null) {
			return e.getWeight();
		}
		throw new IllegalStateException("No edge from " + from.getName() + " to " + to.getName() + " exists.");
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Node node : getNodes()) {
			buf.append(node.toString()).append('\n');
		}
		return buf.toString();
	}
	
	class Edge {
		
		private final Node from;
		private final Node to;
		private final double weight;
		
		public Edge(Node from, Node to, double weight) {
			super();
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
		public Node getFrom() {
			return from;
		}

		public Node getTo() {
			return to;
		}

		public double getWeight() {
			return weight;
		}

	}
	
	private class Key {
		
		private final Node from;
		private final Node to;
		
		public Key(Node from, Node to) {
			super();
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((from == null) ? 0 : from.hashCode());
			result = prime * result + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
		}

	}

}

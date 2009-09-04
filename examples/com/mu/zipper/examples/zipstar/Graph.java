package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple graph implementation.
 * The graph contains a list of nodes and 
 * directional edges. All nodes are named 
 * and have a 2D coordinate. The edges are
 * weighted.
 * Nodes and edges are created using constructor 
 * methods <tt>newNode()</tt>, <tt>connect()</tt>
 * and <tt>direct()</tt>.
 * 
 * @author Adam Smyczek
 */
public final class Graph {

	private final List<Node> nodes;
	private final Map<Object, Edge> edges;
	
	public Graph() {
		super();
		this.nodes = new ArrayList<Node>();
		this.edges = new Hashtable<Object, Edge>();
	}

	/**
	 * Create a new node associated with this graph.
	 * 
	 * @param name the name of the node
	 * @param coordX x coordinate
	 * @param coordY y coordinate
	 * @return a new node
	 */
	public Node newNode(final String name, double coordX, double coordY) {
		Node node = new Node(this, name, coordX, coordY);
		nodes.add(node);
		return node;
	}
	
	/**
	 * Creates two edges <tt>n1->n2</tt> and <tt>n2->n1</tt>, both with 
	 * the weight <tt>weight</tt>.
	 * 
	 * @param n1 node 1
	 * @param n2 node 2
	 * @param weight the edge weight/cost
	 */
	public void connect(final Node n1, final Node n2, double weight) {
		assert(nodes.contains(n1));
		assert(nodes.contains(n2));
		edges.put(new Key(n1, n2), new Edge(n1, n2, weight));
		edges.put(new Key(n2, n1),new Edge(n2, n1, weight));
	}
	
	/**
	 * Create one directional edge <tt>n1->n2</tt>.
	 * 
	 * @param n1 node 1
	 * @param n2 node 2
	 * @param weight the weight/cost of the edge
	 */
	public void direct(final Node n1, final Node n2, double weight) {
		assert(nodes.contains(n1));
		assert(nodes.contains(n2));
		edges.put(new Key(n1, n2), new Edge(n1, n2, weight));
	}
	
	/**
	 * Removes the node and all adjacent edges from the graph.
	 * 
	 * @param node to remove
	 */
	public void removeNode(final Node node) {
		nodes.remove(node);
		for (Iterator<Edge> i = edges.values().iterator(); i.hasNext(); ) {
			Edge e = i.next();
			if (e.from.equals(node) || e.to.equals(node)) {
				i.remove();
			}
		}
	}
	
	/**
	 * Remove all edges between nodes n1 and n2.
	 * 
	 * @param n1
	 * @param n2
	 */
	public void disconnect(final Node n1, final Node n2) {
		edges.remove(new Key(n1, n2));
		edges.remove(new Key(n2, n1));
	}
	
	// ---- Accessors ----
	
	public List<Node> getNodes() {
		return Collections.unmodifiableList(nodes);
	}

	public Collection<Edge> getEdges() {
		return Collections.unmodifiableCollection(edges.values());
	}
	
	/**
	 * Returns all reachable nodes from the <tt>from</tt> node.
	 * 
	 * @param from node
	 * @return list of all reachable nodes
	 */
	public List<Node> adjacentNodesFrom(final Node from) {
		List<Node> nodes = new ArrayList<Node>();
		for (Iterator<Edge> i = edges.values().iterator(); i.hasNext(); ) {
			Edge e = i.next();
			if (e.from.equals(from)) {
				nodes.add(e.to);
			}
		}
		return Collections.unmodifiableList(nodes);
	}
	
	/**
	 * Edge weight/cost of the edge <tt>from->to</tt>.
	 * 
	 * @param from node
	 * @param to node
	 * @return the weight/cost
	 * @throws IllegalStateException from to nodes are not direct connected.
	 */
	public double getWeight(Node from, Node to) {
		Edge e = edges.get(new Key(from, to));
		if (e != null) {
			return e.weight;
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
	
	
	/**
	 * Internal edge class.
	 */
	private class Edge {
		
		private final Node from;
		private final Node to;
		private final double weight;
		
		public Edge(Node from, Node to, double weight) {
			super();
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
	}
	
	/**
	 * Hashtable key for edge objects.
	 */
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

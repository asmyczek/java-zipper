package com.mu.zipper.examples.zipstar;

import java.util.Collection;

/**
 * A graph node.
 * 
 * @author Adam Smyczek
 */
public final class Node {
	
	private final String name;
	private final double coordX;
	private final double coordY;
	
    private final Graph graph;
    
	// Cached reachable nodes
	private Collection<Node> adjacentNodes = null;
	
	/**
	 * Protected constructor. 
	 * Create a node using <tt>newNode()</tt> constructor
	 * method of the Graph class.
	 * 
	 * @param graph the associated graph
	 * @param name node name
	 * @param coordX x coordinate
	 * @param coordY y coordinate
	 */
	protected Node(final Graph graph, final String name, final double coordX, final double coordY) {
		super();
		this.graph = graph;
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	// ---- Accessors ----
	
	public String getName() {
		return name;
	}

	public double getCoordX() {
		return coordX;
	}

	public double getCoordY() {
		return coordY;
	}
	
	/**
	 * Cached collection of nodes reachable from this node.
	 * 
	 * @return the nodes reachable from this node
	 */
	public Collection<Node> adjacentNodes() {
		if (adjacentNodes == null) {
			adjacentNodes = graph.adjacentNodesFrom(this);
		}
		return adjacentNodes;
	}
	
	
	/**
	 * Edge weight/cost from this not to <tt>to</tt? node.
	 * 
	 * @param to node
	 * @return the edge weight/cost
	 * @throws IllegalStateException this and <tt>to</tt> node are not direct connected. 
	 */
	public double edgeWeightTo(final Node to) {
		return graph.getWeight(this, to);
	}

	/**
	 * Calculates air distance between this node coordinate
	 * and <tt>to</tt> node.
	 * 
	 * @param to node
	 * @return the air distance
	 */
	public double directDistanceTo(Node to) {
		double dx = Math.abs(coordX - to.getCoordX());
		double dy = Math.abs(coordY - to.getCoordY());
		return Math.sqrt(dx*dx + dy*dy);
	}

	@Override
	public String toString() {
		return getName();
	}
	
}

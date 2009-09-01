package com.mu.zipper.examples.zipstar;

import java.util.Collection;


public class Node {
	
    private final Graph graph;
	private final String name;
	private final double coordX;
	private final double coordY;
	private Collection<Node> adjacentNodes = null;
	
	protected Node(final Graph graph, final String name, final double coordX, final double coordY) {
		super();
		this.graph = graph;
		this.name = name;
		this.coordX = coordX;
		this.coordY = coordY;
	}

	public String getName() {
		return name;
	}

	public double getCoordX() {
		return coordX;
	}

	public double getCoordY() {
		return coordY;
	}
	
	public Collection<Node> adjacentNodes() {
		if (adjacentNodes == null) {
			adjacentNodes = graph.adjacentNodesFrom(this);
		}
		return adjacentNodes;
	}
	
	public double edgeWeightTo(final Node to) {
		return graph.getWeight(this, to);
	}

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

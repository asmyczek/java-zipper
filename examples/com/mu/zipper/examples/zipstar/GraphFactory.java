package com.mu.zipper.examples.zipstar;

public class GraphFactory {

	public static Graph simpleTestGraph() {
		Graph graph = new Graph();
		Node n1 = graph.newNode("n1", 5, 5);
		Node n2 = graph.newNode("n2", 3, 4);
		Node n3 = graph.newNode("n3", 1, 4);
		Node n4 = graph.newNode("n4", 6, 4);
		Node n5 = graph.newNode("n5", 5, 0);
		
		graph.connect(n1, n2, 2);
		graph.connect(n2, n3, 2);
		graph.connect(n3, n5, 1);
		graph.connect(n1, n4, 1);
		graph.connect(n4, n5, 5);
		
		return graph;
	}
	
}

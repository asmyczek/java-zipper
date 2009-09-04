package com.mu.zipper.examples.zipstar;

import java.util.List;

/**
 * Few graph examples to play with.
 * 
 * @author Adam Smyczek
 */
public final class GraphFactory {

	/**
	 * Just a simple graph for algorithm testing.
	 * 
	 * @return a graph representing two paths from n1 to n5
	 */
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
	
	/**
	 * A grid with all adjacent grid nodes connected.
	 * 
	 * @param width of the grid
	 * @param height of the grid
	 * @return the grid graph
	 */
	public static Graph grid(int width, int height) {
		Graph graph = new Graph();
		buildGrid(graph, 0, 0, width, height);
		return graph;
	}
	
	/**
	 * Two rooms simulation. The rooms are connected by a 1x1 grid
	 * at the right side of the of the rooms.
	 * 
	 * @param roomSize width and height of one room
	 * @return the rooms grid
	 */
	public static Graph twoRoom(int roomSize) {
		Graph graph = new Graph();
		buildGrid(graph, 0, 0, roomSize, roomSize);
		buildGrid(graph, 0, roomSize, roomSize, roomSize);
		
		// Connect rooms
		List<Node> nodes = graph.getNodes();
		graph.connect(nodes.get(roomSize*roomSize-1), nodes.get((roomSize+1)*roomSize-1), 1.0);
		graph.connect(nodes.get(roomSize*roomSize-1), nodes.get((roomSize+1)*roomSize-2), 1.0);
		graph.connect(nodes.get(roomSize*roomSize-2), nodes.get((roomSize+1)*roomSize-1), 1.0);
		graph.connect(nodes.get(roomSize*roomSize-2), nodes.get((roomSize+1)*roomSize-2), 1.0);
		
		return graph;
	}
	
	/**
	 * A helper function to build grids.
	 * 
	 * @param graph the graph
	 * @param x left coordinate
	 * @param y bottom coordinate
	 * @param width width of the grid
	 * @param height height of the grid
	 */
	protected static void buildGrid(final Graph graph, int x, int y, int width, int height) {
		Node[][] matrix = new Node[height][width];
		// Create nodes
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				matrix[row][col] = graph.newNode(String.format("(%1$s,%2$s)", y + row, x + col), y, x);
			}
		}
		
		// Create edges, in the most inefficient way.
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				for (int top = row - 1; top <= row + 1; top++) {
					for (int left = col - 1; left <= col + 1; left++) {
						if (top >= 0 && top < height && left >= 0 && left < width) {
							if (top != row || left != col) {
    							graph.direct(matrix[row][col], matrix[top][left], 1.0);
							}
						}
					}
				}
			}
		}
		
	}
	
}

package com.mu.zipper.examples.zipstar;

import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

public class GraphFactoryTest extends TestCase {

	@Test
	public void testBuildGrid() {
		int rows = 2;
		int cols = 3;
		
		Node[][] m = new Node[rows][cols]; 
		
		// Build grid
		Graph graph = new Graph();
		GraphFactory.buildGrid(graph, 1, 1, cols, rows);
		
		// Test nodes
		Iterator<Node> i = graph.getNodes().iterator();
		for (int r = 0; r < rows; r++) {
    		for (int c = 0; c < cols; c++) {
    			Node n = i.next();
    			m[r][c] = n;
    			assertEquals(String.format("(%1$s,%2$s)", r + 1, c + 1), n.getName());
    		}
		}
		
		// Test edges
		assertEquals(1.0, graph.getWeight(m[0][0], m[0][1]));
		assertEquals(1.0, graph.getWeight(m[0][0], m[1][1]));
		assertEquals(1.0, graph.getWeight(m[0][0], m[1][0]));
		
		assertEquals(1.0, graph.getWeight(m[0][1], m[0][0]));
		assertEquals(1.0, graph.getWeight(m[1][1], m[0][0]));
		assertEquals(1.0, graph.getWeight(m[1][0], m[0][0]));
		
		assertEquals(1.0, graph.getWeight(m[rows-1][cols-1], m[rows-2][cols-1]));
		assertEquals(1.0, graph.getWeight(m[rows-1][cols-1], m[rows-2][cols-2]));
		assertEquals(1.0, graph.getWeight(m[rows-1][cols-1], m[rows-1][cols-2]));
		
		assertEquals(1.0, graph.getWeight(m[rows-2][cols-1], m[rows-1][cols-1]));
		assertEquals(1.0, graph.getWeight(m[rows-2][cols-2], m[rows-1][cols-1]));
		assertEquals(1.0, graph.getWeight(m[rows-1][cols-2], m[rows-1][cols-1]));
		
	}
	
}

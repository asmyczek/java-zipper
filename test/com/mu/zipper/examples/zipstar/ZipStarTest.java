package com.mu.zipper.examples.zipstar;

import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

public class ZipStarTest extends TestCase {

	@Test
	public void testGraph() {
		// Simple graph
		Graph graph = new Graph();
		Node n1 = graph.newNode("n1", 0, 0);
		Node n2 = graph.newNode("n2", 3, 4);
		Node n3 = graph.newNode("n3", 6, 8);

		// Test distance
		assertEquals(5.0, n1.directDistanceTo(n2));
		assertEquals(5.0, n2.directDistanceTo(n1));
		assertEquals(10.0, n1.directDistanceTo(n3));
		assertEquals(10.0, n3.directDistanceTo(n1));
		assertEquals(5.0, n2.directDistanceTo(n3));
		assertEquals(5.0, n3.directDistanceTo(n2));
		assertEquals(0.0, n1.directDistanceTo(n1));
		assertEquals(0.0, n2.directDistanceTo(n2));
		
		// Test edges
		graph.connect(n1, n2, 10);
		assertEquals(2, graph.getEdges().size());
		assertEquals(1, n1.adjacentNodes().size());
		assertEquals(n2, n1.adjacentNodes().iterator().next());
		
		graph.disconnect(n1, n2);
		assertEquals(0, graph.getEdges().size());
		assertEquals(1, n1.adjacentNodes().size());
		assertEquals(0, n2.adjacentNodes().size());
		assertEquals(n2, n1.adjacentNodes().iterator().next());
		
		graph.direct(n1, n2, 10);
		assertEquals(1, graph.getEdges().size());
		
		graph.disconnect(n1, n2);
		assertEquals(0, graph.getEdges().size());
	}
	
	@Test
	public void testSimpleGraphPathSearch() {
		Graph graph = GraphFactory.simpleTestGraph();
		Node start = graph.getNodes().get(0);
		Node end = graph.getNodes().get(4);
		
		Path path = ZipStar.calcPath(graph, start, end);
		
		assertEquals(6.0, path.getDistance());
		Iterator<Node> i = path.getPath().iterator();
		assertEquals("n1", i.next().getName());
		assertEquals("n4", i.next().getName());
		assertEquals("n5", i.next().getName());
		assertFalse(i.hasNext());
		
		// Debug output
		System.out.println("---- Simple graph ----");
		System.out.println("Distance: " + path.getDistance());
		for (Node n : path.getPath()) {
			System.out.println(" " + n.getName());
		}
		
	}
	
	@Test
	public void testTwoRoomsPathSearch() {
		Graph graph = GraphFactory.twoRoom(6);
		Node start = graph.getNodes().get(0);
		Node end = graph.getNodes().get(graph.getNodes().size() - 6);
		
		Path path = ZipStar.calcPath(graph, start, end);
		
		assertEquals(11.0, path.getDistance());
		assertEquals(12, path.getPath().size());
		
		// Debug output
		System.out.println("---- Two rooms graph ----");
		System.out.println("Distance: " + path.getDistance());
		for (Node n : path.getPath()) {
			System.out.println(" " + n.getName());
		}
		
	}
}

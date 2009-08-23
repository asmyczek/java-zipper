package com.mu.zipper;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class ZipperTest extends TestCase {

	private Loc<Node> root = null;
	
	@Override
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		// Test tree
		Node b1 = new Node("b1", true);
		Node b2 = new Node("b2", true);
		
		Node c1 = new Node("c1", true);
		Node c2 = new Node("c2", true);
		
		Node a1 = new Node("a1", b1, b2);
		Node a2 = new Node("a2", c1, c2);
		Node a3 = new Node("a3", false);
		
		root = Zipper.newZipper(new Node("root", a1, a2, a3));
	}

	@Test
	public void testValidMoves() {
		assertEquals("root", nodeName(root));
		assertEquals("a1", nodeName(root.down()));
		assertEquals("a2", nodeName(root.down().right()));
		assertEquals("a1", nodeName(root.down().right().left()));
		assertEquals("root", nodeName(root.down().right().up()));
		
		assertEquals("a1", nodeName(root.next()));
		assertEquals("b1", nodeName(root.next().next()));
		assertEquals("b2", nodeName(root.next().next().next()));
		assertEquals("a2", nodeName(root.next().next().next().next()));
		
		
		assertEquals("a3", nodeName(root.next().rightMost()));
		assertEquals("a1", nodeName(root.next().right().leftMost()));
		
		Loc<Node> a2 = root.down().right();
		assertEquals("c1", nodeName(a2.down()));
		assertEquals("a3", nodeName(a2.right()));
		
	}
	
	@Test
	public void testPredicates() {
		assertTrue(root.isTop());
		assertTrue(root.down().isFirst());
		assertTrue(root.down().rightMost().isLast());
		assertTrue(root.down().rightMost().isEnd());
		assertTrue(root.down().right().down().isLeaf());
		
		assertFalse(root.isEnd());
		assertFalse(root.down().right().down().isLast());
	}
	
	@Test
	public void testAddChildren() {
		Loc<Node> b1 = root.next().rightMost();
		Loc<Node> b11 = b1.add(new Node("d1", true));
		Loc<Node> b12 = b1.add(new Node("e1", true));
		
		assertEquals(0, b1.node().getChildren().size());
		
		assertEquals(1, b11.node().getChildren().size());
		assertEquals(1, b12.node().getChildren().size());
		assertEquals("d1", nodeName(b11.next()));
		assertEquals("e1", nodeName(b12.next()));
	}
	
	@Test
	public void testRemoveChildren() {
		Loc<Node> a1 = root.next();
		Loc<Node> rem = a1.clear();
		
		assertEquals(2, a1.node().getChildren().size());
		assertEquals(0, rem.node().getChildren().size());
	}
	
	@Test
	public void testInsert() {
		Loc<Node> a2 = root.next().right();

		Loc<Node> insL = a2.insertLeft(new Node("l1"), new Node("l2"));
		assertEquals(3, root.node().getChildren().size());
		assertEquals(5, insL.root().node().getChildren().size());
		assertEquals("l2", nodeName(insL.left()));
		assertEquals("l1", nodeName(insL.left().left()));
		
		Loc<Node> insR = a2.insertRight(new Node("r1"), new Node("r2"));
		assertEquals(3, root.node().getChildren().size());
		assertEquals(5, insR.root().node().getChildren().size());
		assertEquals("r1", nodeName(insR.right()));
		assertEquals("r2", nodeName(insR.right().right()));
		
	}
	
	@Test
	public void testRemoveLeftRight() {
		Loc<Node> a2 = root.next().right();
		Loc<Node> remL = a2.removeLeft();
		
		assertEquals(3, root.node().getChildren().size());
		assertEquals(2, remL.node().getChildren().size());
		assertEquals("a2", nodeName(remL));
		assertTrue(remL.isFirst());
		assertFalse(remL.isLast());
		
		Loc<Node> remR = a2.removeRight();
		assertEquals(3, root.node().getChildren().size());
		assertEquals(2, remR.node().getChildren().size());
		assertEquals("a2", nodeName(remR));
		assertTrue(remR.isLast());
		assertFalse(remR.isFirst());
		
	}
	
	@Test
	public void testPath() {
		Loc.Path[] path = new Loc.Path[] {
				Loc.Path.DOWN,
				Loc.Path.RIGHT,
				Loc.Path.DOWN
		};
		Loc<Node> c1 = root.location(path);
		assertEquals("c1", nodeName(c1));
		
		Loc.Path[] rpath = c1.path();
		assertEquals(path.length, rpath.length);
		for (int i = 0; i < path.length; i++) {
			assertEquals(path[i], rpath[i]);
		}
	}
	
	/**
	 * @param loc
	 * @return name for the param location
	 */
	private String nodeName(final Loc<Node> loc) {
		return loc.node()._node().getName();
	}
	
	/**
	 * Debug print tree method
	 * @param node
	 * @return tree string
	 */
	public static String printTree(IZipNode node) {
		StringBuffer buf = new StringBuffer();
		buf.append(node.toString());
		if (node.getChildren() != null && !node.getChildren().isEmpty()) {
			buf.append(":[");
			for (IZipNode n : node.getChildren()) {
				buf.append(printTree(n));
				buf.append(", ");
			}
			buf.delete(buf.length() - 2, buf.length());
			buf.append("]");
		}
		return buf.toString();
	}
		
	/**
	 * Test IZipNode class
	 */
	class Node implements IZipNode {

		private String name;
		private final List<IZipNode> children;

		public Node(final String name) {
			this(name, (IZipNode[])null);
		}
		
		public Node(final String name, final boolean leaf) {
			this(name, (leaf)? (IZipNode[])null : new IZipNode[0]);
		}
		
		public Node(final String name, final IZipNode... children) {
			super();
			assert(name != null);
			this.name = name;
			this.children = (children == null)? null : new LinkedList<IZipNode>(Arrays.asList(children));
		}
		
		public String getName() {
			return name;
		}

		public Collection<IZipNode> getChildren() {
			return this.children;
		}

		@Override
		public String toString() {
			return name;
		}
		
	}
	
}

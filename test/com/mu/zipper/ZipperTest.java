package com.mu.zipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
		
		assertEquals("a1", nodeName(root.down(0)));
		assertEquals("a2", nodeName(root.down(1)));
		assertEquals("a3", nodeName(root.down(2)));
		
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
		Loc<Node> rem1 = a1.clear();
		
		assertEquals(2, a1.node().getChildren().size());
		assertEquals(0, rem1.node().getChildren().size());
		
		Loc<Node> a2 = root.down(1);
		Loc<Node> rem2 = a2.removeChild(1);
		assertEquals(2, a2.node().getChildren().size());
		assertEquals(1, rem2.node().getChildren().size());
		assertEquals("c1", nodeName(rem2.next()));
	}
	
	@Test
	public void testChildrenIterator() {
		Loc<Node> r = root.next().right().root();
		Iterator<Node> iter = r.childrenIterator();
		assertEquals("a1", iter.next().getName());
		assertEquals("a2", iter.next().getName());
		assertEquals("a3", iter.next().getName());
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void testInsert() {
		Loc<Node> a2 = root.down(1);

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
	public void testRemove() {
		Loc<Node> c1 = root.down(1).down();
		Loc<Node> rem1 = c1.remove();
		
		assertEquals("a2", nodeName(rem1));
		assertEquals(2, c1.up().node().getChildren().size());
		assertEquals(1, rem1.node().getChildren().size());
		
		Loc<Node> rem2 = rem1.remove();
		
		assertEquals("root", nodeName(rem2));
		assertEquals(3, c1.up().up().node().getChildren().size());
		assertEquals(2, rem2.node().getChildren().size());
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
		
		Loc<Node> c2 = root.location(path);
		Collection<ZipNode<Node>> nodePath = c2.nodePath();
		assertEquals(3, nodePath.size());
		Iterator<ZipNode<Node>> iter = nodePath.iterator();
		ZipNode<Node> next = iter.next();
		assertEquals("root", next.toString());
		next = iter.next();
		assertEquals("a2", next.toString());
		next = iter.next();
		assertEquals("c1", next.toString());
		
	}
	
	@Test
	public void testReplace() {
		Loc<Node> a2 = root.next().right();
		
		Node repWith1 = new Node("r1", new Node("r11"), new Node("r12"));
		Loc<Node> rep1 = a2.replace(repWith1);
		
		assertEquals(3, rep1.root().node().getChildren().size());
		assertEquals("r1", nodeName(rep1));
		assertEquals(2, rep1.node().getChildren().size());
		assertEquals("r11", nodeName(rep1.down()));
		assertEquals("r12", nodeName(rep1.down().next()));
		
		Loc<Node> repWith2 = root.next();
		Loc<Node> rep2 = a2.replace(repWith2.node());
		assertEquals("a1", nodeName(rep2));
		assertEquals(2, rep2.node().getChildren().size());
		assertEquals("b1", nodeName(rep2.down()));
		assertEquals("b2", nodeName(rep2.down().next()));
	}
	
	@Test
	public void testUnzip() {
		Loc<Node> a2 = root.next().right();
		Loc<Node> insL = a2.insertLeft(new Node("l1", false));
		Loc<Node> add = insL.left().add(new Node("d1"), new Node("d2"));
		
		Node u = Zipper.<Node>unzip(add.root());
		assertEquals(4, u.getChildren().size());
		Iterator<Node> l1i = u.getChildren().iterator();
		l1i.next();
		Node l1 = l1i.next();
		assertEquals("l1", l1.getName());
		assertEquals(2, l1.getChildren().size());
		Iterator<Node> di = l1.getChildren().iterator();
		assertEquals("d1", di.next().getName());
		assertEquals("d2", di.next().getName());
	}
	
	/**
	 * @param loc
	 * @return name for the param location
	 */
	private String nodeName(final Loc<Node> loc) {
		return loc._source().getName();
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
		private final List<Node> children;

		public Node(final String name) {
			this(name, (Node[])null);
		}
		
		public Node(final String name, final boolean leaf) {
			this(name, (leaf)? (Node[])null : new Node[0]);
		}
		
		public Node(final String name, final Node... children) {
			super();
			assert(name != null);
			this.name = name;
			this.children = (children == null)? null : new ArrayList<Node>(Arrays.asList(children));
		}
		
		public String getName() {
			return name;
		}

		public Collection<Node> getChildren() {
			return this.children;
		}

		@Override
		public String toString() {
			return name;
		}
		
	}
	
}

package com.mu.zipper.examples.getting_started;

import com.mu.zipper.IZipNode;
import com.mu.zipper.Loc;
import com.mu.zipper.Zipper;

/**
 * Getting started example
 * see README.markdown
 * 
 * @author Adam Smyczek
 */
public class Main {
	
	/**
	 * A sample tree
	 * <pre>
	 *       root
	 *        /\
	 *       /  \
	 *      a    d
	 *     / \    \
	 *    /   \    \
	 *   b     c    e
	 * </pre>
	 * @return a sample tree
	 */
	public final static Node createTree() {
		return new Node("root", 
				new Node("a", 
					new Leaf("b"),
					new Leaf("c")),
				new Node("d",
			        new Leaf("e")));
	}
		
	/**
	 * Main method performs sample tree modification
	 * and prints the results to stdout.
	 * 
	 * @param args not used
	 */
	public static final void main(final String[] args) {
		// Create and zip a tree
		Node root = createTree();
		Loc<Node> loc = Zipper.zip(root);
		
		// Move to node 'e';
		Loc<Node> e = loc.down().right().down();
		
		// Add a sibling on the right
		Loc<Node> f = e.insertRight(new Leaf("f"));
		
		// Move up and add a sibling on the left
		Loc<Node> g = f.up().insertLeft(new Leaf("g"));
		
		// Print trees
		System.out.println("Tree e:");
		System.out.println(printTree(e));
		
		System.out.println("Tree f:");
		System.out.println(printTree(f));
		
		System.out.println("Tree g:");
		System.out.println(printTree(g));
	}
	
	/**
	 * Print tree from root
	 * @param loc
	 * @return tree rendered to string
	 */
	private static String printTree(final Loc<Node> loc) {
		StringBuffer buffer = new StringBuffer();
		printToBuffer(loc.root().node(), buffer);
		return buffer.toString();
	}
	
	/**
	 * Recursive print node to buffer
	 * @param node
	 * @param buffer
	 */
	private static void printToBuffer(final IZipNode node, final StringBuffer buffer) {
		buffer.append(node.toString());
		if ((node.getChildren() != null)) {
			buffer.append(":[");
			for (IZipNode n : node.getChildren()) {
				printToBuffer(n, buffer);
				buffer.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
			buffer.append("]");
		}
	}
	
}

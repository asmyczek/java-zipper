package com.mu.zipper;

import java.util.Arrays;
import java.util.Collection;

/**
 * Internal Zipper node, a wrapper around a tree IZipNode.
 * All changes to a Zipper node are not reflected
 * in the source tree node, but stored in this wrapper.
 * This allow every Zipper locations to maintain local
 * tree changes not visible to other locations. 
 * Use <tt>_source()</tt> to get the original node, 
 * or <tt>Zipper.unizp()</tt> to retrieve the tree
 * for the current location.
 * 
 * @author Adam Smyczek
 *
 * @param <T> concrete IZipNode type
 */
public final class ZipNode<T extends IZipNode> implements IZipNode {

	// Marks children array as not initialized (default)
	private static final IZipNode[] NOT_INITIALIZED = new IZipNode[0];
	
	// The wrapped node
	private final T node;
	
	// Lazy initialized children list
	private IZipNode[] children;
	
	/**
	 * Default constructor,
	 * the children list is marked as not initialized.
	 * 
	 * @param node wrapped node
	 */
	protected ZipNode(final T node) {
		this(node, NOT_INITIALIZED);
	}
	
	/**
	 * @param node wrapped node
	 * @param children list
	 */
	protected ZipNode(final T node, final IZipNode[] children) {
		super();
		
		if (node == null) throw new IllegalArgumentException("Node is null!");
		assert(!(node instanceof ZipNode<?>));
		
		this.node = node;
		this.children = children;
	}
	
	/**
	 * Returns the wrapped node.
	 * Caution, all changes to this node
	 * are reflected in all locations of the
	 * tree. See <tt>Loc#_source()</tt> or
	 * <tt>Loc#replaceNode()</tt> for details.
	 * 
	 * @return the wrapped node
	 */
	public T _source() {
		return node;
	}
	
	/**
	 * @return true if this node is a leaf node
	 */
	public boolean isLeaf() {
		init();
		return children == null;
	}
	
	/**
	 * @return true if this node has children
	 */
	public boolean hasChildren() {
		init();
		return children != null && children.length > 0;
	}
	
	/**
	 * @return the children array
	 */
	protected IZipNode[] children() {
		init();
		return children;
	}
	
	/**
	 * Implements <tt>IZipNode#getChildren()</tt> method.
	 */
	public Collection<? extends IZipNode> getChildren() {
		init();
		return (children != null)? Arrays.asList(children) : null;
	}
	
	/**
	 * Replaces the current wrapped node with <tt>node</tt>
	 * and returns a new ZipNode instance.
	 * 
	 * @param node to wrap
	 * @return new ZipNode instance
	 */
	protected ZipNode<T> replaceNode(final T node) {
		init();
		return new ZipNode<T>(node, children);
	}
	
	/**
	 * Initializes the children array if not initialized yet.
	 */
	private void init() {
		if (children == NOT_INITIALIZED) {
			Collection<? extends IZipNode> ch = node.getChildren();
			children = (ch == null)? null : ch.toArray(new IZipNode[0]);
		}
	}

	@Override
	public String toString() {
		return node.toString();
	}

}

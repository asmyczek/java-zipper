package com.mu.zipper;

import java.util.Arrays;
import java.util.Collection;

public final class ZipNode<T extends IZipNode> implements IZipNode {

	private final T node;
	
	private final IZipNode[] children;
	
	protected ZipNode(final T node, final Collection<? extends IZipNode> children) {
		this(node, (children == null)? null : children.toArray(new IZipNode[0]));
	}
	
	protected ZipNode(final T node, final IZipNode[] children) {
		super();
		
		if (node == null) throw new IllegalArgumentException("Node is null!");
		assert(!(node instanceof ZipNode<?>));
		
		this.node = node;
		this.children = children;
	}
	
	public T _node() {
		return node;
	}
	
	public boolean isLeaf() {
		return children == null;
	}
	
	public Collection<IZipNode> getChildren() {
		return Arrays.asList(children);
	}
	
	public boolean hasChildren() {
		return children != null && children.length > 0;
	}
	
	protected IZipNode[] children() {
		return children;
	}
	
	protected ZipNode<T> replaceNode(final T node) {
		return new ZipNode<T>(node, children);
	}
	
	@Override
	public String toString() {
		return node.toString();
	}

}

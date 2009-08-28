package com.mu.zipper;

import java.util.Arrays;
import java.util.Collection;

public final class ZipNode<T extends IZipNode> implements IZipNode {

	private static final IZipNode[] NOT_INITIALIZED = new IZipNode[0];
	
	private final T node;
	
	private IZipNode[] children;
	
	protected ZipNode(final T node) {
		this(node, NOT_INITIALIZED);
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
		if (children == NOT_INITIALIZED) init();
		return children == null;
	}
	
	public Collection<IZipNode> getChildren() {
		if (children == NOT_INITIALIZED) init();
		return Arrays.asList(children);
	}
	
	public boolean hasChildren() {
		if (children == NOT_INITIALIZED) init();
		return children != null && children.length > 0;
	}
	
	protected IZipNode[] children() {
		if (children == NOT_INITIALIZED) init();
		return children;
	}
	
	protected ZipNode<T> replaceNode(final T node) {
		if (children == NOT_INITIALIZED) init();
		return new ZipNode<T>(node, children);
	}
	
	private void init() {
		if (children == NOT_INITIALIZED) {
			children = (node.getChildren() == null)? null : node.getChildren().toArray(new IZipNode[0]);
		}
	}

	@Override
	public String toString() {
		return node.toString();
	}

}

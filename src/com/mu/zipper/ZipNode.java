package com.mu.zipper;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public final class ZipNode<T extends IZipNode> implements IZipNode {

	private final T node;
	
	private final Collection<IZipNode> children;
	
	protected ZipNode(final T node, final Collection<? extends IZipNode> children) {
		super();
		
		if (node == null) throw new IllegalArgumentException("Node is null!");
		assert(!(node instanceof ZipNode<?>));
		
		this.node = node;
		this.children = newList(children);
	}
	
	public T _node() {
		return node;
	}
	
	public boolean isLeaf() {
		return children == null;
	}
	
	public Collection<IZipNode> getChildren() {
		return children;
	}
	
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}
	
	protected ZipNode<T> replaceNode(final T node) {
		return new ZipNode<T>(node, newList(children));
	}
	
	private Collection<IZipNode> newList(final Collection<? extends IZipNode> children) {
		return (children == null)? null : 
			Collections.unmodifiableCollection(new LinkedList<IZipNode>(children));
	}

	@Override
	public String toString() {
		return node.toString();
	}

}

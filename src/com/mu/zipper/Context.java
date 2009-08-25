package com.mu.zipper;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class Context {

	protected static final Context TOP = new Context((ZipNode<?>)null, null, null);
	
	private final Collection<IZipNode> left;
	
	private final Collection<IZipNode> right;
	
	private final Context parentContext;
	
	private final ZipNode<?> parentNode;
	
	protected Context(
			final ZipNode<?> parentNode, 
			final Context parentContext, 
			final Collection<IZipNode> children) {
		this(parentNode, 
				parentContext, 
				new LinkedList<IZipNode>(), 
				(children == null)? new LinkedList<IZipNode>() : 
					new LinkedList<IZipNode>(children));
	}
	
	protected Context(
			final ZipNode<?> parentNode, 
			final Context parentContext, 
			final LinkedList<IZipNode> left, 
			final LinkedList<IZipNode> right) {
		super();
		this.parentNode = parentNode;
		this.parentContext = parentContext;
		this.left = Collections.unmodifiableList(left);
		this.right = Collections.unmodifiableList(right);
	}
	
	protected boolean isTop() {
		return parentNode == null;
	}
	
	protected boolean isFirst() {
		return left.isEmpty();
	}
	
	protected boolean isLast() {
		return right.isEmpty();
	}
	
	protected Collection<IZipNode> leftNodes() {
		return left;
	}
	
	protected Collection<IZipNode> rightNodes() {
		return right;
	}

	protected Context getParentContext() {
		return parentContext;
	}

	protected ZipNode<?> getParentNode() {
		return parentNode;
	}
	
	protected Context copy() {
		LinkedList<IZipNode> left = new LinkedList<IZipNode>(leftNodes());
		LinkedList<IZipNode> right = new LinkedList<IZipNode>(rightNodes());
		return new Context(getParentNode(), getParentContext(), left, right);
	}

}

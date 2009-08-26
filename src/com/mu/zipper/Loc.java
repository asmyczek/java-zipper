package com.mu.zipper;

import java.util.Collection;
import java.util.LinkedList;

public final class Loc<T extends IZipNode> {

	public enum Path {
		DOWN, UP, LEFT, RIGHT,
		LEFT_MOST, RIGHT_MOST, NEXT;
	}
	
	private final Context context;

	private ZipNode<T> node;
	
	protected Loc(final ZipNode<T> node, final Context context) {
		super();
		this.node = node;
		this.context = context;
	}
	
	public ZipNode<T> node() {
		return node;
	}
	
	// **** Orientation in zipper ****
	
	public boolean isTop() {
		return context.isTop();
	}
	
	public boolean isFirst() {
		return context.isFirst();
	}
	
	public boolean isLast() {
		return context.isLast();
	}
	
	/**
	 * @return true if this location is the last node
	 * in the deep first traversal order
	 */
	public boolean isEnd() {
		if (hasChildren() || !isLast()) {
			return false;
		} else {
			Loc<T> up = this;
			while (up.isLast() && !up.isTop()) {
				up = up.up();
			}
			return up.isTop();
		}
	}
	
	public boolean hasChildren() {
		return node.hasChildren();
	}
	
	public boolean isLeaf() {
		return node.isLeaf();
	}
	
	// **** Traversing the zipper ****
	
	public Loc<T> down() {
		if (hasChildren()) {
			int length = node.children().length;
			IZipNode[] r = new IZipNode[length - 1];
        	System.arraycopy(node.children(), 1, r, 0, length - 1);
			return new Loc<T>(toZipNode(node.children()[0]), new Context(node, context, new IZipNode[0], r));
		}
		throw new ZipperException("Current node does not have any children!");
	}
	
	@SuppressWarnings("unchecked")
	public Loc<T> up() {
		if (!isTop()) {
			IZipNode[] ch = new IZipNode[1+
			                             context.leftNodes().length +
			                             context.rightNodes().length];
			System.arraycopy(context.leftNodes(), 0, ch, 0, context.leftNodes().length);
			ch[context.leftNodes().length] = node;
			System.arraycopy(context.rightNodes(), 0, ch, context.leftNodes().length + 1, context.rightNodes().length);
			return new Loc<T>(new ZipNode<T>((T)context.getParentNode()._node(), ch), context.getParentContext());
		}
		throw new ZipperException("Current node is already the top node!");
	}
	
	
	public Loc<T> right() {
		if (!isLast()) {
			IZipNode[] left = new IZipNode[context.leftNodes().length + 1];
        	System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
        	left[left.length-1] = node;
        	
			IZipNode[] right = new IZipNode[context.rightNodes().length - 1];
        	System.arraycopy(context.rightNodes(), 1, right, 0, context.rightNodes().length - 1);
        	
			Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
			return new Loc<T>(toZipNode(context.rightNodes()[0]), ctx);
		}
		throw new ZipperException("Current node is already the the most right node!");
	}
	
	
	public Loc<T> left() {
		if (!isFirst()) {
			IZipNode[] left = new IZipNode[context.leftNodes().length - 1];
        	System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length - 1);
        	
        	IZipNode[] right = new IZipNode[context.rightNodes().length + 1];
        	System.arraycopy(context.rightNodes(), 0, right, 1, context.rightNodes().length);
        	right[0] = node;
			Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
			return new Loc<T>(toZipNode(context.leftNodes()[left.length]), ctx);
		}
		throw new ZipperException("Current node is already the the most left node!");
	}
	
	public Loc<T> rightMost() {
		Loc<T> l = this;
		while (!l.isLast()) {
			l = l.right();
		}
		return l;
	}
	
	public Loc<T> leftMost() {
		Loc<T> l = this;
		while (!l.isFirst()) {
			l = l.left();
		}
		return l;
	}	
	
	public Loc<T> root() {
		Loc<T> l = this;
		while (!l.isTop()) {
			l = l.up();
		}
		return l;
	}
	
	public Loc<T> next() {
		if (hasChildren()) {
			return down();
		} else if (!isLast()) {
			return right();
		} else {
			Loc<T> up = this;
			while (up.isLast() && !up.isTop()) {
				up = up.up();
			}
			if (!up.isLast()) {
				return up.right();
			} else {
        		throw new ZipperException("Current node is a top node.");
			}
		}
	}
	
	// **** Mutations ****
	
	@SuppressWarnings("unchecked")
	public Loc<T> addAll(final Collection<T> nodes) {
		return add((T[]) nodes.toArray());
	}
	
	public Loc<T> add(final T... nodes) {
		if (!isLeaf()) {
			IZipNode[] ch = new IZipNode[nodes.length + node.children().length];
			System.arraycopy(node.children(), 0, ch, 0, node.children().length);
			System.arraycopy(nodes, 0, ch, node.children().length, nodes.length);
    		return new Loc<T>(new ZipNode<T>(node._node(), ch), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	public Loc<T> clear() {
		if (!isLeaf()) {
    		return new Loc<T>(new ZipNode<T>(node._node(), new IZipNode[0]), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	public Loc<T> insertLeft(T... nodes) {
		IZipNode[] left = new IZipNode[context.leftNodes().length + nodes.length];
		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
		System.arraycopy(nodes, 0, left, context.leftNodes().length, nodes.length);
		
		IZipNode[] right = new IZipNode[context.rightNodes().length];
		System.arraycopy(context.rightNodes(), 0, right, 0, context.rightNodes().length);
		
		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
		return new Loc<T>(node, ctx);
	}
	
	public Loc<T> insertRight(T... nodes) {
		IZipNode[] left = new IZipNode[context.leftNodes().length];
		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
		
		IZipNode[] right = new IZipNode[context.rightNodes().length + nodes.length];
		System.arraycopy(nodes, 0, right, 0, nodes.length);
		System.arraycopy(context.rightNodes(), 0, right, nodes.length, context.rightNodes().length);
		
		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
		return new Loc<T>(node, ctx);
	}
	
	public Loc<T> removeLeft() {
		if (!isFirst()) {
    		IZipNode[] left = new IZipNode[context.leftNodes().length - 1];
    		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length - 1);
			
    		IZipNode[] right = new IZipNode[context.rightNodes().length];
    		System.arraycopy(context.rightNodes(), 0, right, 0, context.rightNodes().length);
			
    		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
    		return new Loc<T>(node, ctx);
		}
		throw new ZipperException("Current node is the most left node!");
	}
	
	public Loc<T> removeRight() {
		if (!isFirst()) {
    		IZipNode[] left = new IZipNode[context.leftNodes().length];
    		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
			
    		IZipNode[] right = new IZipNode[context.rightNodes().length - 1];
    		System.arraycopy(context.rightNodes(), 1, right, 0, context.rightNodes().length - 1);
    		
    		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
    		return new Loc<T>(node, ctx);
		}
		throw new ZipperException("Current node is the most right node!");
	}
	
	public Loc<T> replace(IZipNode node) {
		return new Loc<T>(toZipNode(node), context.copy());
	}
	
	public Loc<T> replaceNode(T node) {
		if (node instanceof ZipNode<?>) {
			throw new IllegalArgumentException("ZipNode not supported!");
		}
		return new Loc<T>(this.node.replaceNode(node), context.copy());
	}
	
	// **** Path ****
	
	public Path[] path() {
		LinkedList<Path> path = new LinkedList<Path>();
		Loc<T> l = this;
		while (!l.isTop()) {
			if (!l.isFirst()) {
				path.addFirst(Path.RIGHT);
				l = l.left();
			} else {
				path.addFirst(Path.DOWN);
				l = l.up();
			}
		}
		return path.toArray(new Path[path.size()]);
	}
	
	public Loc<T> location(final Path... path) {
		Loc<T> l = this.root();
		for (Path p : path) {
			switch (p) {
			case DOWN:       l = l.down(); break;
			case LEFT:       l = l.left(); break;
			case RIGHT:      l = l.right(); break;
			case UP:         l = l.up(); break;
			case LEFT_MOST:  l = l.leftMost(); break;
			case RIGHT_MOST: l = l.rightMost(); break;
			case NEXT:       l = l.next(); break;
			}
		}
		return l;
	}
	
	@SuppressWarnings("unchecked")
	private ZipNode<T> toZipNode(final IZipNode node) {
		if (node instanceof ZipNode<?>) {
			return (ZipNode<T>) node;
		} else {
			return new ZipNode<T>((T)node, node.getChildren());
		}
	}
	
}

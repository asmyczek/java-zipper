package com.mu.zipper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
			LinkedList<IZipNode> children = new LinkedList<IZipNode>(node.getChildren());
			IZipNode first = children.removeFirst();
			return new Loc<T>(toZipNode(first), new Context(node, context, children));
		}
		throw new ZipperException("Current node does not have any children!");
	}
	
	@SuppressWarnings("unchecked")
	public Loc<T> up() {
		if (!isTop()) {
			LinkedList<IZipNode> children = new LinkedList<IZipNode>();
			children.addAll(context.leftNodes());
			children.add(node);
			children.addAll(context.rightNodes());
			return new Loc<T>(new ZipNode<T>((T)context.getParentNode()._node(), children), context.getParentContext());
		}
		throw new ZipperException("Current node is already the top node!");
	}
	
	
	public Loc<T> right() {
		if (!isLast()) {
			LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
			left.addLast(node);
			LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
			Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
			return new Loc<T>(toZipNode(right.removeFirst()), ctx);
		}
		throw new ZipperException("Current node is already the the most right node!");
	}
	
	
	public Loc<T> left() {
		if (!isFirst()) {
			LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
			LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
			right.addFirst(node);
			Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
			return new Loc<T>(toZipNode(left.removeLast()), ctx);
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
	
	public Loc<T> add(final T... nodes) {
		return addAll(Arrays.<T>asList(nodes));
	}
	
	public Loc<T> addAll(final Collection<T> nodes) {
		if (!isLeaf()) {
    		LinkedList<IZipNode> children = new LinkedList<IZipNode>(node.getChildren());
    		children.addAll(nodes);
    		return new Loc<T>(new ZipNode<T>(node._node(), children), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	public Loc<T> clear() {
		if (!isLeaf()) {
    		return new Loc<T>(new ZipNode<T>(node._node(), Collections.<IZipNode>emptyList()), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	public Loc<T> insertLeft(T... nodes) {
		LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
		LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
		for (T n : nodes) {
			left.addLast(n);
		}
		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
		return new Loc<T>(node, ctx);
	}
	
	public Loc<T> insertRight(T... nodes) {
		LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
		LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
		for (int i = nodes.length - 1; i >= 0; i--) {
			right.addFirst(nodes[i]);
		}
		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
		return new Loc<T>(node, ctx);
	}
	
	public Loc<T> removeLeft() {
		if (!isFirst()) {
    		LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
    		LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
    		left.removeLast();
    		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
    		return new Loc<T>(node, ctx);
		}
		throw new ZipperException("Current node is the most left node!");
	}
	
	public Loc<T> removeRight() {
		if (!isFirst()) {
    		LinkedList<IZipNode> left = new LinkedList<IZipNode>(context.leftNodes());
    		LinkedList<IZipNode> right = new LinkedList<IZipNode>(context.rightNodes());
    		right.removeFirst();
    		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
    		return new Loc<T>(node, ctx);
		}
		throw new ZipperException("Current node is the most right node!");
	}
	
	@SuppressWarnings("unchecked")
	public Loc<T> replace(IZipNode node) {
		ZipNode<T> zipNode = null;
		if (node instanceof ZipNode<?>) {
			zipNode = (ZipNode<T>)node;
		} else {
			zipNode = new ZipNode(node, node.getChildren());
		}
		return new Loc<T>(zipNode, context.copy());
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

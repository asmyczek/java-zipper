package com.mu.zipper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
		return down(0);
	}
	
	public Loc<T> down(int index) {
		if (hasChildren() && index < node.children().length) {
			IZipNode[] left = new IZipNode[index];
			System.arraycopy(node.children(), 0, left, 0, left.length);
			IZipNode[] right = new IZipNode[node.children().length - index - 1];
			System.arraycopy(node.children(), index + 1, right, 0, right.length);
			return new Loc<T>(toZipNode(node.children()[index]), new Context(node, context, left, right));
		}
		throw new ZipperException("Current node does not have any children or index out of bound!");
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
	
	public Loc<T> removeChild(int index) {
		if (hasChildren() && index < node.getChildren().size()) {
			IZipNode[] ch = new IZipNode[node.children().length - 1];
			System.arraycopy(node.children(), 0, ch, 0, index);
			System.arraycopy(node.children(), index + 1, ch, index, node.children().length - index - 1);
    		return new Loc<T>(new ZipNode<T>(node._node(), ch), context);
		}
		throw new ZipperException("Current node does not have any children or index out of bound!");
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
	
	/**
	 * Remove current node, returns parent location
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Loc<T> remove() {
		if (!isTop()) {
			IZipNode[] ch = new IZipNode[context.leftNodes().length +
			                             context.rightNodes().length];
			System.arraycopy(context.leftNodes(), 0, ch, 0, context.leftNodes().length);
			System.arraycopy(context.rightNodes(), 0, ch, context.leftNodes().length, context.rightNodes().length);
			return new Loc<T>(new ZipNode<T>((T)context.getParentNode()._node(), ch), context.getParentContext());
		}
		throw new ZipperException("Current node is already the top node!");
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
	
	public Collection<ZipNode<T>> nodePath() {
		List<ZipNode<T>> path = new ArrayList<ZipNode<T>>();
		Loc<T> l = this;
		while (!l.isTop()) {
			path.add(0, l.node());
			l = l.up();
		}
		path.add(0, l.node());
		return Collections.unmodifiableList(path);
	}
	
	// ****
	
	@SuppressWarnings("unchecked")
	private ZipNode<T> toZipNode(final IZipNode node) {
		if (node instanceof ZipNode<?>) {
			return (ZipNode<T>) node;
		} else {
			return new ZipNode<T>((T)node, node.getChildren());
		}
	}
	
}

package com.mu.zipper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A <tt>Loc</tt> object represents a location inside the
 * Zipper data structure. A location references the
 * node in focus and a context describing the position
 * of the node inside the tree. 
 * A Zipper is traversed by moving from one location to
 * another using the basic <tt>down()</tt>, <tt>left()</tt>, 
 * <tt>right()</tt> and <tt>up()</tt> methods. Every move 
 * operation returns a new location instance. 
 * Changes to the tree are possible only at the the
 * node in currently focused location.
 * 
 * A location can be seen as a view into the tree.
 * Tree changes in one location are not visible to other
 * locations. See getting started example for details.
 * 
 * @author Adam Smyczek
 *
 * @param <T> the concrete node type
 */
public final class Loc<T extends IZipNode> {

	// The node in focus
	private final ZipNode<T> node;
	
	// The context of this location
	private final Context context;

	/**
	 * Default constructor
	 * @param node in focus
	 * @param context location context
	 */
	protected Loc(final ZipNode<T> node, final Context context) {
		super();
		this.node = node;
		this.context = context;
	}
	
	/**
	 * @return ZipNode for this location
	 */
	public ZipNode<T> node() {
		return node;
	}
	
	/**
	 * Returns the underlying source node for this location.
	 * Caution, all changes to this node are reflected
	 * in all locations of the tree. If this is not
	 * intended, use <tt>replace()</tt> or <tt>replaceNode()</tt>.
	 * 
	 * @return the source node
	 */
	public T _source() {
		return node._source();
	}
	
	// ---- Location predicates ----
	
	/** 
	 * @return true if this location marks the root node
	 */
	public boolean isTop() {
		return context.isTop();
	}
	
	/**
	 * @return true if this location marks the most left sibling node
	 */
	public boolean isFirst() {
		return context.isFirst();
	}
	
	/**
	 * @return true if this location marks the most right sibling node
	 */
	public boolean isLast() {
		return context.isLast();
	}
	
	/**
	 * Marks the last node in a deep-first traversal order.
	 * Use this method in combination with <tt>next()</tt>.
	 * 
	 * @return true if this location is the last node
	 * in a deep-first traversal order
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
	
	// ---- Children accessors ----
	
	/**
	 * @return if this location marks a leaf node
	 */
	public boolean isLeaf() {
		return node.isLeaf();
	}
	
	/**
	 * @return if the node contains children, false
	 * if the node is a leaf node or has no children.
	 */
	public boolean hasChildren() {
		return node.hasChildren();
	}
	
	/**
	 * An iterator over all children nodes.
	 * This iterator always returns the underlying
	 * source nodes. In difference to it,
	 * <tt>node().getChildren()</tt> returns a mixed
	 * collection of T and ZipNodes, depending on if a
	 * node was already traversed by the Zipper or not.
	 * 
	 * @return an iterator over all children nodes of the current location
	 * @throws ZipperException if this node is a leaf node
	 */
	public Iterator<T> childrenIterator() {
		if (!isLeaf()) {
    		final Iterator<? extends IZipNode> iter = node.getChildren().iterator();
    		return new Iterator<T>() {
    
    			public boolean hasNext() {
    				return iter.hasNext();
    			}
    
    			@SuppressWarnings("unchecked")
				public T next() {
    				IZipNode node = iter.next();
    				return (node instanceof ZipNode<?>)? ((ZipNode<T>)node)._source() : (T)node;
    			}
    
    			public void remove() {
    				throw new UnsupportedOperationException("Use Loc remove methods to remove children.");
    			}
    			
    		};
		}
		throw new ZipperException("This node is a leaf node!");
	}
	
	// **** Traversing the zipper ****
	
	/**
	 * Move down to the first/most left child node
	 * 
	 * @return the new location
	 */
	public Loc<T> down() {
		return down(0);
	}
	
	/**
	 * Move down to the n-th node.
	 * 
	 * @param index of the n-th node
	 * @return the new location
	 * @throws ZipperException if this node is a leaf node or index out of bound
	 */
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
	
	/**
	 * Move up to the parent node.
	 * 
	 * @return new location
	 * @throws ZipperException if this node is already a root node
	 */
	@SuppressWarnings("unchecked")
	public Loc<T> up() {
		if (!isTop()) {
			IZipNode[] ch = new IZipNode[1+
			                             context.leftNodes().length +
			                             context.rightNodes().length];
			System.arraycopy(context.leftNodes(), 0, ch, 0, context.leftNodes().length);
			ch[context.leftNodes().length] = node;
			System.arraycopy(context.rightNodes(), 0, ch, context.leftNodes().length + 1, context.rightNodes().length);
			return new Loc<T>(new ZipNode<T>((T)context.getParentNode()._source(), ch), context.getParentContext());
		}
		throw new ZipperException("Current node is already the top node!");
	}
	
	
	/**
	 * Move to the next sibling node.
	 * 
	 * @return new location
	 * @throws ZipperException if this node is the most right sibling node already
	 */
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
	
	
	/**
	 * Move to the previous sibling node.
	 * 
	 * @return new location
	 * @throws ZipperException if this location is the most left sibling node already
	 */
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
	
	/**
	 * Move to the right most sibling node.
	 * 
	 * @return new location
	 */
	public Loc<T> rightMost() {
		Loc<T> l = this;
		while (!l.isLast()) {
			l = l.right();
		}
		return l;
	}
	
	/**
	 * Move to the most left sibling node.
	 * 
	 * @return new location
	 */
	public Loc<T> leftMost() {
		Loc<T> l = this;
		while (!l.isFirst()) {
			l = l.left();
		}
		return l;
	}	
	
	/**
	 * Move up to the root node.
	 * 
	 * @return new location
	 */
	public Loc<T> root() {
		Loc<T> l = this;
		while (!l.isTop()) {
			l = l.up();
		}
		return l;
	}
	
	/**
	 * Move to the next node in a deep-first traversal order.
	 * Use in combination with <tt>isEnd()</tt> predicate.
	 * 
	 * @return new location
	 */
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
	
	// ---- Altering Zipper tree ----
	
	/**
	 * Add <tt>nodes</tt> to the end of the children list.
	 * This method returns a new location instance referencing
	 * the same source node but and the updated children set.
	 * 
	 * @param nodes to append
	 * @return new location instance for the same node, 
	 *         but containing the new children set
	 */
	public Loc<T> add(final T... nodes) {
		if (!isLeaf()) {
			IZipNode[] ch = new IZipNode[nodes.length + node.children().length];
			System.arraycopy(node.children(), 0, ch, 0, node.children().length);
			System.arraycopy(nodes, 0, ch, node.children().length, nodes.length);
    		return new Loc<T>(new ZipNode<T>(_source(), ch), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	/**
	 * Same as <tt>add(T...)</tt>.
	 */
	@SuppressWarnings("unchecked")
	public Loc<T> addAll(final Collection<T> nodes) {
		return add((T[]) nodes.toArray());
	}
	
	/**
	 * Remove child node at position <tt>index</tt>.
	 * 
	 * @param index to remove
	 * @return new location with updated children set
	 * @throws ZipperException if index out of bounds
	 */
	public Loc<T> removeChild(int index) {
		if (hasChildren() && index < node.getChildren().size()) {
			IZipNode[] ch = new IZipNode[node.children().length - 1];
			System.arraycopy(node.children(), 0, ch, 0, index);
			System.arraycopy(node.children(), index + 1, ch, index, node.children().length - index - 1);
    		return new Loc<T>(new ZipNode<T>(_source(), ch), context);
		}
		throw new ZipperException("Current node does not have any children or index out of bound!");
	}
	
	/**
	 * Remove all children.
	 * 
	 * @return new location with updated children set
	 * @throws ZipperException if location marks a leaf node
	 */
	public Loc<T> clear() {
		if (!isLeaf()) {
    		return new Loc<T>(new ZipNode<T>(_source(), new IZipNode[0]), context);
		}
		throw new ZipperException("Current node is a leaf!");
	}
	
	/**
	 * Inserts <tt>nodes</tt> to the left of the current location node.
	 * 
	 * @param nodes to insert
	 * @return new location referencing same node, but updated context
	 */
	public Loc<T> insertLeft(T... nodes) {
		IZipNode[] left = new IZipNode[context.leftNodes().length + nodes.length];
		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
		System.arraycopy(nodes, 0, left, context.leftNodes().length, nodes.length);
		
		IZipNode[] right = new IZipNode[context.rightNodes().length];
		System.arraycopy(context.rightNodes(), 0, right, 0, context.rightNodes().length);
		
		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
		return new Loc<T>(node, ctx);
	}
	
	/**
	 * Inserts <tt>nodes</tt> to the right of the current location node.
	 * 
	 * @param nodes to insert
	 * @return new location referencing same node, but updated context
	 */
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
	 * Remove current node, returns parent location.
	 * 
	 * @return location referencing parent node
	 * @throws ZipperException if location marks the top node
	 */
	@SuppressWarnings("unchecked")
	public Loc<T> remove() {
		if (!isTop()) {
			IZipNode[] ch = new IZipNode[context.leftNodes().length +
			                             context.rightNodes().length];
			System.arraycopy(context.leftNodes(), 0, ch, 0, context.leftNodes().length);
			System.arraycopy(context.rightNodes(), 0, ch, context.leftNodes().length, context.rightNodes().length);
			return new Loc<T>(new ZipNode<T>((T)context.getParentNode()._source(), ch), context.getParentContext());
		}
		throw new ZipperException("Current node is already the top node!");
	}
	
	/**
	 * Removes the sibling on the left.
	 * 
	 * @return new location referencing same node, but updated context
	 * @throws ZipperException if location marks the most left node
	 */
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
	
	/**
	 * Removes next sibling to the right.
	 * 
	 * @return new location referencing same node, but updated context
	 * @throws ZipperException if location marks the most right node
	 */
	public Loc<T> removeRight() {
		if (!isLast()) {
    		IZipNode[] left = new IZipNode[context.leftNodes().length];
    		System.arraycopy(context.leftNodes(), 0, left, 0, context.leftNodes().length);
			
    		IZipNode[] right = new IZipNode[context.rightNodes().length - 1];
    		System.arraycopy(context.rightNodes(), 1, right, 0, context.rightNodes().length - 1);
    		
    		Context ctx = new Context(context.getParentNode(), context.getParentContext(), left, right);
    		return new Loc<T>(node, ctx);
		}
		throw new ZipperException("Current node is the most right node!");
	}
	
	/**
	 * Replaces current location node.
	 * 
	 * @param node
	 * @return new location with updated node
	 */
	public Loc<T> replace(IZipNode node) {
		return new Loc<T>(toZipNode(node), context.copy());
	}
	
	/**
	 * Replaces the source node for the current location.
	 * 
	 * @param node
	 * @return new location with updated source node
	 */
	public Loc<T> replaceSource(T node) {
		if (node instanceof ZipNode<?>) {
			throw new IllegalArgumentException("ZipNode not supported!");
		}
		return new Loc<T>(this.node.replaceNode(node), context.copy());
	}
	
	// ---- Path ----
	
	/**
	 * Path components
	 */
	public enum Path {
		DOWN, UP, LEFT, RIGHT, LEFT_MOST, RIGHT_MOST, NEXT;
	}
	
	/**
	 * Calculates the path from root node to the current location.
	 * 
	 * @return Path array for the current location.
	 */
	// TODO: it's not the most optimal path. Improve this.
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
	
	/**
	 * Traverses Zipper data structure in the order
	 * of path elements and returns the resulting
	 * location.
	 * 
	 * @param path array
	 * @return resulting location
	 * @throws ZipperException in case of an invalid path
	 */
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
	

	/**
	 * @return all ZipNodes at the direct path from root
	 * to this location node.
	 */
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
	
	// ---- Helper functions ----
	
	/**
	 * ZipNode constructor helper
	 * 
	 * @param node
	 * @return same node if <tt>node</tt> is a ZipNode already,
	 * a new ZipNode wrapper otherwise
	 */
	@SuppressWarnings("unchecked")
	private ZipNode<T> toZipNode(final IZipNode node) {
		if (node instanceof ZipNode<?>) {
			return (ZipNode<T>) node;
		} else {
			return new ZipNode<T>((T)node);
		}
	}
	
}

package com.mu.zipper;

import java.util.Collection;

/**
 * Zipper constructor and util functions.
 * 
 * @author Adam Smyczek
 */
public final class Zipper {

	/**
	 * Zips the <tt>node</tt> and returns the Zipper
	 * location for this node.
	 * 
	 * @param <T> concrete IZipNode type
	 * @param node root node of the tree
	 * @return Zipper root location
	 */
	public static <T extends IZipNode> Loc<T> zip(final T node) {
		return new Loc<T>(new ZipNode<T>(node), Context.TOP);
	}
	
	/**
	 * Opposite to zip, <tt>unzip</tt> re-creates the 
	 * tree from the Zipper data structure.
	 * 
	 * @param <T> concrete note type
	 * @param location a location in the tree
	 * @return the unzipped tree
	 */
	public static <T extends IZipNode> T unzip(final Loc<T> location) {
		return Zipper.<T>unzip(location.root().node());
	}
	
	/**
	 * Recursive <tt>unzip</tt> call to all children nodes. The children
	 * of every original source node are replaced with the 
	 * children of the corresponding ZipNode.
	 */
	@SuppressWarnings("unchecked")
	private static <T extends IZipNode> T unzip(final IZipNode node) {
		if (node instanceof ZipNode<?>) {
			ZipNode<T> zipNode = (ZipNode<T>)node;
			T source = zipNode._source();
			if (!zipNode.isLeaf()) {
				Collection<T> ch = (Collection<T>)source.getChildren();
				source.getChildren().clear();
    			for (IZipNode n : zipNode.children()) {
    				ch.add((T)unzip(n));
    			}
			}
			return source;
		} else {
			return (T) node;
		}
	}
	
	/**
	 * Traverses the entire tree and wraps every tree node into
	 * a ZipNode. Usually a call to <tt>Loc#node()#getChildren()</tt>
	 * will return a mixed collection of T and ZipNode nodes, depending
	 * on if a node was already traversed or not. Calling this function 
	 * first will guarantee that the call to <tt>getChildren()</tt>
	 * returns ZipNode objects only. This method is expensive, before
	 * you decide to use it, take a look at <tt>Loc#childrenIterator()</tt>.
	 * 
	 * @param <T>
	 * @param node
	 * @return location to a root node where every tree node
	 *         is a ZipNode
	 */
	public static <T extends IZipNode> Loc<T> unfold(final Loc<T> node) {
		Loc<T> l = node.root();
		while (!l.isEnd()) {
			l = l.next();
		}
		return l.root();
	}
	
}

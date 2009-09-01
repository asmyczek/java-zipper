package com.mu.zipper;

import java.util.Collection;

/**
 * Zipepr constructor
 *
 */
public final class Zipper {

	/**
	 * Create a zipper from a IZipNode tree
	 * @param <T> IZipNode type
	 * @param node
	 * @return zipper root location
	 */
	public static <T extends IZipNode> Loc<T> newZipper(final T node) {
		return new Loc<T>(new ZipNode<T>(node), Context.TOP);
	}

	public static <T extends IZipNode> Loc<T> unfold(final Loc<T> node) {
		Loc<T> l = node.root();
		while (!l.isEnd()) {
			l = l.next();
		}
		return l.root();
	}
	
	/**
	 * @param <T>
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IZipNode> T unzip(final IZipNode node) {
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
	 * @param <T>
	 * @param location
	 * @return
	 */
	public static <T extends IZipNode> T unzip(final Loc<T> location) {
		return Zipper.<T>unzip(location.node());
	}
	
}

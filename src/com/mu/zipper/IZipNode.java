package com.mu.zipper;

import java.util.Collection;

/**
 * The only hook to the Zipper data structure.
 * All nodes handled by the Zipper have 
 * to implement this interface.
 *
 * @author Adam Smyczek
 */
public interface IZipNode {

	/**
	 * If <tt>getChildren()</tt> returns null
	 * Zipper considers this node to be a leaf node.
	 * Return a collection to add/remove child nodes.
	 * 
	 * @return null if child node, a collection (empty or not) otherwise.
	 */
	abstract public Collection<? extends IZipNode> getChildren();
	
}

package com.mu.zipper;

import java.util.Collection;

/**
 * The only hook to the Zipper data structure.
 * All nodes handlen by the Zipper have 
 * to implement this interface.
 *
 * @author asmyczek
 */
public interface IZipNode {

	/**
	 * @return null if this node is a leaf node, otherwise
	 * return a collection, empty or not.
	 */
	abstract public Collection<? extends IZipNode> getChildren();
	
}

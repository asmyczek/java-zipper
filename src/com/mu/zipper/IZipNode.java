package com.mu.zipper;

import java.util.Collection;

/**
 * Base component interface for all zippable classes.
 * @author asmyczek
 */
public interface IZipNode {

	/**
	 * @return a collection if this component is a node
	 * or null if it is a leaf.
	 */
	abstract public Collection<IZipNode> getChildren();
	
}

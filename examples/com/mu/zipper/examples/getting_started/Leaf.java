package com.mu.zipper.examples.getting_started;

import java.util.Collection;

/**
 * A sample leaf node.
 * 
 * @author Adam Smyczek
 */
public class Leaf extends Node {

	public Leaf(final String name) {
		super(name, new Node[0]);
	}
	
	/**
	 * A leaf node returns null.
	 * 
	 * @return null
	 */
	@Override
	public Collection<Node> getChildren() {
		return null;
	}

}

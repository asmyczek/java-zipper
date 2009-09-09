package com.mu.zipper.examples.getting_started;

import java.util.Collection;

public class Leaf extends Node {

	public Leaf(final String name) {
		super(name, new Node[0]);
	}
	
	@Override
	public Collection<Node> getChildren() {
		return null;
	}

}

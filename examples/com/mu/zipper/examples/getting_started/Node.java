package com.mu.zipper.examples.getting_started;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mu.zipper.IZipNode;

public class Node implements IZipNode {

	private String name;
	
	private final List<Node> children;
	
	public Node(final String name, final Node... children) {
		super();
		this.name = name;
		this.children = Arrays.asList(children);
	}
	
	public String getName() {
		return name;
	}

	public Collection<Node> getChildren() {
		return this.children;
	}

	@Override
	public String toString() {
		return name;
	}
	
}

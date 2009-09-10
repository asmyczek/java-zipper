package com.mu.zipper.examples.getting_started;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.mu.zipper.IZipNode;

/**
 * A sample node.
 * 
 * @author Adam Smyczek
 */
public class Node implements IZipNode {

	private String name;
	
	private final List<Node> children;
	
	/**
	 * @param name node name
	 * @param children children list, not null
	 */
	public Node(final String name, final Node... children) {
		super();
		
		assert(name != null);
		assert(children != null);
		
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

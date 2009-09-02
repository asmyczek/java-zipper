package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mu.zipper.Loc;
import com.mu.zipper.ZipNode;
import com.mu.zipper.examples.zipstar.ZipStar.ZipStarNode;

public class Path {

	private final Loc<ZipStarNode> loc;

	public Path(Loc<ZipStarNode> loc) {
		super();
		this.loc = loc;
	}
	
	public Collection<Node> getPath() {
    	List<Node> result = new ArrayList<Node>();
    	for (ZipNode<ZipStarNode> n : loc.nodePath()) {
    		result.add(n._source().node);
    	}
    	return result;
	}
	
	public double getDistance() {
		return loc._source().distanceFromStart;
	}
	
}

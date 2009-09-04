package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mu.zipper.Loc;
import com.mu.zipper.ZipNode;
import com.mu.zipper.examples.zipstar.ZipStar.ZipStarNode;

/**
 * ZipStar result object.
 * 
 * @author Adam Smyczek
 */
public final class Path {

	private final Loc<ZipStarNode> loc;

	protected Path(Loc<ZipStarNode> loc) {
		super();
		this.loc = loc;
	}
	
	/**
	 * @return the node path from start to target.
	 */
	public Collection<Node> getPath() {
    	List<Node> result = new ArrayList<Node>();
    	for (ZipNode<ZipStarNode> n : loc.nodePath()) {
    		result.add(n._source().node);
    	}
    	return result;
	}
	
	/**
	 * @return distance/cost from start node to target node.
	 */
	public double getDistance() {
		return loc._source().distanceFromStart;
	}
	
}

package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mu.zipper.IZipNode;
import com.mu.zipper.Loc;
import com.mu.zipper.ZipNode;
import com.mu.zipper.Zipper;

public final class ZipStar {

	public final static Collection<Node> calcPath(final Graph graph, final Node from, final Node to) {
		return new ZipStar(graph).calcPath(from, to);
	}
	
	private final Map<Node, ZipStarNode> nodes;
	
	private final Graph graph;
	
	private ZipStar(final Graph graph) {
		super();
		this.graph = graph;
		nodes = new Hashtable<Node, ZipStarNode>();
		for (Node node : graph.getNodes()) {
			nodes.put(node, new ZipStarNode(node));
		}
	}
	
	private void removeNode(final ZipStarNode znode) {
		nodes.remove(znode.node);
	}
	
	private Collection<ZipStarNode> getChildren(final ZipStarNode node) {
		List<ZipStarNode> result = new ArrayList<ZipStarNode>();
		for (Node aNode : node.node.adjacentNodes()) {
			ZipStarNode znode = nodes.get(aNode);
			if (znode != null) {
				result.add(znode);
			}
		}
		return result;
	}
	
	private double getWeight(ZipStarNode from, ZipStarNode to) {
		return graph.getWeight(from.node, to.node);
	}
	
	private Collection<Node> calcPath(final Node from, final Node to) {
		ZipStarNode target = nodes.get(to);
		Loc<ZipStarNode> loc = Zipper.<ZipStarNode>newZipper(nodes.get(from));
		List<Loc<ZipStarNode>> paths = new ArrayList<Loc<ZipStarNode>>(); 
		
		paths.add(loc);
		removeNode(loc._source());
		
		Loc<ZipStarNode> path;
		while ((path = nextNode(paths, target)) == null);
			
		System.out.println("Dist: " + path._source().distanceFromStart);
		
		List<Node> result = new ArrayList<Node>();
		for (ZipNode<ZipStarNode> n : path.nodePath()) {
			result.add(n._source().node);
		}
		return result;
	}
	
	private Loc<ZipStarNode> nextNode(final List<Loc<ZipStarNode>> paths, final ZipStarNode target) {
		Loc<ZipStarNode> nextNode = null;
		Double minCost = null;
		for(Iterator<Loc<ZipStarNode>> pi = paths.iterator(); pi.hasNext(); ) {
			Loc<ZipStarNode> l = pi.next();
			ZipStarNode lnode = l._source();
			
			// Remove from list if all children analyzed
			List<ZipStarNode> znodes = new ArrayList<ZipStarNode>(l._source().getChildren());
			if (znodes.isEmpty()) {
				pi.remove();
				continue;
			}
			
			for (int i = 0; i < znodes.size(); i++) {
    			ZipStarNode znode = znodes.get(i);
    			double distFromStart = lnode.distanceFromStart + getWeight(lnode, znode);
    			double cost = distFromStart + znode.directDistanceTo(target);
				minCost = (minCost == null)? cost : Math.min(cost, minCost);
				if (cost == minCost) {
					nextNode = l.down(i);
					nextNode._source().distanceFromStart = distFromStart;
				}
			}
	    }
		
		if (target.equals(nextNode._source())) {
			return nextNode;
		} else if (nextNode != null) {
			paths.add(nextNode);
			removeNode(nextNode._source());
		} else {
			throw new IllegalArgumentException("No path between start and end node exists!");
		}
		
		return null;
	}
	

	private class ZipStarNode implements IZipNode {

		double distanceFromStart;
		final Node node;
		
		public ZipStarNode(Node node) {
			super();
			this.node = node;
			this.distanceFromStart = 0;
		}

		public double directDistanceTo(final ZipStarNode to) {
			return node.directDistanceTo(to.node);
		}
		
		public Collection<ZipStarNode> getChildren() {
			return ZipStar.this.getChildren(this);
		}
		
	}
	
}

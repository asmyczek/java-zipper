package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.mu.zipper.IZipNode;
import com.mu.zipper.Loc;
import com.mu.zipper.Zipper;

public final class ZipStar {

	public final static Path calcPath(final Graph graph, final Node from, final Node to) {
		return new ZipStar(graph, from, to).calcPath();
	}
	
	private final Graph graph;
	
	private final Node start;
	
	private final Node target;
	
	private final List<Node> activeNodes;
	
	private ZipStar(final Graph graph, final Node start, final Node target) {
		super();
		this.graph = graph;
		this.start = start;
		this.target = target;
		this.activeNodes = new ArrayList<Node>(graph.getNodes());
	}
	
	private Collection<ZipStarNode> getChildren(final ZipStarNode node) {
		List<ZipStarNode> children = new ArrayList<ZipStarNode>();
		for (Node n : node.node.adjacentNodes()) {
			if (activeNodes.contains(n)) {
    			children.add(new ZipStarNode(n, n.directDistanceTo(target)));
			}
		}
		return children;
	}
	
	private Path calcPath() {
		Comparator<Loc<ZipStarNode>> comparator = new Comparator<Loc<ZipStarNode>>() {
			public int compare(Loc<ZipStarNode> o1, Loc<ZipStarNode> o2) {
				return (int) Math.signum(o1._source().distance() - o2._source().distance());
			}
		};
		SortedList<Loc<ZipStarNode>> paths = new SortedList<Loc<ZipStarNode>>(comparator);
		paths.add(Zipper.<ZipStarNode>newZipper(new ZipStarNode(start, start.directDistanceTo(target))));
		follow(paths);
		
		if (!paths.isEmpty()) {
			return new Path(paths.get(0));
		}
		throw new RuntimeException(String.format("No path exists from %1$s to %2$s", start, target));
	}
	
	private void follow(SortedList<Loc<ZipStarNode>> paths) {
		if (paths.isEmpty() || paths.getFirst()._source().node.equals(target)) return;
		
		Loc<ZipStarNode> first = paths.removeFirst();
		activeNodes.remove(first._source().node);
		
		for (int i = 0 ; i < first.node().getChildren().size(); i++) {
			Loc<ZipStarNode> down = first.down(i); 
			down._source().distanceFromStart = first._source().distanceFromStart + 
			    graph.getWeight(first._source().node, down._source().node);
			paths.add(down);
		}
		
	    follow(paths);
	}
	
	class ZipStarNode implements IZipNode {

		double distanceFromStart;
		double distanceToTarget;
		final Node node;
		
		public ZipStarNode(Node node, double distanceToTarget) {
			super();
			this.node = node;
			this.distanceFromStart = 0;
			this.distanceToTarget = distanceToTarget;
		}

		public double distance() {
			return distanceFromStart + distanceToTarget;
		}
		
		public Collection<ZipStarNode> getChildren() {
			return ZipStar.this.getChildren(this);
		}

	}
	
}

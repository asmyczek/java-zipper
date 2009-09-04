package com.mu.zipper.examples.zipstar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.mu.zipper.IZipNode;
import com.mu.zipper.Loc;
import com.mu.zipper.Zipper;

/**
 * A* algorithm in Zipper.
 * Not a conventional Zipper application, but it makes a nice example.
 * For details on A* see <a target="_blank" href="http://en.wikipedia.org/wiki/A*">
 * 'A*' on Wikipedia</a>
 * 
 * @author Adam Smyczek
 */
public final class ZipStar {

	/**
	 * Calculate the best-first path from <tt>start</tt> 
	 * to <tt>target</tt> node for the given <tt>graph</tt>.
	 * 
	 * @param graph the graph
	 * @param start node
	 * @param target node
	 * @return Path result containing the path and distance information
	 */
	public final static Path calcPath(final Graph graph, final Node start, final Node target) {
		return new ZipStar(graph, start, target).calcPath();
	}
	
	// ---- Private implementation ----
	
	private final Graph graph;
	
	private final Node start;
	
	private final Node target;
	
	// List of potential nodes to be evaluated.
	private final List<Node> activeNodes;
	
	/**
	 * The constructor performs plain assignments,
	 * no initialization required.
	 * 
	 * @param graph
	 * @param start
	 * @param target
	 */
	private ZipStar(final Graph graph, final Node start, final Node target) {
		super();
		this.graph = graph;
		this.start = start;
		this.target = target;
		this.activeNodes = new ArrayList<Node>(graph.getNodes());
	}
	
	/**
	 * Calculates a set of currently reachable graph nodes from <tt>node</tt>.
	 * <tt>getChildren()</tt> returns only the active/not evaluated reachable 
	 * nodes. The result set changes as the path search proceeds. 
	 * 
	 * @param node
	 * @return current list of reachable and active/not visited nodes for a given <tt>node</tt>
	 */
	private Collection<ZipStarNode> getChildren(final ZipStarNode node) {
		List<ZipStarNode> children = new ArrayList<ZipStarNode>();
		for (Node n : node.node.adjacentNodes()) {
			if (activeNodes.contains(n)) {
    			children.add(new ZipStarNode(n, n.directDistanceTo(target)));
			}
		}
		return children;
	}
	
	/**
	 * The algorithm
	 * @return path result
	 * @throws IllegalStateException if no path form <tt>start</tt> to <tt>target</tt> exists.
	 */
	private Path calcPath() {
		// List of possible paths locations sorted by the distance-plus-cost function, see comparator
		SortedList<Loc<ZipStarNode>> paths = new SortedList<Loc<ZipStarNode>>(distance_plus_cost_comparator);
		
		// Start location from the start node
		Loc<ZipStarNode> startLoc = Zipper.<ZipStarNode>newZipper(new ZipStarNode(start, start.directDistanceTo(target)));
		paths.add(startLoc);
		
		// Calculate possible best-first paths recursively
		follow(paths);
		
		// If paths is not empty, first location represents
		// the best-first path from start to target node
		if (!paths.isEmpty()) {
			return new Path(paths.get(0));
		}
		// otherwise no path exists between this nodes
		throw new IllegalStateException(String.format("No path exists from %1$s to %2$s", start, target));
	}
	
	/**
	 * Calculate possible best-first paths recursively
	 * @param paths sorted by the best distance-plus-cost function
	 */
	private void follow(SortedList<Loc<ZipStarNode>> paths) {
		// If paths is empty, no path between start and target node exists,
		// If first node is the target node, best-first path found.
		if (paths.isEmpty() || paths.getFirst()._source().node.equals(target)) return;
		
		// Follow the best location, first path in paths list:
		// 1. remove first node from the path list,
		// 2. follow all it's children and add this to the paths list.
		Loc<ZipStarNode> first = paths.removeFirst();
		activeNodes.remove(first._source().node);
		
		for (int i = 0 ; i < first.node().getChildren().size(); i++) {
			Loc<ZipStarNode> down = first.down(i); 
			down._source().distanceFromStart = first._source().distanceFromStart + 
			    graph.getWeight(first._source().node, down._source().node);
			paths.add(down);
		}
		
		// Follow paths
	    follow(paths);
	}
	
	/**
	 * The link between graph and Zipper nodes,
	 * a wrapper around a graph node which implements
	 * the <tt>IZipNode</tt> interface.
	 */
	protected class ZipStarNode implements IZipNode {

		double distanceFromStart;
		double distanceToTarget;
		final Node node;
		
		/**
		 * @param node the wrapped graph node
		 * @param distanceToTarget cached air distance from 
		 *        this node to the target
		 */
		public ZipStarNode(Node node, double distanceToTarget) {
			super();
			this.node = node;
			this.distanceFromStart = 0;
			this.distanceToTarget = distanceToTarget;
		}

		/**
		 * The distance-plus-cost heuristic function calculated
		 * from the distance from star node and the air distance
		 * to the target node.
		 * 
		 * @return sum of the traveled distance from start node
		 * and the air distance to target node
		 */
		public double distance() {
			return distanceFromStart + distanceToTarget;
		}
		
		/**
		 * A delegate to ZipStar <tt>getChildren()</tt>.
		 * 
		 * @return current list of reachable nodes,
		 * already visited nodes excluded.
		 */
		public Collection<ZipStarNode> getChildren() {
			return ZipStar.this.getChildren(this);
		}

	}
	
	/**
	 * Compares nodes inside a location based on distance-plus-cost
	 * function <tt>distance()</tt>
	 */
	private final static Comparator<Loc<ZipStarNode>> distance_plus_cost_comparator = 
		new Comparator<Loc<ZipStarNode>>() {
    		public int compare(Loc<ZipStarNode> o1, Loc<ZipStarNode> o2) {
    			return (int) Math.signum(o1._source().distance() - o2._source().distance());
    		}
    	};
	
}

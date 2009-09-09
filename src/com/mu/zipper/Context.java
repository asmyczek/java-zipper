package com.mu.zipper;

/**
 * Internal location context holds references
 * to the parent context and all sibling nodes 
 * of the current location node.
 * 
 * @author Adam Smyczek
 */
final class Context {

	// Top context, marks the context of the root node
	protected static final Context TOP = new Context(null, null, null, null);
	
	// Left sibling nodes
	private final IZipNode[] left;
	
	// Right sibling nodes
	private final IZipNode[] right;
	
	// Parent context
	private final Context parentContext;
	
	// Parent node
	private final ZipNode<?> parentNode;
	
	protected Context(
			final ZipNode<?> parentNode, 
			final Context parentContext, 
			final IZipNode[] left, 
			final IZipNode[] right) {
		super();
		this.parentNode = parentNode;
		this.parentContext = parentContext;
		this.left = (left == null)? new IZipNode[0] : left;
		this.right = (right == null)? new IZipNode[0] : right;
	}
	
	/**
	 * @return true if the current location marks the root node
	 */
	protected boolean isTop() {
		return parentNode == null;
	}
	
	/**
	 * @return true if the current location is the first sibling
	 */
	protected boolean isFirst() {
		return left.length == 0;
	}
	
	/**
	 * @return true if the current location is the last sibling
	 */
	protected boolean isLast() {
		return right.length == 0;
	}
	
	/**
	 * @return left sibling nodes
	 */
	protected IZipNode[] leftNodes() {
		return left;
	}
	
	/**
	 * @return right sibling nodes
	 */
	protected IZipNode[] rightNodes() {
		return right;
	}

	/**
	 * @return parent context
	 */
	protected Context getParentContext() {
		return parentContext;
	}

	/**
	 * @return parent node
	 */
	protected ZipNode<?> getParentNode() {
		return parentNode;
	}
	
	/**
	 * Helper copy function.
	 * 
	 * @return a shallow copy of this context
	 */
	protected Context copy() {
		IZipNode[] l = new IZipNode[left.length];
    	System.arraycopy(left, 0, l, 0, left.length);
		IZipNode[] r = new IZipNode[right.length];
    	System.arraycopy(right, 0, r, 0, right.length);
		return new Context(parentNode, parentContext, l, r);
	}

}

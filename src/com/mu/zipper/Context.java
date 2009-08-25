package com.mu.zipper;


public class Context {

	protected static final Context TOP = new Context(null, null, null, null);
	
	private final IZipNode[] left;
	
	private final IZipNode[] right;
	
	private final Context parentContext;
	
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
	
	protected boolean isTop() {
		return parentNode == null;
	}
	
	protected boolean isFirst() {
		return left.length == 0;
	}
	
	protected boolean isLast() {
		return right.length == 0;
	}
	
	protected IZipNode[] leftNodes() {
		return left;
	}
	
	protected IZipNode[] rightNodes() {
		return right;
	}

	protected Context getParentContext() {
		return parentContext;
	}

	protected ZipNode<?> getParentNode() {
		return parentNode;
	}
	
	protected Context copy() {
		IZipNode[] l = new IZipNode[left.length];
    	System.arraycopy(left, 0, l, 0, left.length);
		IZipNode[] r = new IZipNode[right.length];
    	System.arraycopy(right, 0, r, 0, right.length);
		return new Context(parentNode, parentContext, l, r);
	}

}

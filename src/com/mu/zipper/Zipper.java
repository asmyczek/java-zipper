package com.mu.zipper;

public final class Zipper {

	public static <T extends IZipNode> Loc<T> newZipper(final T root) {
		return new Loc<T>(new ZipNode<T>(root, root.getChildren()), Context.TOP);
	}
	
	
}

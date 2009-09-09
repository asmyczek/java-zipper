package com.mu.zipper;

/**
 * General Zipper exception.
 * 
 * @author Adam Smyczek
 */
public class ZipperException extends RuntimeException {

	public ZipperException(String message) {
		super(message);
	}

	public ZipperException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 6752384268429655524L;

}

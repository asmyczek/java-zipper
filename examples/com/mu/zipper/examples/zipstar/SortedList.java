package com.mu.zipper.examples.zipstar;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A simple SortedList implementation.
 * 
 * @param <E> element type
 * 
 * @author Adam Smyczek
 */
public class SortedList<E> implements List<E> {

	private final Comparator<E> comparator;
	
	private final LinkedList<E> delegate = new LinkedList<E>();
	
	/**
	 * The constructor takes a comparator used
	 * for element insertion. See <tt>add()</tt> method.
	 * 
	 * @param comparator
	 */
	public SortedList(Comparator<E> comparator) {
		super();
		if (comparator == null) throw new IllegalArgumentException("Comparator is null!");
		this.comparator = comparator;
	}

	/**
	 * Inserts new element in the order defined by <tt>comparator</tt>.
	 * 
	 * @param o the object to insert
	 */
	public boolean add(E o) {
        int idx = Collections.binarySearch(delegate, o, comparator);
        delegate.add((int)Math.signum(idx) * (idx + 1), o);
        return true;
	}

	/**
	 * Inserts all elements in the order defined by <tt>comparator</tt>.
	 * 
	 * @param c
	 * @return true
	 */
	public boolean add(E... c) {
		for (E e : c) add(e);
		return true;
	}
	
	/**
	 * Same as <tt>add(E... c)</tt>, but for collections.
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) add(e);
		return true;
	}

	// ---- LinkedList methods ----
	
	public E getFirst() {
		return delegate.getFirst();
	}
	
	public E getLast() {
		return delegate.getLast();
	}
	
	public E removeFirst() {
		return delegate.removeFirst();
	}
	
	public E removeLast() {
		return delegate.removeLast();
	}
	
	// ---- Delegated methods ----
	
	public void clear() {
	    delegate.clear();
	}

	public boolean contains(Object o) {
		return delegate.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return delegate.containsAll(c);
	}

	public E get(int index) {
		return delegate.get(index);
	}

	public int indexOf(Object o) {
		return delegate.indexOf(o);
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public Iterator<E> iterator() {
		return delegate.iterator();
	}

	public int lastIndexOf(Object o) {
		return delegate.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return delegate.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return delegate.listIterator(index);
	}

	public boolean remove(Object o) {
		return delegate.remove(o);
	}

	public E remove(int index) {
		return delegate.remove(index);
	}

	public boolean removeAll(Collection<?> c) {
		return delegate.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return delegate.retainAll(c);
	}

	public int size() {
		return delegate.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return delegate.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return delegate.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return delegate.toArray(a);
	}

	// ---- Unsupported methods ----
	
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

}

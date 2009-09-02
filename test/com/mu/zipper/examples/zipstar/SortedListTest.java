package com.mu.zipper.examples.zipstar;

import java.util.Comparator;

import junit.framework.TestCase;

public class SortedListTest extends TestCase {

	public void testSortedList() {
		
		Comparator<String> comparator = new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		};
		SortedList<String> list = new SortedList<String>(comparator);
		
		list.add("b", "d", "e");
		list.add("a");
		list.add("c");
		list.add("d");
		list.add("f");
		list.add("g");
		
		assertEquals("a", list.removeFirst());
		assertEquals("b", list.removeFirst());
		assertEquals("c", list.removeFirst());
		assertEquals("d", list.removeFirst());
		assertEquals("d", list.removeFirst());
		assertEquals("e", list.removeFirst());
		assertEquals("f", list.removeFirst());
		assertEquals("g", list.removeFirst());
		
		assertTrue(list.isEmpty());
		
	}
	
}

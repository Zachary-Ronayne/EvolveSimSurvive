package util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import evolve.util.ArrayHandler;

public class ArrayHandlerTest{
	
	@Test
	public void testAdd(){
		Integer[] arr = new Integer[]{1, 2, 3};
		arr = ArrayHandler.add(arr, 2);
		assertTrue(arr.length == 4);
		assertTrue(arr[0] == 1);
		assertTrue(arr[1] == 2);
		assertTrue(arr[2] == 3);
		assertTrue(arr[3] == 2);
	}
	
	@Test
	public void testRemove(){
		Integer[] arr = new Integer[]{1, 2, 3};
		arr = ArrayHandler.remove(arr, 1);
		assertTrue(arr.length == 2);
		assertTrue(arr[0] == 1);
		assertTrue(arr[1] == 3);
	}
	
}

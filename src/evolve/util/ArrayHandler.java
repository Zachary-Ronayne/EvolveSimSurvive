package evolve.util;

import java.lang.reflect.Array;

/**
 * A class that handles some miscellaneous array actions
 */
public final class ArrayHandler{
	
	/**
	 * Remove the given index from the given array.<br>
	 * @param arr the array
	 * @param index the index
	 * @return the array with the removed index
	 */
	public static<E> E[] remove(E[] arr, int index){
		@SuppressWarnings("unchecked")
		E[] newArr = (E[]) Array.newInstance(arr[0].getClass(), arr.length - 1);
		int pos = 0;
		for(int i = 0; i < arr.length; i++){
			if(i != index){
				newArr[pos] = arr[i];
				pos++;
			}
		}
		return newArr;
	}
	
	/**
	 * Add the given object to the given array.<br>
	 * Array and add must be the same type for this action to work correctly.<br>
	 * @param arr the array to add to
	 * @param add the object to add
	 * @return the array with the added object
	 */
	public static<E> E[] add(E[] arr, E add){
		@SuppressWarnings("unchecked")
		E[] newArr = (E[]) Array.newInstance(add.getClass(), arr.length + 1);
		
		for(int i = 0; i < arr.length; i++){
			newArr[i] = arr[i];
		}
		newArr[newArr.length - 1] = add;
		return newArr;
	}
	
}

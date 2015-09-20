package com.jfcorugedo.rserver.common.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CollectionUtils {

	private CollectionUtils(){
		
	}
	
	public static <T> List<T> arrayToList(T[] array){
		return Arrays.stream(array).collect(Collectors.toList());
	}

	/**
	 * Return true if this map is null or it's empty.
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<? extends Object, ? extends Object> map) {
		return (map == null || map.size() == 0);
	}
	
	public static boolean isEmpty(List<? extends Object> list) {
		return (list == null || list.size() == 0);
	}
	
	/**
	 * Return true if this map contains one value at least
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isNotEmpty(Map<? extends Object, ? extends Object> map) {
		return !isEmpty(map);
	}
	
	public static boolean isNotEmpty(Collection<? extends Object> collection) {
		return (collection != null && collection.size() > 0);
	}
	
	/**
	 * Converts the given list of Double objects into an array of primitive doubles.
	 * 
	 * If the list is null or empty, an empty array will be returned
	 * 
	 * @param inputList List of Double object to be converted in a primitive array
	 * @return
	 */
	public static double[] listToPrimitiveArray(List<Double> inputList) {
		if(isEmpty(inputList)) {
			return new double[0];
		} else {
			return inputList.stream().mapToDouble(Double::new).toArray();
		}
	}
	
	/**
	 * This method creates a new list and store in it all the objects contained in the
	 * given list.
	 * 
	 * @param targetList Source list used to create a new one
	 * @return New list containing all the objects of the primitive list
	 */
	public static <T> List<T> cloneList(List<T> targetList) {
		return targetList.stream().collect(Collectors.toList());
	}
	
	/**
	 * Transform a list of objects into an array of primitives
	 * 
	 * @param listOfObject
	 * @return
	 */
	public static double[] convertToPrimitives(List<Double> listOfObjects) {
		return listOfObjects.stream().mapToDouble(Double::doubleValue).toArray();
	}
	
	/**
	 * Creates a new list with the given elements
	 * @param values
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> newList(T... values) {
		List<T> arrayList = new ArrayList<T>(values.length);
		for(T element : values) {
			arrayList.add(element);
		}
		
		return arrayList;
	}
}

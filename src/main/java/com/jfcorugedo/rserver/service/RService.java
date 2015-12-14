package com.jfcorugedo.rserver.service;

import java.util.List;


public interface RService {

	/**
	 * Groups the given values in pairs based on the correlation between values.
	 * 
	 * For instance, given the following values:
	 * 
	 * {1.0, 2.0, 2.0, 1.0}
	 * 
	 * This function will return
	 * 
	 * [{0,3}, {1,2}]
	 * 
	 * So the first element in the values array must be paired with the fourth element of the array.
	 * Similarly, the second element of the array must be paired with the third element.
	 * 
	 * @param values Values to be paired
	 * @return List of pairs. Each element in the list will have to indexes that point at the elements
	 * of the input array that must be paired
	 */
	List<int[]> groupValues(List<double[]> values);

	/**
	 * Groups the given values in pairs based on the correlation between values.
	 * 
	 * For instance, given the following values:
	 * 
	 * {"A", "B", "B", "A"}
	 * 
	 * This function will return
	 * 
	 * [{0,3}, {1,2}]
	 * 
	 * So the first element in the values array must be paired with the fourth element of the array.
	 * Similarly, the second element of the array must be paired with the third element.
	 * 
	 * @param values Values to be paired
	 * @return List of pairs. Each element in the list will have to indexes that point at the elements
	 * of the input array that must be paired
	 */
	List<int[]> groupDiscreteValues(List<String[]> values);
	
	/**
	 * Groups the given values in pairs based on the correlation between values.
	 * 
	 * For instance, given the following values:
	 * 
	 * {1.0, 2.0, 3.0, 4.0}, {"A", "A", "B", "B"}
	 * 
	 * This function will return
	 * 
	 * [{0,3}, {1,2}]
	 * 
	 * So the first element in the values array must be paired with the fourth element of the array.
	 * Similarly, the second element of the array must be paired with the third element.
	 * 
	 * @param values Values to be paired
	 * @return List of pairs. Each element in the list will have to indexes that point at the elements
	 * of the input array that must be paired
	 */
	List<int[]> groupMultipleValues(List<String[]> discreteValues, List<double[]> continuousValues);
	
	/**
	 * Computes the p-value, or observed significance level, of a two-sample Kolmogorov-Smirnov test 
	 * evaluating the null hypothesis that x and y are samples drawn from the same probability distribution.
	 *  
	 * @param x
	 * @param y
	 * @return
	 */
	double kolmogorovSmirnovTest(double[] x, double[] y);

	
	/**
	 * This method returns the square root of the given value
	 * @param number
	 * @return
	 */
	double sqrt(double number);
}

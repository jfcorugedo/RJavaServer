package com.jfcorugedo.rserver.generalization.algorithm;

/**
 * This component executes Chi-Square test over two samples in order to compare them.
 * 
 * 
 * @author jfcorugedo
 *
 */
public interface ChiSquareTest {

	double DEFAULT_CONFIDENCE_LEVEL = 0.05;

	/**
	 * This test evaluates the null hypothesis that the two list of observed counts
	 * conform to the same frequency distribution.
	 * 
	 * @param observed1
	 * @param observed2
	 * @return true if the null hypothesis can be accepted
	 */
	boolean areGeneralizable(long[] observed1, long[] observed2);
	
	/**
	 * It returns a string representation of the algorithm used to compare the given samples.
	 * 
	 * @return
	 */
	String getAlgorithm();
}

package com.jfcorugedo.rserver.generalization.algorithm;

/**
 * This component executes Two-sample Kolmogorov–Smirnov test over two samples in order to
 * know if both of them are equivalent and generalizable
 * 
 * @see https://en.wikipedia.org/wiki/Kolmogorov–Smirnov_test
 * @author jfcorugedo
 *
 */
public interface KolmogorovSmirnovTest {

	double CRITICAL_VALUE_0_05 = 1.36;
	double CRITICAL_VALUE_0_01 = 1.63;

	/**
	 * This test try to test if the null hypothesis is truth or not.
	 * 
	 * The null hypothesis is that the samples are drawn from the same distribution.
	 * 
	 */
	boolean areGeneralizable(double[] x, double[] y);

	/**
	 * It returns a string representation of the algorithm used to compare the given samples.
	 * 
	 * @return
	 */
	String getAlgorithm();
}

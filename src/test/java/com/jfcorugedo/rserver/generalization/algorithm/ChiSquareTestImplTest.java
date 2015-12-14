package com.jfcorugedo.rserver.generalization.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.jfcorugedo.rserver.exception.DimensionMismatchException;

public class ChiSquareTestImplTest {

	@Test
	public void areSimilarDistributionGeneralizable() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{10, 20, 20, 30}, 
											new long[]{100, 200, 300, 400}
										);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void areDifferentDistributionGeneralizable() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{10, 20, 20, 30}, 
											new long[]{200, 100, 300, 400}
										);
		
		assertThat(result).isFalse();
	}
	
	@Test
	public void compareTwoValuesWhenOneOfThemHasZeroFrequency() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{0, 2000}, 
											new long[]{0, 10}
										);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void secondSampleHasZeroFrequency() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{10, 0}, 
											new long[]{1, 0}
										);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void firstSampleHasZeroFrequency() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{0, 2000}, 
											new long[]{10, 10}
										);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void binaryFrequenciesWithoutAnyZero() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{1000, 20000}, 
											new long[]{100, 100}
										);
		
		assertThat(result).isFalse();
	}
	
	@Test(expected=DimensionMismatchException.class)
	public void compareTwoSamplesWithDifferentLength() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		boolean result = chiSquareTest.areGeneralizable(
											new long[]{10, 20, 30}, 
											new long[]{1, 2}
										);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void getAlgorithm() {
		
		ChiSquareTest chiSquareTest = new ChiSquareTestImpl();
		
		String algorithm = chiSquareTest.getAlgorithm();
		
		assertThat(algorithm).isEqualTo("Chi-Square test");
	}
}

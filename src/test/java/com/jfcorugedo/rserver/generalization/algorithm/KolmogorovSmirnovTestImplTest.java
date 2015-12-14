package com.jfcorugedo.rserver.generalization.algorithm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.stream.IntStream;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.jfcorugedo.rserver.service.RService;

public class KolmogorovSmirnovTestImplTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private RService rService;

	@InjectMocks
	private KolmogorovSmirnovTestImpl ksTest = new KolmogorovSmirnovTestImpl();
	
	@Test
	public void areTwoEqualSamplesGeneralizable() {
		
		setKSPValue(1.0);
		
		double[] sample = new double[]{1.0, 2.0, 3.0, 4.0};
		
		boolean result = ksTest.areGeneralizable(sample, sample);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void areTwoDifferentSamplesGeneralizable() {
		
		setKSPValue(0.03);
		
		double[] sample = new double[]{1.0, 2.0, 3.0, 4.0};
		double[] different = new double[]{-1.0, -2.0, -3.0, -4.0};
		
		boolean result = ksTest.areGeneralizable(sample, different);
		
		assertThat(result).isFalse();
	}
	
	@Test
	public void getAlgorithmName() {
		
		String algorithm = ksTest.getAlgorithm();
		
		assertThat(algorithm).isEqualTo("K-S test");
	}
	
	@Test
	public void testSameSamples() {
		
		setKSPValue(0.03);
		
		double[] sample1 = IntStream.range(0, 100).asDoubleStream().toArray();
		double[] sample2 = IntStream.range(0, 100).asDoubleStream().toArray();
		
		boolean isGeneralizable = ksTest.areGeneralizable(sample1, sample2);
		
		assertThat(isGeneralizable).isFalse();
	}

	private void setKSPValue(double pValue) {
		
		when(rService.kolmogorovSmirnovTest(any(double[].class), any(double[].class))).thenReturn(pValue);
	}
}

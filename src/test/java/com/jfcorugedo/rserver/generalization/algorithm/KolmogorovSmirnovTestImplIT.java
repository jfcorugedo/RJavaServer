package com.jfcorugedo.rserver.generalization.algorithm;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jfcorugedo.rserver.Application;

@Ignore("These tests requires R environment installed")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles(profiles={"local", "integrationtest"})
public class KolmogorovSmirnovTestImplIT {
	
	@Inject
	private KolmogorovSmirnovTest ksTest;
	
	@Test
	public void areTwoEqualSamplesGeneralizable() {
		
		double[] sample = new double[]{1.0, 2.0, 3.0, 4.0};
		
		boolean result = ksTest.areGeneralizable(sample, sample);
		
		assertThat(result).isTrue();
	}
	
	@Test
	public void areTwoDifferentSamplesGeneralizable() {
		
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
	public void testSameLargeSamples() {
		
		double[] sample1 = IntStream.range(0, 10000).asDoubleStream().toArray();
		double[] sample2 = IntStream.range(0, 10000).asDoubleStream().toArray();
		
		boolean isGeneralizable = ksTest.areGeneralizable(sample1, sample2);
		
		assertThat(isGeneralizable).isTrue();
	}

}
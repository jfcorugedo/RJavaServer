package com.jfcorugedo.rserver.generalization.algorithm;

import java.util.Arrays;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jfcorugedo.rserver.service.RService;

@Component
public class KolmogorovSmirnovTestImpl implements KolmogorovSmirnovTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KolmogorovSmirnovTestImpl.class);
	
	/** Name of the algorithm used to compare two samples */
	private static final String KS_TEST = "K-S test";
	
	@Inject
	private RService rService;
	
	/**
	 * This test try to test if the null hypothesis is truth or not.
	 * 
	 * The null hypothesis is that the samples are drawn from the same distribution.
	 * 
	 */
	@Override
	public boolean areGeneralizable(double[] x, double[] y){
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Calculating K-S test of \nx: {}\ny: {}", Arrays.toString(x), Arrays.toString(y));
		}
		
		double pValue = rService.kolmogorovSmirnovTest(x, y);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("K-S pValue calculated: {}", pValue);
		}
		
		//Accept null hypothesis: Both samples come from the same distribution
		return pValue > 0.05;
	}
	
	@Override
	public String getAlgorithm() {
		
		return KS_TEST;
	}
}

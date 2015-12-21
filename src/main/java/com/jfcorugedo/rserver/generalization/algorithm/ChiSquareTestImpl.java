package com.jfcorugedo.rserver.generalization.algorithm;

import org.springframework.stereotype.Component;

import com.codahale.metrics.annotation.Timed;
import com.jfcorugedo.rserver.exception.DimensionMismatchException;

import static java.lang.String.format;

@Component
public class ChiSquareTestImpl implements ChiSquareTest {

	public static final String CHI_TEST = "Chi-Square test";
	
	private org.apache.commons.math3.stat.inference.ChiSquareTest chiTest = new org.apache.commons.math3.stat.inference.ChiSquareTest();
	
	@Override
	@Timed
	public boolean areGeneralizable(long[] observed1, long[] observed2) {
		
		if(observed1.length != observed2.length) {
			throw new DimensionMismatchException(format("Observed samples have different dimension: %d != %d", observed1.length, observed2.length));
		}
		
		if(isBinary(observed1, observed2)) {
			if(observed1[0] == 0 || observed1[1] == 0){
				return true;
			}
		}
		
		return !chiTest.chiSquareTestDataSetsComparison(observed1, observed2, DEFAULT_CONFIDENCE_LEVEL);
	}

	/**
	 * it returns true when there are only two possible values on each sample.
	 * 
	 * For instance, given two samples with these samples:
	 * 
	 * <pre>
	 * - "true":10, "false":20
	 * - "true":2, "false":3
	 * </pre>
	 * 
	 * This method will return true, because there are only two possible values: "true" or "false".
	 * 
	 * On the other hand, given these samples:
	 * 
	 * <pre>
	 * - "MAD":10, "LON":23, "NY":45
	 * - "MAD":2, "LON":4, "NY":7
	 * </pre>
	 * 
	 * This method will return flase, because there three possible values in each sample
	 * 
	 * @param observed1
	 * @param observed2
	 * @return
	 */
	private boolean isBinary(long[] observed1, long[] observed2) {
		
		return observed1.length == 2;
	}

	@Override
	public String getAlgorithm() {
		
		return CHI_TEST;
	}

}

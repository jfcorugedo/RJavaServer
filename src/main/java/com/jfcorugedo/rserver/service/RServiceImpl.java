package com.jfcorugedo.rserver.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jfcorugedo.rserver.engine.REngineProviderService;

@Service
public class RServiceImpl implements RService{

	private static final Logger LOGGER = LoggerFactory.getLogger(RServiceImpl.class);
	
	@Inject
	private REngineProviderService engineProvider;
	
	@Override
	public List<int[]> groupValues(List<double[]> values) {
		
		REXPInteger rIds = new REXPInteger(generateIds(values.get(0).length));
		REXPDouble[] rValues = values.stream().map(rawValues -> new REXPDouble(rawValues)).toArray(REXPDouble[]::new);
		
		REXP result = engineProvider.blockFunction(rIds, rValues);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("blockFunction:\n{}", blockResultToString(result));
		}
		return formatResult(result);
	}
	
	@Override
	public List<int[]> groupDiscreteValues(List<String[]> values) {
		
		REXPInteger rIds = new REXPInteger(generateIds(values.get(0).length));
		REXPString[] rValues = values.stream().map(rawValues -> new REXPString(rawValues)).toArray(REXPString[]::new);
		
		REXP result = engineProvider.blockDiscreteFunction(rIds, rValues);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("blockDiscreteFunction:\n{}", blockResultToString(result));
		}
		return formatResult(result);
	}
	
	@Override
	public List<int[]> groupMultipleValues(List<String[]> discreteValues, List<double[]> continuousValues) {
		
		REXPInteger rIds = new REXPInteger(generateIds(discreteValues.get(0).length));
		List<REXPString> discreteNativeValues = discreteValues.stream()
														.map(values -> new REXPString(values))
														.collect(Collectors.toList()); 
		List<REXPDouble> continuousNativeValues = continuousValues.stream()
															.map(values -> new REXPDouble(values))
															.collect(Collectors.toList());
		
		REXP result = engineProvider.blockGeneralFunction(rIds, discreteNativeValues, continuousNativeValues);
				
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("blockGeneralFunction:\n{}", blockResultToString(result));
		}
		return formatResult(result);
	}

	/**
	 * Format the results from Rserve objects to java standard objects.
	 * If the results are odd, the last element of the list will contain only one number
	 * @param result
	 * @return
	 */
	private List<int[]> formatResult(REXP result) {
		REXPGenericVector vector = (REXPGenericVector) result;
		
		Integer[] unit1 = getNumbers((REXP)vector.asList().get(0));
		Integer[] unit2 = getNumbers((REXP)vector.asList().get(1));
		
		List<int[]> resultFormated = new ArrayList<int[]>(unit1.length);
		for(int index = 0 ; index < unit2.length -1 ; index++) {
			resultFormated.add(new int[]{unit1[index], unit2[index]});
		}
		if(unit2[unit2.length - 1] == null){
			resultFormated.add(new int[]{unit1[unit2.length - 1]});
		} else {
			resultFormated.add(new int[]{unit1[unit2.length - 1], unit2[unit2.length - 1]});
		}
		
		return resultFormated;
	}

	private Integer[] getNumbers(REXP ids) throws NumberFormatException {
		if(ids.isString()) {
			return Arrays.stream(((REXPString)ids).asStrings())
						.map(
								id -> {
									if(id != null) {
										return Integer.valueOf(id);
									} else {
										return null;
									}
								}
						).toArray(Integer[]::new);
		} else {
			int[] idsAsArray = ((REXPInteger)ids).asIntegers();
			return (Integer[])Arrays.stream(idsAsArray).boxed().toArray(Integer[]::new);
		}
	}

	private int[] generateIds(int length) {
		return IntStream.range(0, length).toArray();
	}

	private String blockResultToString(REXP result) {
		REXPGenericVector vector = (REXPGenericVector) result;
		String[] unit1 = ((REXPString)vector.asList().get(0)).asStrings();
		String[] unit2 = ((REXPString)vector.asList().get(1)).asStrings();
		double[] distance = ((REXPDouble)vector.asList().get(2)).asDoubles(); 
		
		StringBuilder sb = new StringBuilder("\n");
		
		for(int i = 0 ; i < unit1.length ; i++) {
			sb.append(unit1[i]).append("     ").append(unit2[i]).append("     ").append(distance[i]).append("\n");
		}
		
		return sb.toString();
	}

	@Override
	public double kolmogorovSmirnovTest(double[] x, double[] y) {
		
		REXPDouble sampleX = new REXPDouble(x);
		REXPDouble sampleY = new REXPDouble(y);
		
		return engineProvider.ksTest(sampleX, sampleY);
	}

	@Override
	public double sqrt(double number) {
		return engineProvider.sqrt(number);
	}

}
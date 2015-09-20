package com.jfcorugedo.rserver.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public List<int[]> groupValues(double[] values) {
		
		REXPInteger rIds = new REXPInteger(generateIds(values.length));
		REXPDouble rValues = new REXPDouble(values);
		
		REXP result = engineProvider.blockFunction(rIds, rValues);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(blockResultToString(result));
		}
		return formatResult(result);
	}
	
	@Override
	public List<int[]> groupDiscreteValues(String[] values) {
		
		REXPInteger rIds = new REXPInteger(generateIds(values.length));
		REXPString rValues = new REXPString(values);
		
		REXP result = engineProvider.blockDiscreteFunction(rIds, rValues);
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug(blockResultToString(result));
		}
		return formatResult(result);
	}

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
			Integer[] result = (Integer[])Arrays.stream(idsAsArray).boxed().toArray(Integer[]::new);
			return result;
		}
	}

	private int[] generateIds(int length) {
		int[] ids = new int[length];
		for(int i = 0 ; i < length ; i++) {
			ids[i] = i;
		}
		return ids;
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
	public double sqrt(double number) {
		return engineProvider.sqrt(number);
	}
}

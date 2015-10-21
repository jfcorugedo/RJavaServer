package com.jfcorugedo.rserver.engine;

import java.util.List;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;

/**
 * Interface used to obtain access to the low-level engine
 */
public interface REngineProviderService {

	/**
	 * Executes R block function against a list of values.
	 * 
	 * @see https://cran.r-project.org/web/packages/blockTools/blockTools.pdf
	 * @param ids Identifiers associated to each value
	 * @param values Values used to perform the matching
	 * @return An object containing all the IDs grouped in pairs based on the distance of each one
	 */
	REXP blockFunction(REXPInteger ids, REXPDouble... values);

	/**
	 * Executes R block function against a list of discrete (non-numeric) values.
	 * 
	 * @see https://cran.r-project.org/web/packages/blockTools/blockTools.pdf
	 * @param ids Identifiers associated to each value
	 * @param values Values that must be grouped into pairs
	 * @return An object containing all the IDs grouped in pairs based on the distance of eachone
	 */
	REXP blockDiscreteFunction(REXPInteger ids, REXPString values);
	
	REXP blockGeneralFunction(REXPInteger ids, List<REXPString> discreteValues, List<REXPDouble> continuousValues);

	/**
	 * Calculates the sqare root of a given number
	 * @param number
	 * @return
	 */
	double sqrt(double number);
}

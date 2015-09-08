package com.jfcorugedo.rserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * This service uses REngine as a wrapper of JRIEngine.
 * 
 * It allows us to use some very useful methods (like REXP.createDataFrame()) that aren't available
 * using JRI classes directly
 * 
 * @author jfcorugedo
 *
 */
@Service
@Primary
public class REngineServiceImpl implements RService{

	private static final Logger LOG = LoggerFactory.getLogger(REngineServiceImpl.class);
	
	private REngine engine;
	
	public REngineServiceImpl() {
		setUpR();
	}
	
	public void setUpR() {
		// just making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
			LOG.error("** Version mismatch - Java files don't match library version.");
			throw new RuntimeException(String.format("Invalid versions. Rengine must have the same version of native library. Rengine version: %d. RNI library version: %d", Rengine.getVersion(), Rengine.rniGetVersion()));
		}
		
		// Enables debug traces
		Rengine.DEBUG = 1;
		
		LOG.info("Creating Rengine (with arguments)");
		// 1) we won't use the main loop at first, we'll start it later
		// (that's the "false" as second argument)
		// 2) no callback class will be used
		try {
			engine = REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new String[] { "--no-save" }, null, false);
			LOG.info("Rengine created");
		} catch(Exception e) {
			String message = "Unexpected exception while initializing R engine";
			LOG.error(message, e);
			throw new RuntimeException(message, e);
		}
		
	}
	
	@Override
	public Double calculateMean(List<Double> vector) throws Exception{
		String values = vector.stream().map(d -> d.toString()).collect(Collectors.joining(","));
		String rVector = String.format("c(%s)", values);
		REXP result = engine.parseAndEval(String.format("mean(%s)", rVector));
		return result.asDouble();
	}
}

package com.jfcorugedo.rserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingClass(name="com.jfcorugedo.rserver.service.REngineServiceImpl")
public class JRIEngineServiceImpl implements RService{

	private Rengine engine;
	
	public JRIEngineServiceImpl() {
		setUpR();
	}
	
	public void setUpR() {
		// just making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
			System.err.println("** Version mismatch - Java files don't match library version.");
			throw new RuntimeException(String.format("Invalid versions. Rengine must have the same version of native library. Rengine version: %d. RNI library version: %d", Rengine.getVersion(), Rengine.rniGetVersion()));
		}
		
		// Enables debug traces
		Rengine.DEBUG = 1;
		
		System.out.println("Creating Rengine (with arguments)");
		// 1) we pass the arguments from the command line
		// 2) we won't use the main loop at first, we'll start it later
		// (that's the "false" as second argument)
		// 3) no callback class will be used
		engine = new Rengine(new String[] { "--no-save" }, false, null);
		System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's
		// ready
		if (!engine.waitForR()) {
			throw new RuntimeException("Cannot load R");
		}
	}
	
	@Override
	public Double calculateMean(List<Double> vector) {
		String values = vector.stream().map(d -> d.toString()).collect(Collectors.joining(","));
		String rVector = String.format("c(%s)", values);
		REXP result = engine.eval(String.format("mean(%s)", rVector));
		return result.asDouble();
	}
}

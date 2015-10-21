package com.jfcorugedo.sample.rjava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineStdOutput;
import org.rosuda.REngine.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore("Maven can't execute this tests because they require a R environmet installed")
public class UseREngineInFrontOfJRIEngineIT {

	private static final Logger LOG = LoggerFactory.getLogger(UseREngineInFrontOfJRIEngineIT.class);
	
	private static REngine engine;
	
	@Test
	public void testAssignDataFrame() throws Exception{
		REXPDouble rIds = new REXPDouble(new double[]{1d,2d});
		REXPDouble rValues = new REXPDouble(new double[]{10000d,20000d});
		
		RList data = new RList();
		data.add(rIds);
		data.add(rValues);
		data.setKeyAt(0, "id");
		data.setKeyAt(1, "values");
		
		engine.assign("data", REXP.createDataFrame(data));
		
		REXP result = engine.parseAndEval("blockFunction(data,c(\"id\"),c(\"values\"))");
		
		
		System.out.println(result.toDebugString());
	}
	
	@Test
	public void testMean() throws Exception{

		
		/*
		 * High-level API - do not use RNI methods unless there is no other way
		 * to accomplish what you want
		 */
		engine.parseAndEval("rVector=c(1,2,3,4,5)");
		REXP result = engine.parseAndEval("meanVal=mean(rVector)");
		// generic vectors are RVector to accomodate names
		assertThat(result.asDouble()).isEqualTo(3.0);
	}
	
	@Test
	public void testSqrt() throws Exception{
		REXP result = engine.parseAndEval("sqrt(36)");
		assertThat(result.asDouble()).isEqualTo(6.0);
	}
	
	@BeforeClass
	public static void setUpR() throws Exception{
		// just making sure we have the right version of everything
		if (!Rengine.versionCheck()) {
			System.err.println("** Version mismatch - Java files don't match library version.");
			fail(String.format("Invalid versions. Rengine must have the same version of native library. Rengine version: %d. RNI library version: %d", Rengine.getVersion(), Rengine.rniGetVersion()));
		}
		
		// Enables debug traces
		Rengine.DEBUG = 1;
		
		System.out.println("Creating Rengine (with arguments)");
		// 1) we pass the arguments from the command line
		// 2) we won't use the main loop at first, we'll start it later
		// (that's the "false" as second argument)
		// 3) no callback class will be used
		engine = REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new String[] { "--no-save" }, new REngineStdOutput(), false);
		System.out.println("Rengine created...");
		
		REXP result = engine.parseAndEval("source(\"/Users/jfcorugedo/Documents/git/RJavaServer/src/test/resources/blockFunction.R\")");
		if(result == null) {
			LOG.error("blockFunction is not loaded!");
		} else {
			LOG.info("blockFunction loaded successfully");
		}
	}
}

package com.jfcorugedo.sample.rjava;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.Rengine;

/**
 * In order to make these test work, two parameters must be configured:
 * 
 * Inside VM args, add: 
 * -Djava.library.path=/Library/Frameworks/R.framework/Versions/3.2/Resources/library/rJava/jri/
 * 
 * Export an environment variable with this value:
 * 
 * R_HOME=/Library/Frameworks/R.framework/Resources
 * 
 * @author jfcorugedo
 *
 */
@Ignore("Maven can't execute this tests because they require a R environmet installed")
public class SimpleArithmeticCalculationsTest {

	private static Rengine engine = null;
	
	@BeforeClass
	public static void setUpR() {
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
		engine = new Rengine(new String[] { "--no-save" }, false, null);
		System.out.println("Rengine created, waiting for R");
		// the engine creates R is a new thread, so we should wait until it's
		// ready
		if (!engine.waitForR()) {
			fail("Cannot load R");
		}
	}
	
	@Test
	public void testMean() throws Exception{

		
		/*
		 * High-level API - do not use RNI methods unless there is no other way
		 * to accomplish what you want
		 */
		engine.eval("rVector=c(1,2,3,4,5)");
		REXP result = engine.eval("meanVal=mean(rVector)");
		// generic vectors are RVector to accomodate names
		assertThat(result.asDouble()).isEqualTo(3.0);
	}
	
	@Test
	public void testSqrt() {
		REXP result = engine.eval("sqrt(36)");
		assertThat(result.asDouble()).isEqualTo(6.0);
	}
}

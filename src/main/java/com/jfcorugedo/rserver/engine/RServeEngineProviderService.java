package com.jfcorugedo.rserver.engine;

import javax.annotation.PostConstruct;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RServeEngineProviderService extends REngineProviderBaseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RServeEngineProviderService.class);

	@PostConstruct//NOSONAR
	public void setUpR() {//NOSONAR
		
		try {
			//make sure JRI lib can be loaded (it must be present in java.library.path parameter)
			System.loadLibrary("jri");
			// just making sure we have the right version of everything
			if (!Rengine.versionCheck()) {
				LOGGER.error("** Version mismatch - Java files don't match library version.");
				LOGGER.error(String.format("Invalid versions. Rengine must have the same version of native library. Rengine version: %d. RNI library version: %d", Rengine.getVersion(), Rengine.rniGetVersion()));
			}
			
			// Enables debug traces
			Rengine.DEBUG = 1;
			
			LOGGER.info("Creating Rengine (with arguments)");
			
			// 1) we pass the arguments from the command line
			// 2) we won't use the main loop at first, we'll start it later
			// (that's the "false" as second argument)
			// 3) no callback class will be used
			setEngine(REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new String[] { "--no-save" }, new REngineStdOutCallback(LOGGER), false));
			LOGGER.info("Rengine created...");
			
			LOGGER.info("Loading blockFunction from " + getBlockFunction());
			REXP result = getEngine().parseAndEval(getBlockFunction());
			if(result == null) {
				LOGGER.error("blockFunction is not loaded!");
			} else {
				LOGGER.info("blockFunction loaded successfully");
			}
		} catch(Exception|UnsatisfiedLinkError e) {
			LOGGER.error("Unexpected error setting up R", e);
		}
	}
}

package com.jfcorugedo.rserver.engine;

import javax.annotation.PostConstruct;

import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("rJavaServer.rengine")
@ConditionalOnProperty(name="rJavaServer.rengine.enableJRI", havingValue="true")
public class JRIEngineProviderService implements REngineProviderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JRIEngineProviderService.class);

	private REngine engine;
	
	private String blockFunction;
	
	/**
	 * This method initializes REngine properly and make all the operations needed 
	 * to set up the environment.
	 * 
	 * This JRI implementation must load JRI library and starts JRIEngine
	 * 
	 */
	@PostConstruct
	public void setUpR() {//NOSONAR
		
		try {
			//make sure JRI lib can be loaded (it must be present in java.library.path parameter)
			//This line is necessary because Rengine.versionCheck() will execute a System.exit if
			//it can't load JRI library.
			System.loadLibrary("jri");
			// just making sure we have the right version of everything
			if (!Rengine.versionCheck()) {
				LOGGER.error("** Version mismatch - Java files don't match library version.");
				LOGGER.error(String.format("Invalid versions. Rengine must have the same version of native library. Rengine version: %d. RNI library version: %d", Rengine.getVersion(), Rengine.rniGetVersion()));
			}
			
			// Enables debug traces
			Rengine.DEBUG = 1;
			
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("Creating Rengine (with arguments)");
			}
			// 1) we pass the arguments from the command line
			// 2) we won't use the main loop at first, we'll start it later
			// (that's the "false" as second argument)
			// 3) no callback class will be used
			this.engine = REngine.engineForClass("org.rosuda.REngine.JRI.JRIEngine", new String[] { "--no-save" }, new REngineStdOutCallback(LOGGER), false);
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("Rengine created...");
				LOGGER.info("Loading blockFunction from " + getBlockFunction());
			}
			
			REXP result = engine.parseAndEval(getBlockFunction());
			if(result == null) {
				LOGGER.error("blockFunction is not loaded!");
			} else if(LOGGER.isInfoEnabled()) {
				LOGGER.info("blockFunction loaded successfully");
			}
		} catch(Exception|UnsatisfiedLinkError e) {
			LOGGER.error("Unexpected error setting up R", e);
		}
	}
	
	@Override
	public REXP blockFunction(REXPInteger ids, REXPDouble values) {
		
		RList data = new RList();
		data.add(ids);
		data.add(values);
		data.setKeyAt(0, "ids");
		data.setKeyAt(1, "values");
		
		try{
			synchronized(engine){
				engine.assign("data", REXP.createDataFrame(data));
				return engine.parseAndEval("blockFunction(data,c(\"ids\"),c(\"values\"))");
			}
		}catch(Exception e) {
			throw new REngineException("Unexpected error while executing blockFunction", e);
		}
	}
	
	@Override
	public REXP blockDiscreteFunction(REXPInteger ids, REXPString values) {
		RList data = new RList();
		data.add(ids);
		data.add(values);
		data.setKeyAt(0, "ids");
		data.setKeyAt(1, "values");
		
		try{
			synchronized(engine){
				engine.assign("data", REXP.createDataFrame(data));
				return engine.parseAndEval("blockDiscreteFunction(data,c(\"ids\"),c(\"values\"))");
			}
		}catch(Exception e) {
			throw new REngineException("Unexpected error while executing blockDiscreteFunction", e);
		}
	}
	
	@Override
	public double sqrt(double number) {
		try {
			synchronized(engine) {
				return engine.parseAndEval(String.format("sqrt(%f)", number)).asDouble();
			}
		} catch(Exception e) {
			throw new REngineException("Unexpected error while executing sqrt function", e);
		}
	}
	
	public String getBlockFunction() {
		return blockFunction;
	}

	public void setBlockFunction(String blockFunction) {
		this.blockFunction = blockFunction;
	}
}

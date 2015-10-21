package com.jfcorugedo.rserver.engine;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@ConfigurationProperties("rJavaServer.rengine")
@ConditionalOnProperty(name="rJavaServer.rengine.enableJRI", havingValue="false", matchIfMissing=true)
public class RServeEngineProviderService implements REngineProviderService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RServeEngineProviderService.class);

	/** Code returned by R script when everything goes ok */
	private static final int SUCCESS_CODE = 0;

	/** Full path to the rserve configuration file */
	private String rserveConf;
	
	/** Full path to the R executable file */
	private String rexe;
	
	/**
	 * This method initializes REngine properly and make all the operations needed 
	 * to set up the environment.
	 * 
	 * This RServe implementation must run R in a separate process and check the connection.
	 * 
	 */
	@PostConstruct
	public void setUpR() {//NOSONAR
		
		REngine engine = null;
		try {
	        if(LOGGER.isInfoEnabled()) {
	        	LOGGER.info("Starting RServe process...");
	        }
			ProcessBuilder builder = new ProcessBuilder("/bin/sh", "-c", String.format("echo 'library(Rserve);Rserve(FALSE,args=\"--no-save --slave --RS-conf %s\")'|%s --no-save --slave", rserveConf, rexe));
	    	builder.inheritIO();
	    	Process rProcess = builder.start();
	        
	    	if(LOGGER.isInfoEnabled()) {
	    		LOGGER.info("Waiting for Rserve to start...");
	    	}
	        int execCodeResult = rProcess.waitFor();
	        
	        if(execCodeResult != SUCCESS_CODE) {
	        	LOGGER.error(String.format("Unexpected error code starting RServe: %d", execCodeResult));
	        } else {
	        	LOGGER.error("RServe started successfully");
	        }
	        
	        if(LOGGER.isInfoEnabled()) {
	        	LOGGER.info("Opening connection to RServe daemon....");
	        }
	        engine = new RConnection();
	        if(LOGGER.isInfoEnabled()) {
	        	LOGGER.info(String.format("Obtaining R server version: %d", ((RConnection)engine).getServerVersion()));
	        }
			
		} catch(Exception e) {
			LOGGER.error("Unexpected error setting up RServe environment", e);
		} finally {
			closeRServeConnection(engine);
		}
	}

	private void closeRServeConnection(REngine engine) {
		if(engine != null) {
			if(!engine.close()) {
				LOGGER.warn("Unexpected error closing RServe connection");
			}
		}
	}
	
	@PreDestroy
	public void tearDown() {
		try {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("Shuting down Rserve daemon....");
			}
			RConnection rConnection = new RConnection();
			rConnection.shutdown();
			rConnection.close();
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info("Shutdown signal sent to Rserve daemon");
			}
		} catch(Exception e) {
			LOGGER.error("Unexpected error shuting down RServe", e);
		}
	}
	
	@Override
	public REXP blockFunction(REXPInteger ids, REXPDouble values) {
		
		RList data = new RList();
		data.add(ids);
		data.add(values);
		data.setKeyAt(0, "ids");
		data.setKeyAt(1, "values");
		
		REngine engine = null;
		try {
			engine = new RConnection();
			engine.assign("data", REXP.createDataFrame(data));
			return engine.parseAndEval("blockFunction(data,c(\"ids\"),c(\"values\"))");
		}catch(Exception e) {
			throw new REngineException("Unexpected error while executing blockFunction", e);
		} finally {
			closeRServeConnection(engine);
		}
	}
	
	@Override
	public REXP blockDiscreteFunction(REXPInteger ids, REXPString values) {
		RList data = new RList();
		data.add(ids);
		data.add(values);
		data.setKeyAt(0, "ids");
		data.setKeyAt(1, "values");
		
		REngine engine = null;
		try {
			engine = new RConnection();
			engine.assign("data", REXP.createDataFrame(data));
			return engine.parseAndEval("blockDiscreteFunction(data,c(\"ids\"),c(\"values\"))");
		}catch(Exception e) {
			throw new REngineException("Unexpected error while executing blockDiscreteFunction", e);
		} finally {
			closeRServeConnection(engine);
		}
	}
	
	@Override
	public double sqrt(double number) {
		REngine engine = null;
		try {
			engine = new RConnection();
			return engine.parseAndEval(String.format("sqrt(%f)", number)).asDouble();
		} catch(Exception e) {
			throw new REngineException("Unexpected error while executing sqrt function", e);
		} finally {
			closeRServeConnection(engine);
		}
	}

	public String getRserveConf() {
		return rserveConf;
	}

	public void setRserveConf(String rserveConf) {
		this.rserveConf = rserveConf;
	}

	public String getRexe() {
		return rexe;
	}

	public void setRexe(String rexe) {
		this.rexe = rexe;
	}
}

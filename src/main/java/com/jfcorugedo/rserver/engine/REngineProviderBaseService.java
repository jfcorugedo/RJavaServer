package com.jfcorugedo.rserver.engine;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;
import org.rosuda.REngine.RList;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("kmd.rengine")
public abstract class REngineProviderBaseService implements REngineProviderService {
	
	private REngine engine;
	
	private String blockFunction;
	
	/**
	 * This method must be implemented by all the subclasses. Each subclass must initialize
	 * REngine properly and make all the operations needed to set up the environment.
	 * 
	 * For instance, a JRI implementation must load JRI library and starts JRIEngine. On the other
	 * hand, RServe implementation must run R in a separate process and creates a connection.
	 * 
	 */
	public abstract void setUpR();
	
	@Override
	public REXP blockFunction(REXPInteger ids, REXPDouble values) {
		
		RList data = new RList();
		data.add(ids);
		data.add(values);
		data.setKeyAt(0, "ids");
		data.setKeyAt(1, "values");
		
		try{
			this.engine.assign("data", REXP.createDataFrame(data));
			return this.engine.parseAndEval("blockFunction(data,c(\"ids\"),c(\"values\"))");
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
			this.engine.assign("data", REXP.createDataFrame(data));
			return this.engine.parseAndEval("blockDiscreteFunction(data,c(\"ids\"),c(\"values\"))");
		}catch(Exception e) {
			throw new REngineException("Unexpected error while executing blockDiscreteFunction", e);
		}
	}
	
	@Override
	public double sqrt(double number) {
		try {
			return this.engine.parseAndEval(String.format("sqrt(%f)", number)).asDouble();
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

	protected REngine getEngine() {
		return engine;
	}

	protected void setEngine(REngine engine) {
		this.engine = engine;
	}
}

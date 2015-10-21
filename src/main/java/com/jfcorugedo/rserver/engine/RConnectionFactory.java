package com.jfcorugedo.rserver.engine;

import org.rosuda.REngine.REngine;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RConnectionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(RConnectionFactory.class);
	
	public RConnection getConnection() throws RserveException {
		return new RConnection();
	}
	
	public void releaseConnection(REngine engine) {
		if(engine != null) {
			if(!engine.close()) {
				LOGGER.warn("Unexpected error closing RServe connection");
			}
		}
	}
}

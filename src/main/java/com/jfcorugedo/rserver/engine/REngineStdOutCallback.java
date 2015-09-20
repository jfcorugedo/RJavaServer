package com.jfcorugedo.rserver.engine;

import org.rosuda.REngine.REngine;
import org.rosuda.REngine.REngineCallbacks;
import org.rosuda.REngine.REngineOutputInterface;
import org.slf4j.Logger;

public class REngineStdOutCallback implements REngineCallbacks, REngineOutputInterface{

	private Logger logger;
	
	public REngineStdOutCallback(Logger logger) {
		this.logger = logger;
	}
	@Override
	public void RWriteConsole(REngine eng, String text, int oType) {//NOSONAR
		if(oType == 0) {
			logger.info(text);
		} else {
			logger.warn(text);
		}
	}

	@Override
	public void RShowMessage(REngine eng, String text) {//NOSONAR
		logger.error(text);
	}

	@Override
	public void RFlushConsole(REngine eng) {//NOSONAR
		
	}
}

package com.jfcorugedo.rserver.engine;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.rosuda.REngine.REngine;
import org.slf4j.Logger;

public class REngineStdOutCallbackTest {

	@Test
	public void testPrintInfoMessage() {
		
		Logger loggerMock = mock(Logger.class);
		REngineStdOutCallback stdoutCallback = new REngineStdOutCallback(loggerMock);
		
		stdoutCallback.RWriteConsole(mock(REngine.class), "Something has happened", 0);
		
		verify(loggerMock, times(1)).info("Something has happened");
	}
	
	@Test
	public void testPrintWarnMessage() {
		
		Logger loggerMock = mock(Logger.class);
		REngineStdOutCallback stdoutCallback = new REngineStdOutCallback(loggerMock);
		
		stdoutCallback.RWriteConsole(mock(REngine.class), "Something goes wrong", 1);
		
		verify(loggerMock, times(1)).warn("Something goes wrong");
	}
	
	@Test
	public void testPrintErrorMessage() {
		
		Logger loggerMock = mock(Logger.class);
		REngineStdOutCallback stdoutCallback = new REngineStdOutCallback(loggerMock);
		
		stdoutCallback.RShowMessage(mock(REngine.class), "Something goes really wrong");
		
		verify(loggerMock, times(1)).error("Something goes really wrong");
	}
	
	@Test
	public void testFlushConsole() {
		
		Logger loggerMock = mock(Logger.class);
		REngineStdOutCallback stdoutCallback = new REngineStdOutCallback(loggerMock);
		
		stdoutCallback.RFlushConsole(mock(REngine.class));
		
		verify(loggerMock, never()).info(anyString());
		verify(loggerMock, never()).warn(anyString());
		verify(loggerMock, never()).error(anyString());
	}
}

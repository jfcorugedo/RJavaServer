package com.jfcorugedo.rserver.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REngine;

public class JRIEngineProviderServiceTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private REngine engine;
	
	@InjectMocks
	private REngineProviderService service = new JRIEngineProviderService();

	@Test(expected=REngineException.class)
	public void testThrowsEngineExceptionWhenSomethingGoesWrong() throws Exception {
		
		when(engine.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		
		service.blockFunction(mock(REXPInteger.class), mock(REXPDouble.class));
	}
	
	@Test
	public void testReturnResult() throws Exception {
		REXP expectedResult = mock(REXP.class);
		when(engine.parseAndEval(anyString())).thenReturn(expectedResult);
		
		REXP result = service.blockFunction(mock(REXPInteger.class), mock(REXPDouble.class));
		
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(expectedResult);
	}
	
	@Test(expected=REngineException.class)
	public void testThrowsEngineExceptionWhenSomethingGoesWrongInBlockDiscrete() throws Exception {
		
		when(engine.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		
		service.blockDiscreteFunction(mock(REXPInteger.class), mock(REXPString.class));
	}
	
	@Test
	public void testReturnResultBlockDiscreteFunction() throws Exception {
		REXP expectedResult = mock(REXP.class);
		when(engine.parseAndEval(anyString())).thenReturn(expectedResult);
		
		REXP result = service.blockDiscreteFunction(mock(REXPInteger.class), mock(REXPString.class));
		
		assertThat(result).isNotNull();
		assertThat(result).isEqualTo(expectedResult);
	}
	
	@Test(expected=REngineException.class)
	public void testThrowsEngineExceptionWhenSomethingGoesWrongCallingSqrt() throws Exception {
		
		when(engine.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		
		service.sqrt(4d);
	}
	
	@Test
	public void testReturnResultWhenCallingSqrt() throws Exception {
		REXP expectedResult = new REXPDouble(2d);
		when(engine.parseAndEval(anyString())).thenReturn(expectedResult);
		
		double result = service.sqrt(4d);
		
		assertThat(result).isEqualTo(2d);
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void ksTestIsNotImplementedYet() {
		
		service.ksTest(mock(REXPDouble.class), mock(REXPDouble.class));
	}
}

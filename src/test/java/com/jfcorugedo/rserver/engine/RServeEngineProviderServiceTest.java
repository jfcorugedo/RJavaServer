package com.jfcorugedo.rserver.engine;

import static com.jfcorugedo.rserver.common.collection.CollectionUtils.newList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;

public class RServeEngineProviderServiceTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private RConnectionFactory rConnectionFactory;
	
	@InjectMocks
	private RServeEngineProviderService rServeEngineProviderService;
	
	@Test
	public void blockGeneralFunction() throws Exception{
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockGeneralFunction(getDemoIds(), getDemoDiscreteVars(), getDemoContinuousVars());
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("blockGeneralFunction(data,c(\"ids\"),c(\"discreteValues0\"),c(\"continuousValues0\"))");
	}

	private List<REXPString> getDemoDiscreteVars() {
		
		return newList(new REXPString(new String[]{"A", "B", "B", "A"}));
	}

	private List<REXPDouble> getDemoContinuousVars() {
		
		return newList(new REXPDouble(new double[]{1.0, 2.0, 3.0, 4.0}));
	}

	private REXPInteger getDemoIds() {
		
		return new REXPInteger(new int[]{0,1,2,3});
	}
	
	@Test(expected=REngineException.class)
	public void blockGeneralFunctionUnexpectedError() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockGeneralFunction(getDemoIds(), getDemoDiscreteVars(), getDemoContinuousVars());
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		verify(rConnectionMock, times(1)).parseAndEval(anyString());
	}
	
	@Test
	public void blockFunction() throws Exception{
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockFunction(getDemoIds(), getDemoContinuousVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("blockFunction(data,c(\"ids\"),c(\"values0\"))");
	}
	
	@Test
	public void blockFunctionUsingMoreThanOneVariable() throws Exception{
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockFunction(getDemoIds(), getDemoContinuousVars().get(0), getDemoContinuousVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("blockFunction(data,c(\"ids\"),c(\"values0\",\"values1\"))");
	}
	
	@Test(expected=REngineException.class)
	public void blockFunctionUnexpectedError() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockFunction(getDemoIds(), getDemoContinuousVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		verify(rConnectionMock, times(1)).parseAndEval(anyString());
	}
	
	@Test
	public void blockDiscreteFunction() throws Exception{
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockDiscreteFunction(getDemoIds(), getDemoDiscreteVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("blockDiscreteFunction(data,c(\"ids\"),c(\"values0\"))");
	}
	
	@Test
	public void blockDiscreteFunctionUsingMoreThanOneVariable() throws Exception{
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockDiscreteFunction(getDemoIds(), getDemoDiscreteVars().get(0), getDemoDiscreteVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("blockDiscreteFunction(data,c(\"ids\"),c(\"values0\",\"values1\"))");
	}
	
	@Test(expected=REngineException.class)
	public void blockDiscreteFunctionUnexpectedError() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenThrow(new RuntimeException("Something goes wrong..."));
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.blockDiscreteFunction(getDemoIds(), getDemoDiscreteVars().get(0));
		
		verify(rConnectionMock, times(1)).assign(eq("data"), any(REXP.class));
		verify(rConnectionMock, times(1)).parseAndEval(anyString());
	}
	
	@Test
	public void ksTest() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenReturn(new REXPGenericVector(new RList(new REXP[]{new REXPDouble(new double[]{0.15})})));
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		REXPDouble demoVars = getDemoContinuousVars().get(0);
		
		rServeEngineProviderService.ksTest(demoVars, demoVars);
		
		verify(rConnectionMock, times(1)).assign(eq("x"), any(REXP.class));
		verify(rConnectionMock, times(1)).assign(eq("y"), any(REXP.class));
		ArgumentCaptor<String> rCommand = ArgumentCaptor.forClass(String.class);
		verify(rConnectionMock, times(1)).parseAndEval(rCommand.capture());
		assertThat(rCommand.getValue()).isEqualTo("ks.test(x, y)['p.value']");
	}
	
	@Test(expected=REngineException.class)
	public void ksTestThrowsException() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenThrow(new RuntimeException());
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		REXPDouble demoVars = getDemoContinuousVars().get(0);
		
		rServeEngineProviderService.ksTest(demoVars, demoVars);
	}
	
	@Test
	public void testSqrt() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval("sqrt(4.000000)")).thenReturn(new REXPDouble(2.0));
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		double result = rServeEngineProviderService.sqrt(4.0);
		
		assertThat(result).isEqualTo(2.0);
	}
	
	@Test(expected=REngineException.class)
	public void testSqrtThrowsException() throws Exception{
		
		RConnection rConnectionMock = mock(RConnection.class);
		when(rConnectionMock.parseAndEval(anyString())).thenThrow(new RuntimeException());
		when(rConnectionFactory.getConnection()).thenReturn(rConnectionMock);
		
		rServeEngineProviderService.sqrt(4.0);
	}
}

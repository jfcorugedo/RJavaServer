package com.jfcorugedo.rserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
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

import com.jfcorugedo.rserver.engine.REngineProviderService;

public class RServiceImplTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private REngineProviderService engineProvider;
	
	@InjectMocks
	private RServiceImpl service = new RServiceImpl();
	
	@Test
	public void testBlockFunction() throws Exception{
		
		when(engineProvider.blockFunction(any(REXPInteger.class), any(REXPDouble.class))).thenReturn(getTestReturnValue());
		
		List<int[]> result = service.groupValues(new double[]{1d,2d,3d,4d});
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
	}

	private REXP getTestReturnValue() {
		RList values = new RList();
		values.add(new REXPString(new String[]{"1","2"}));
		values.add(new REXPString(new String[]{"3","4"}));
		values.add(new REXPDouble(new double[]{0.098,1.234}));
		REXPGenericVector vector = new REXPGenericVector(values);
		
		return vector;
	}
	
	@Test
	public void testBlockFunctionWithOddNumberOfValues() throws Exception{
		
		when(engineProvider.blockFunction(any(REXPInteger.class), any(REXPDouble.class))).thenReturn(getTestReturnOddValues());
		
		List<int[]> result = service.groupValues(new double[]{1d,2d,3d,4d,5d});
		
		assertThat(result).hasSize(3);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4}, new int[]{5});
	}
	
	private REXP getTestReturnOddValues() {
		RList values = new RList();
		values.add(new REXPString(new String[]{"1","2","5"}));
		values.add(new REXPString(new String[]{"3","4",null}));
		values.add(new REXPDouble(new double[]{0.098,1.234, Double.NaN}));
		REXPGenericVector vector = new REXPGenericVector(values);
		
		return vector;
	}
}

package com.jfcorugedo.rserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.jfcorugedo.rserver.common.collection.CollectionUtils.newList;

import java.util.ArrayList;
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
		
		List<int[]> result = service.groupValues(newList(new double[]{1d,2d,3d,4d}));
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
	}
	
	@Test
	public void testBlockFunctionWithSeveralVariables() throws Exception{
		
		when(engineProvider.blockFunction(any(REXPInteger.class), anyVararg())).thenReturn(getTestReturnValue());
		
		List<int[]> result = service.groupValues(newList(new double[]{1d,2d,3d,4d}, new double[]{-1d,-2d,-3d,-4d}));
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
		ArgumentCaptor<REXPDouble> continuousValues = ArgumentCaptor.forClass(REXPDouble.class);
		verify(engineProvider, times(1)).blockFunction(any(REXPInteger.class), continuousValues.capture());
		assertThat(continuousValues.getAllValues().get(0).asDoubles()).containsExactly(1,2,3,4);
		assertThat(continuousValues.getAllValues().get(1).asDoubles()).containsExactly(-1,-2,-3,-4);
	}
	
	@Test
	public void testBlockDiscreteFunction() throws Exception{
		
		when(engineProvider.blockDiscreteFunction(any(REXPInteger.class), any(REXPString.class))).thenReturn(getTestReturnValue());
		
		List<int[]> result = service.groupDiscreteValues(buildList(new String[]{"A","B","C","D"}));
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
		ArgumentCaptor<REXPString> discreteValues = ArgumentCaptor.forClass(REXPString.class);
		verify(engineProvider, times(1)).blockDiscreteFunction(any(REXPInteger.class), discreteValues.capture());
		assertThat(discreteValues.getValue().asStrings()).containsExactly("A", "B", "C", "D");
	}
	
	@Test
	public void testBlockDiscreteFunctionWithSeveralVariables() throws Exception{
		
		when(engineProvider.blockDiscreteFunction(any(REXPInteger.class), anyVararg())).thenReturn(getTestReturnValue());
		
		List<int[]> result = service.groupDiscreteValues(newList(new String[]{"A", "B", "C", "D"}, new String[]{"E", "F", "G", "H"}));
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
		ArgumentCaptor<REXPString> discreteValues = ArgumentCaptor.forClass(REXPString.class);
		verify(engineProvider, times(1)).blockDiscreteFunction(any(REXPInteger.class), discreteValues.capture());
		assertThat(discreteValues.getAllValues().get(0).asStrings()).containsExactly("A", "B", "C", "D");
		assertThat(discreteValues.getAllValues().get(1).asStrings()).containsExactly("E", "F", "G", "H");
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
		
		List<int[]> result = service.groupValues(newList(new double[]{1d,2d,3d,4d,5d}));
		
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
	
	@Test
	public void testBlockFunctionUsingOneDiscreteAndOneContinuousVariableCallsEngine() {
		
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		List<int[]> result = service.groupMultipleValues(
													buildList(new String[]{"A", "B", "C", "D"}), 
													newList(new double[]{1d,2d,3d,4d})
												);
		
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												any(REXPInteger.class), 
												anyListOf(REXPString.class), 
												anyListOf(REXPDouble.class)
											);
		
		assertThat(result).hasSize(2);
		assertThat(result).containsExactly(new int[]{1,3}, new int[]{2,4});
	}

	private void configureMockEngineBlockGeneralFunctionToReturnEvenResults() {
		when(
			engineProvider
				.blockGeneralFunction(any(REXPInteger.class), 
						              anyListOf(REXPString.class), 
						              anyListOf(REXPDouble.class))
			).thenReturn(getTestReturnValue());
	}
	
	@Test
	public void testBlockFunctionUsingOneDiscreteAndOneContinuousVariableGeneratesIds() {
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		service.groupMultipleValues(
										buildList(new String[]{"A", "B", "C", "D"}), 
										newList(new double[]{1d,2d,3d,4d})
									);
		
		ArgumentCaptor<REXPInteger> ids = ArgumentCaptor.forClass(REXPInteger.class);
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												ids.capture(), 
												anyListOf(REXPString.class), 
												anyListOf(REXPDouble.class)
											);
		
		assertThat(ids.getAllValues()).hasSize(1);
		assertThat(ids.getValue().asIntegers()).containsExactly(0, 1, 2, 3);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testBlockFunctionUsingOneDiscreteAndOneContinuousVariableGeneratesDiscreteNativeValues() {
		
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		service.groupMultipleValues(
										buildList(new String[]{"A", "B", "C", "D"}), 
										newList(new double[]{1d,2d,3d,4d})
									);
		
		ArgumentCaptor<List> discreteValues = ArgumentCaptor.forClass(List.class);
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												any(REXPInteger.class), 
												discreteValues.capture(), 
												anyListOf(REXPDouble.class)
											);
		
		assertThat(discreteValues.getAllValues()).hasSize(1);
		assertThat(discreteValues.getValue()).hasSize(1);
		assertThat(discreteValues.getValue().get(0)).isInstanceOf(REXPString.class);
		assertThat(((REXPString)discreteValues.getValue().get(0)).asStrings()).containsExactly("A","B","C","D");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testBlockFunctionUsingOneDiscreteAndOneContinuousVariableGeneratesContinuousNativeValues() {
		
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		service.groupMultipleValues(
										buildList(new String[]{"A", "B", "C", "D"}), 
										newList(new double[]{1d,2d,3d,4d})
									);
		
		ArgumentCaptor<List> continuousValues = ArgumentCaptor.forClass(List.class);
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												any(REXPInteger.class), 
												anyListOf(REXPString.class), 
												continuousValues.capture()
											);
		
		assertThat(continuousValues.getAllValues()).hasSize(1);
		assertThat(continuousValues.getValue()).hasSize(1);
		assertThat(continuousValues.getValue().get(0)).isInstanceOf(REXPDouble.class);
		assertThat(((REXPDouble)continuousValues.getValue().get(0)).asDoubles()).containsExactly(1,2,3,4);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testBlockFunctionUsingSeveralDiscreteAndOneContinuousVariableGeneratesDiscreteNativeValues() {
		
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		service.groupMultipleValues(
										buildList(
												new String[]{"A", "B", "C", "D"}, 
												new String[]{"10", "20", "30", "40"}
										), 
										newList(new double[]{1d,2d,3d,4d})
									);
		
		ArgumentCaptor<List> discreteValues = ArgumentCaptor.forClass(List.class);
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												any(REXPInteger.class), 
												discreteValues.capture(), 
												anyListOf(REXPDouble.class)
											);
		
		assertThat(discreteValues.getAllValues()).hasSize(1);
		assertThat(discreteValues.getValue()).hasSize(2);
		assertThat(discreteValues.getValue().get(0)).isInstanceOf(REXPString.class);
		assertThat(((REXPString)discreteValues.getValue().get(0)).asStrings()).containsExactly("A","B","C","D");
		assertThat(((REXPString)discreteValues.getValue().get(1)).asStrings()).containsExactly("10","20","30","40");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testBlockFunctionUsingOneDiscreteAndSeveralContinuousVariableGeneratesContinuousNativeValues() {
		
		configureMockEngineBlockGeneralFunctionToReturnEvenResults();
		
		service.groupMultipleValues(
										buildList(new String[]{"A", "B", "C", "D"}), 
										newList(
												new double[]{1d,2d,3d,4d},
												new double[]{-1d,-2d,-3d,-4d}
												)
									);
		
		ArgumentCaptor<List> continuousValues = ArgumentCaptor.forClass(List.class);
		verify(engineProvider, times(1))
						.blockGeneralFunction(
												any(REXPInteger.class), 
												anyListOf(REXPString.class), 
												continuousValues.capture()
											);
		
		assertThat(continuousValues.getAllValues()).hasSize(1);
		assertThat(continuousValues.getValue()).hasSize(2);
		assertThat(continuousValues.getValue().get(0)).isInstanceOf(REXPDouble.class);
		assertThat(((REXPDouble)continuousValues.getValue().get(0)).asDoubles()).containsExactly(1,2,3,4);
		assertThat(((REXPDouble)continuousValues.getValue().get(1)).asDoubles()).containsExactly(-1,-2,-3,-4);
	}
	
	@Test
	public void testKolmogorovSmirnovTest() {
		
		when(engineProvider.ksTest(any(REXPDouble.class), any(REXPDouble.class))).thenReturn(1.0);
		
		double pValue = service.kolmogorovSmirnovTest(new double[]{1d,2d,3d,4d}, new double[]{1d,2d,3d,4d});
		
		assertThat(pValue).isGreaterThan(0.05);
	}
	
	
	private List<String[]> buildList(String[]... strings) {
		List<String[]> result = new ArrayList<String[]>();
		for(String[] value : strings) {
			result.add(value);
		}
		return result;
	}
}

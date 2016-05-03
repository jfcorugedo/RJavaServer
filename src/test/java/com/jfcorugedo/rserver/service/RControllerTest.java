package com.jfcorugedo.rserver.service;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jfcorugedo.rserver.exception.BadRequestException;
import com.jfcorugedo.rserver.generalization.algorithm.KolmogorovSmirnovTest;

import static com.jfcorugedo.rserver.common.collection.CollectionUtils.newList;

public class RControllerTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Mock
	private RService rService;
	
	@Mock
	private KolmogorovSmirnovTest ksTest;
	
	@InjectMocks
	private RController controller = new RController();
	
	@Test(expected=BadRequestException.class)
	public void testThrowsExceptionWhenNullInput() {
		
		controller.block(null);
	}
	
	@Test
	public void testCallBlock() {
		
		List<double[]> values = newList(new double[]{1d,2d});
		controller.block(values);
		
		verify(rService, times(1)).groupValues(values);
	}
	
	@Test(expected=BadRequestException.class)
	public void testBlockDiscreteThrowsExceptionWhenNullInput() {
		
		controller.blockDiscrete(null);
	}
	
	@Test
	public void testCallBlockDiscrete() {
		
		List<String[]> values = buildList(new String[]{"a", "b", "c"});
		controller.blockDiscrete(values);
		
		verify(rService, times(1)).groupDiscreteValues(values);
	}
	
	@Test
	public void testCallSqrt() {
		
		controller.sqrt(4d);
		
		verify(rService, times(1)).sqrt(4d);
	}
	
	private List<String[]> buildList(String[]... strings) {
		List<String[]> result = new ArrayList<String[]>();
		for(String[] value : strings) {
			result.add(value);
		}
		return result;
	}
	
	@Test
	public void areGeneralizableCallsKSTest() {
	    
	    double[] sampleA = new double[]{1d, 2d};
	    double[] sampleB = new double[]{3d, 4d};
	    
	    controller.areGeneralizable(newList(sampleA, sampleB));
	    
	    verify(ksTest, times(1)).areGeneralizable(sampleA, sampleB);
	}
	
	@Test
	public void areGeneralizableRetursResult() {
	    
	    when(ksTest.areGeneralizable(any(), any())).thenReturn(true);
        when(ksTest.getAlgorithm()).thenReturn("K-S test");
        
        ResponseEntity<String> result = controller.areGeneralizable(newList(new double[]{1d, 2d}, new double[]{1d, 2d}));
        
        assertThat(result.getBody()).isEqualTo("K-S test: true");
	}
	
	@Test
	public void areGeneralizableReturnsBadRequestIfInputDoesNotHaveEnoughValues() {
	    
	    ResponseEntity<String> result = controller.areGeneralizable(newList(new double[]{1d, 2d}));
	    
	    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	    assertThat(result.getBody()).isEqualTo("This algorithm needs two different list of values");
	}
}

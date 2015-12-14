package com.jfcorugedo.rserver.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.jfcorugedo.rserver.exception.BadRequestException;
import static com.jfcorugedo.rserver.common.collection.CollectionUtils.newList;

public class RControllerTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Mock
	private RService rService;
	
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
}

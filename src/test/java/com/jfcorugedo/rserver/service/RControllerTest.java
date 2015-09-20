package com.jfcorugedo.rserver.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.jfcorugedo.rserver.exception.BadRequestException;

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
	public void testCallRService() {
		
		double[] values = new double[]{1d,2d};
		controller.block(values);
		
		verify(rService, times(1)).groupValues(values);
	}
}

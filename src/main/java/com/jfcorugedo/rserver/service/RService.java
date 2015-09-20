package com.jfcorugedo.rserver.service;

import java.util.List;


public interface RService {

	List<int[]> groupValues(double[] values);

	List<int[]> groupDiscreteValues(String[] values);
	
	double sqrt(double number);

}

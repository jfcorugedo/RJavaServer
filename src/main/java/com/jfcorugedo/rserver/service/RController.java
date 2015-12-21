package com.jfcorugedo.rserver.service;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.jfcorugedo.rserver.exception.BadRequestException;
import com.jfcorugedo.rserver.generalization.algorithm.KolmogorovSmirnovTest;


@RestController
public class RController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RController.class);
			
	@Inject
	private RService rService;
	
	@Inject
	private KolmogorovSmirnovTest ksTest;
	
	@RequestMapping(value="/stratification/continuous/group", method=POST)
	public ResponseEntity<List<int[]>> block(@RequestBody(required=false) List<double[]> data) {
		if(data != null) {
			return new ResponseEntity<List<int[]>>(rService.groupValues(data), HttpStatus.CREATED);
		} else {
			throw new BadRequestException("This service needs all the values of the stratification variable");
		}
	}
	
	@RequestMapping(value="/stratification/discrete/group", method=POST)
	public ResponseEntity<List<int[]>> blockDiscrete(@RequestBody(required=false) List<String[]> data) {
		if(data != null) {
			return new ResponseEntity<List<int[]>>(rService.groupDiscreteValues(data), HttpStatus.CREATED);
		} else {
			throw new BadRequestException("This service needs all the values of the stratification variable");
		}
	}
	
	@RequestMapping(value="/ks-test", method=POST)
	@Timed
	public ResponseEntity<String> areGeneralizable(@RequestBody List<double[]> values) {

		if(values.size() == 2) {
			boolean areGeneralizable = ksTest.areGeneralizable(values.get(0), values.get(1));
			
			return new ResponseEntity<String>(String.format("%s: %b", ksTest.getAlgorithm(), areGeneralizable), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<String>("This algorithm needs two different list of values", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value="/sqrt", method=POST)
	public ResponseEntity<Double> sqrt(@RequestBody Double number) {
		
		return new ResponseEntity<Double>(rService.sqrt(number), HttpStatus.CREATED);
	}
}

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

import com.jfcorugedo.rserver.exception.BadRequestException;


@RestController
@RequestMapping("stratification")
public class RController {

	public static final Logger LOGGER = LoggerFactory.getLogger(RController.class);
			
	@Inject
	private RService rService;
	
	@RequestMapping(value="/continuous/group", method=POST)
	public ResponseEntity<List<int[]>> block(@RequestBody(required=false) double[] data) {
		if(data != null) {
			return new ResponseEntity<List<int[]>>(rService.groupValues(data), HttpStatus.CREATED);
		} else {
			throw new BadRequestException("This service needs all the values of the stratification variable");
		}
	}
	
	@RequestMapping(value="/discrete/group", method=POST)
	public ResponseEntity<List<int[]>> blockDiscrete(@RequestBody(required=false) String[] data) {
		if(data != null) {
			return new ResponseEntity<List<int[]>>(rService.groupDiscreteValues(data), HttpStatus.CREATED);
		} else {
			throw new BadRequestException("This service needs all the values of the stratification variable");
		}
	}
	
	@RequestMapping(value="/sqrt", method=POST)
	public ResponseEntity<Double> sqrt(double number) {
		
		return new ResponseEntity<Double>(rService.sqrt(number), HttpStatus.CREATED);
	}
}

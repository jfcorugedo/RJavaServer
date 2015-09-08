package com.jfcorugedo.rserver;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jfcorugedo.rserver.service.RService;

@SpringBootApplication
@RestController
public class Application {

	private static final Logger LOG = LoggerFactory.getLogger(Application.class);
	
	@Inject
	private RService rService;
	
	public static void main(String[] args) {
        LOG.debug("\n*************\n* rJava POC *\n*************\n");
        
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        
        app.run(args);
    }
	
	@RequestMapping(value="/mean", method=POST)
	public Double mean(@RequestBody List<Double> vector) throws Exception{
		return rService.calculateMean(vector);
	}
}

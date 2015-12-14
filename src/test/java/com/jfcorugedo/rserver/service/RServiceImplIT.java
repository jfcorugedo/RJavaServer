package com.jfcorugedo.rserver.service;

import static org.assertj.core.api.Assertions.assertThat;
import static com.jfcorugedo.rserver.common.collection.CollectionUtils.newList;

import java.util.Random;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jfcorugedo.rserver.Application;

@Ignore("These tests requires R environment installed")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
@ActiveProfiles(profiles={"local", "integrationtest"})
public class RServiceImplIT {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RServiceImplIT.class);

	public static final double[] TEST_POPULATION = new double[20];
	static{
		TEST_POPULATION[0] = 10;
		TEST_POPULATION[1] = 5;
		TEST_POPULATION[2] = 20;
		TEST_POPULATION[3] = 12;
		TEST_POPULATION[4] = 4;
		TEST_POPULATION[5] = 11;
		TEST_POPULATION[6] = 23;
		TEST_POPULATION[7] = 0;
		TEST_POPULATION[8] = 12;
		TEST_POPULATION[9] = 6;
		TEST_POPULATION[10] = 6;
		TEST_POPULATION[11] = 11;
		TEST_POPULATION[12] = 14;
		TEST_POPULATION[13] = 13;
		TEST_POPULATION[14] = 16;
		TEST_POPULATION[15] = 18;
		TEST_POPULATION[16] = 19;
		TEST_POPULATION[17] = 12;
		TEST_POPULATION[18] = 0;
		TEST_POPULATION[19] = 4;
	}
	
	public static final double[] TEST_BIG_POPULATION = new double[200];
	static{
		for(int i = 0 ; i < 10 ; i++) {
			for(int j = 0 ; j < 20 ; j++) {
				TEST_BIG_POPULATION[i*20+j] = TEST_POPULATION[j];
			}
		}
	}
	
	@Inject
	private RServiceImpl service;
	
	@Test
	public void testBlockFunction() {
		
		for(int i = 0 ; i < 2 ; i++) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info(Integer.toString(i));
			}
			service.groupValues(newList(TEST_BIG_POPULATION));
		}
	}
	
	@Test
	public void testBlockFunctionWithRandom() {
		
		for(int i = 0 ; i < 2 ; i++) {
			if(LOGGER.isInfoEnabled()) {
				LOGGER.info(Integer.toString(i));
			}
			service.groupValues(newList(new Random().doubles(200).map(v -> v*20).toArray()));
		}
	}
	
	@Test
	public void ksTest() {
		
		double[] x = new double[]{ 1.0, 3.0, 50.0};
		double[] y = new double[]{ 2.0, 4.0, 90.0, 34.1};
		
		double pValue = service.kolmogorovSmirnovTest(x, y);
		
		assertThat(pValue).isGreaterThan(0.80);
	}
}

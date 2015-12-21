package com.jfcorugedo.rserver.metrics;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.DatadogReporter.Expansion;
import org.coursera.metrics.datadog.transport.HttpTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.codahale.metrics.MetricRegistry;

/**
 * This bean will create and configure a DatadogReporter that will be in charge of sending
 * all the metrics collected by Spring Boot actuator system to Datadog.
 * 
 * @see https://www.datadoghq.com/
 * @author jfcorugedo
 *
 */
@Configuration
@ConfigurationProperties("rJavaServer.metrics")
@Profile({ "!local"})
public class DatadogReporterConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatadogReporterConfig.class);
	
	/** Datadog API key used to authenticate every request to Datadog API */
	private String apiKey;
	
	/** Logical name associated to all the events send by this application */
	private String host;
	
	/** Time, in seconds, between every call to Datadog API. The lower this value the more information will be send to Datadog */
	private long period;
	
	@Bean
	@Autowired
	public DatadogReporter datadogReporter(MetricRegistry registry) {
		
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("Initializing Datadog reporter using [ host: {}, period(seconds):{}, api-key:{} ]", getHost(), getPeriod(), getApiKey());
		}
		
		EnumSet<Expansion> expansions = DatadogReporter.Expansion.ALL;
		HttpTransport httpTransport = new HttpTransport
									.Builder()
									.withApiKey(getApiKey())
									.build();
		
		DatadogReporter reporter = DatadogReporter.forRegistry(registry)
		  .withHost(getHost())
		  .withTransport(httpTransport)
		  .withExpansions(expansions)
		  .build();

		reporter.start(getPeriod(), TimeUnit.SECONDS);
		
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("Datadog reporter successfully initialized");
		}
		
		return reporter;
	}

	/**
	 * @return Datadog API key used to authenticate every request to Datadog API
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey Datadog API key used to authenticate every request to Datadog API
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return Logical name associated to all the events send by this application
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host Logical name associated to all the events send by this application
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Time, in seconds, between every call to Datadog API. The lower this value the more information will be send to Datadog
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * @param period Time, in seconds, between every call to Datadog API. The lower this value the more information will be send to Datadog
	 */
	public void setPeriod(long period) {
		this.period = period;
	}
}


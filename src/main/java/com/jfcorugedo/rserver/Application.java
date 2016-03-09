package com.jfcorugedo.rserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;

@SpringBootApplication
@EnableMetrics
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
        LOGGER.info("\n****************\n* rJava Server *\n****************\n");
        
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
        SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
    	
        // Check if the selected profile has been set as argument.
        // if not the development profile will be added
        addDefaultProfile(app, source);
        
        
        
        app.run(args);
    }
	
	/**
     * Set a default profile if it has not been set
     */
    private static void addDefaultProfile(SpringApplication app, SimpleCommandLinePropertySource source) {
        if (!source.containsProperty("spring.profiles.active")) {
            app.setAdditionalProfiles("local");
            LOGGER.info("Staring application with profiles: local");
        } else {
            LOGGER.info("Staring application with profiles: {}", source.getProperty("spring.profiles.active"));
        }
    }
    
    /**
     * This method is needed because Sonar doesn't allow public constructors in a class with only static methods,
     * but SpringBoot needs a public constructor in this class.
     * This method should not be invoked
     */
    public void avoidSonarViolation(){
    	LOGGER.warn("This method should not be invoked");
    }
}

package com.jfcorugedo.rserver.engine;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.rosuda.REngine.REngine;

public class RConnectionFactoryTest {

    private RConnectionFactory connectionFactory = new RConnectionFactory();
    
    @Test
    public void releaseConnectionTriesToCloseConnection() {
        
        REngine engine = mock(REngine.class);
        
        connectionFactory.releaseConnection(engine);
        
        verify(engine, times(1)).close();
    }
}

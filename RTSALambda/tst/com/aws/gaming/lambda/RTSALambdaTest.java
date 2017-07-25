package com.aws.gaming.lambda;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;


import com.amazonaws.services.lambda.runtime.Context;
import com.aws.gaming.lambda.RTSALambda.RTSAResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RTSALambdaTest {

    private static RegisterUserRequest goodInput;
    private static RegisterUserRequest badInput;

    @BeforeClass
    public static void createInput() throws IOException {
        goodInput = new RegisterUserRequest("testUser","password123","testUser@email.com");
        badInput = new RegisterUserRequest(null,"password123","testUser@email.com");
    }

    private Context createContext() {
        TestContext testContext = new TestContext();

        // TODO: customize your context here if needed.
        testContext.setFunctionName("RTSALambda");
        testContext.setMemoryLimitInMB(128);
        testContext.setLogGroupName("RegisterUserLogStream");
        testContext.setAwsRequestId("41C359C079CBAFCF");

        return testContext;
    }

    @Test
    public void testValidRegisterUser() {
        RTSALambda handler = new RTSALambda();
        Context context = createContext();

        RTSAResponse response = handler.handleRequest(goodInput, context);
        
        if (response != null) {
        	Assert.assertTrue(response.getResponseCode().equals(200));
        }
    }
    
    @Test
    public void testInvalidRegisterUser() {
        RTSALambda handler = new RTSALambda();
        Context context = createContext();

        RTSAResponse response = handler.handleRequest(badInput, context);
        
        if (response != null) {
        	Assert.assertTrue(response.getResponseCode().equals(500));
        }
    }
}

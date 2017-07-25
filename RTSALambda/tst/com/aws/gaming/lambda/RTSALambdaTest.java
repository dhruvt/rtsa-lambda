package com.aws.gaming.lambda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.aws.gaming.lambda.RTSALambda.RTSAResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RTSALambdaTest {

    private static S3Event goodInput;
    

    @BeforeClass
    public static void createInput() throws IOException {
    	S3EventNotificationRecord record = new S3EventNotificationRecord("us-west-2","ObjectCreated:Put",
    										"aws:s3","1970-01-01T00:00:00.000Z","2.0",null,null,null,null);
    	List<S3EventNotificationRecord> records = new ArrayList<S3EventNotificationRecord>();
    	records.add(record);
        goodInput = new S3Event(records);        
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
       
}

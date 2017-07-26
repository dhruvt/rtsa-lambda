package com.aws.gaming.lambda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification.S3BucketEntity;
import com.amazonaws.services.s3.event.S3EventNotification.S3Entity;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.event.S3EventNotification.S3ObjectEntity;
import com.amazonaws.services.s3.event.S3EventNotification.UserIdentityEntity;
import com.aws.gaming.lambda.RTSALambda.RTSAResponse;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class RTSALambdaTest {

    private static S3Event goodInput;
    

    @BeforeClass
    public static void createInput() throws IOException {
    	S3BucketEntity s3BucketEntity = new S3BucketEntity("rtsa-source",new UserIdentityEntity("AIDAJ45Q7YFFAREXAMPLE"),"arn:aws:s3:::rtsa-source");
    	S3ObjectEntity s3ObjectEntity = new S3ObjectEntity("1499450422361.png", new Long(320422),"c4550ea1ef4ca84a7628ad2487dcb842", "1.0", "0A1B2C3D4E5F678901");
    	S3Entity s3Entity = new S3Entity("rtsaConfigRule",s3BucketEntity,s3ObjectEntity,"1.0");
    	S3EventNotificationRecord s3EventNotificationRecord = new S3EventNotificationRecord("us-west-2","ObjectCreated:Put",
    										"aws:s3","1970-01-01T00:00:00.000Z","2.0",null,null,s3Entity,new UserIdentityEntity("AIDAJ45Q7YFFAREXAMPLE"));
    	List<S3EventNotificationRecord> s3EventNotificationRecords = new ArrayList<S3EventNotificationRecord>();
    	s3EventNotificationRecords.add(s3EventNotificationRecord);
        goodInput = new S3Event(s3EventNotificationRecords);        
    }

    private Context createContext() {
        TestContext testContext = new TestContext();

        // TODO: customize your context here if needed.
        testContext.setFunctionName("RTSALambda");
        testContext.setMemoryLimitInMB(128);
        testContext.setLogGroupName("RTSALambdaLogStream");
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

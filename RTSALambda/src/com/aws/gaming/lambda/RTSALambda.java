package com.aws.gaming.lambda;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.rekognition.model.S3Object;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.aws.gaming.lambda.RTSALambda.RTSAResponse;
import com.aws.gaming.lambda.rtsa.RTSAExecutorInterface;
import com.aws.gaming.lambda.rtsa.RTSAS3ObjectExecutor;
import com.aws.gaming.lambda.rtsa.data.RTSADataPoint;
import com.aws.gaming.lambda.rtsa.data.RTSADataPointDAO;
import com.aws.gaming.lambda.rtsa.data.RTSADataPointKinesisDAOImpl;

public class RTSALambda implements RequestHandler<S3Event, RTSAResponse> {

    @Override
    public RTSAResponse handleRequest(S3Event input, Context context) {
    	
    	List<S3Object> s3ObjectList = new ArrayList<S3Object>();
    	List<RTSADataPoint> results = new ArrayList<RTSADataPoint>();
    	
    	for (S3EventNotificationRecord record : input.getRecords()) {
            String s3Key = record.getS3().getObject().getKey();
            String s3Bucket = record.getS3().getBucket().getName();
            
            System.out.println("Received File:" + s3Key + " inside S3 Bucket: " + s3Bucket);
            
            S3Object s3Object = new S3Object();
            s3Object.setBucket(s3Bucket);
            s3Object.setName(s3Key);
            s3ObjectList.add(s3Object);            
        }
    	
    	RTSAExecutorInterface rtsaExecutor = new RTSAS3ObjectExecutor();
    	try{
    		results = rtsaExecutor.runFacialAnalysis(s3ObjectList);
    		RTSADataPointDAO rtsaDAO = new RTSADataPointKinesisDAOImpl();
    		for(RTSADataPoint dataPoint:results){
    			System.out.println("Saving Data Point - Emotion:" + dataPoint.getEmotion() + " with Confidence: " + dataPoint.getConfidence());
    			rtsaDAO.save(dataPoint);
    		}
    	}catch(Exception e){
    		System.out.println("Error Running Facial Analysis" + e.getMessage());
    	}
        return new RTSAResponse(results,200);
    }
    
    
    public static class RTSAResponse{
    	
    	List<RTSADataPoint> results;
    	Integer responseCode;
    	
    	public RTSAResponse(){};
    	
		public RTSAResponse(List<RTSADataPoint> results, Integer responseCode) {			
			this.results = results;
			this.responseCode = responseCode;
		}		
		public Integer getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(Integer responseCode) {
			this.responseCode = responseCode;
		}

		public List<RTSADataPoint> getResults() {
			return results;
		}

		public void setResults(List<RTSADataPoint> results) {
			this.results = results;
		}    	   	
    }

}

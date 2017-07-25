package com.aws.gaming.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.aws.gaming.lambda.RTSALambda.RTSAResponse;
import com.aws.gaming.lambda.rtsa.exception.RTSALambdaException;

public class RTSALambda implements RequestHandler<S3Event, RTSAResponse> {

    @Override
    public RTSAResponse handleRequest(S3Event input, Context context) {
    	
        
        return new RTSAResponse("0",200);
    }
    
    
    public static class RTSAResponse{
    	
    	String userId;
    	Integer responseCode;
    	
    	public RTSAResponse(){};
    	
		public RTSAResponse(String userId, Integer responseCode) {
			super();
			this.userId = userId;
			this.responseCode = responseCode;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Integer getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(Integer responseCode) {
			this.responseCode = responseCode;
		}
    	
    	
    }

}

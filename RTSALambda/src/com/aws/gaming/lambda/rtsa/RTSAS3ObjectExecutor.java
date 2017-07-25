package com.aws.gaming.lambda.rtsa;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.S3Object;
import com.aws.gaming.lambda.rekognition.DetectFaceInterface;
import com.aws.gaming.lambda.rekognition.DetectFaceInterfaceImpl;
import com.aws.gaming.lambda.rtsa.data.RTSADataPoint;



/**
 * RTSAS3ObjectExecutor.java
 * 
 * Implementation of the RTSAExecutorInterface Interface, this implementation runs the Amazon Rekognition
 * algorithm on a series of frames that are stored on a remote S3 bucket. 
 * 
 * @author dhruv
 *
 */
public class RTSAS3ObjectExecutor implements RTSAExecutorInterface {

	/* (non-Javadoc)
	 * @see com.aws.gaming.rtsa.RTSAExecutorInterface#runFacialAnalysis(java.util.List)
	 */
	@Override
	public List<RTSADataPoint> runFacialAnalysis(List<S3Object> framesList)
			throws InterruptedException, ExecutionException {		
		
		int threads = Runtime.getRuntime().availableProcessors();
		ExecutorService service = Executors.newFixedThreadPool(threads);
		
		List<Future<RTSADataPoint>> futures = new ArrayList<Future<RTSADataPoint>>();
		for(final S3Object frameLocation:framesList){
			Callable<RTSADataPoint> callable = new Callable<RTSADataPoint>(){
				public RTSADataPoint call() throws Exception{
					DetectFaceInterface dfi = new DetectFaceInterfaceImpl();
					List<FaceDetail> faceDetails = dfi.detectFaceFromS3(frameLocation.getBucket(), frameLocation.getName());
					String fileName = frameLocation.getName().replaceFirst("[.][^.]+$", "");
					RTSACalculator rtsaCalculator = new RTSACalculator();
					Float sentiment = rtsaCalculator.calculateSentinementFromEmotions(faceDetails.get(0).getEmotions());
					String emotion = rtsaCalculator.getPredominantEmotion(faceDetails.get(0).getEmotions());
					return new RTSADataPoint(emotion, Long.parseLong(fileName),sentiment);
				}
			};
			futures.add(service.submit(callable));
		}
		
		service.shutdown();
		
		List<RTSADataPoint> datapoints = new ArrayList<RTSADataPoint>();
		for(Future<RTSADataPoint> future: futures){
			datapoints.add(future.get());
		}
		return datapoints;
	}

}

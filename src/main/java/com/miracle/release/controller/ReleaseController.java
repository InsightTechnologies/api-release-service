package com.miracle.release.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.common.api.bean.Feature;
import com.miracle.common.controller.APIMicroService;
import com.miracle.effort.bean.StoryMetadataBean;
import com.miracle.exception.GatewayServiceException;
import com.miracle.ordering.bean.UnorderedFeaturesBean;
import com.miracle.release.bean.FeatureWithEstimates;
import com.miracle.release.bean.RetrieveReleaseFeatureRequest;
import com.miracle.release.exception.ReleaseErrorCode;
import com.miracle.release.exception.ReleaseException;
import com.miracle.release.util.ReleaseUtil;
import com.miracle.story.bean.FeatureMetadetails;
import com.miracle.story.bean.FeatureStoryDetails;


@RestController
@RequestMapping(value = "/masterBot/project")
public class ReleaseController extends APIMicroService {
	
	public static final Logger logger = LoggerFactory.getLogger(ReleaseController.class);
	@Value("${masterbot.orderFeatureEndpoint}")
	protected String orderFeatureEndpoint;
	@Value("${masterbot.extractStoriesEndpoint}")
	protected String extractStoriesEndpoint;
	@Value("${masterbot.estimateEffortEndpoint}")
	protected String estimateEffortEndpoint;
	
	@Autowired
	public UnorderedFeaturesBean unorderedFeaturesBean;
	@Autowired
	public FeatureMetadetails featureMetadetails;
	@Autowired
	public ReleaseUtil releaseUtil;

	// API_M2
	@PostMapping("/retrieveReleaseFeatures")
	public List<FeatureWithEstimates> runService(
			@RequestBody RetrieveReleaseFeatureRequest retrieveReleaseFeatureRequest) throws Exception {
		double maxStoryPoint = retrieveReleaseFeatureRequest.getMaxStoryPoint();
		double totalEffort = 0;
		unorderedFeaturesBean.setFeatures(retrieveReleaseFeatureRequest.getCustomFeatures());
		unorderedFeaturesBean.setFilterType(retrieveReleaseFeatureRequest.getFilterType());
		List<Feature> orderedFeatures = releaseUtil.postUnorderedFeaturesDetails(buildURLToOrderFeatures(),
				unorderedFeaturesBean, commonUtil.getHeaderDetails(), commonUtil.getAcceptableMediaTypes());

		List<FeatureWithEstimates> releaseFeatures = new ArrayList<FeatureWithEstimates>();
		for (Feature feature : orderedFeatures) {
			FeatureWithEstimates featureWithEstimates = new FeatureWithEstimates();
			featureWithEstimates.setFeatureID(feature.getId());
			featureWithEstimates.setUid(feature.getUid());
			featureWithEstimates.setFeatureName(feature.getName());
			logger.info("Feature ID ::" + feature.getId() + "\t Feature State :: " + feature.getState());
			if (feature.getState() != 0) {
				featureMetadetails.setId(feature.getId());
				featureMetadetails.setProjectName(retrieveReleaseFeatureRequest.getProjectName());
				try
				{
					FeatureStoryDetails featureStoryDetails = releaseUtil.postfeatureMetadata(
							buildURLForGetStoriesFromFeature(), featureMetadetails, commonUtil.getHeaderDetails(),
							commonUtil.getAcceptableMediaTypes());
					logger.info("Feature ID :" + featureStoryDetails.getId() + " \t Story ids :"
							+ featureStoryDetails.getStories_ids());
					StoryMetadataBean storyMetadataBean = new StoryMetadataBean();
					storyMetadataBean.setProjectName(retrieveReleaseFeatureRequest.getProjectName());
					storyMetadataBean.setStoryStates(retrieveReleaseFeatureRequest.getStoryStates());
					storyMetadataBean.setStories_ids(featureStoryDetails.getStories_ids());
					double featureEstimate = releaseUtil.estimateEffort(buildURLForEstimateFeatureEffort(),
							storyMetadataBean, commonUtil.getHeaderDetails(),
							commonUtil.getAcceptableMediaTypes());
					totalEffort += featureEstimate;
					logger.info("Feature ID :: " + feature.getId() + "\t Stories Satets : "
							+ storyMetadataBean.getStories_ids() + "\t Estimate ::" + featureEstimate);
					featureWithEstimates.setEffort(featureEstimate);
				}
				catch(ReleaseException releaseException){
					logger.error("Getting exception in retrieve release features, Exception Description :: "+releaseException.getMessage(),releaseException);
					throw releaseException;
				}catch(GatewayServiceException gatewayServiceException){
					logger.error("Getting exception in retrieve release features, Exception Description :: " +gatewayServiceException.getMessage(),gatewayServiceException);
					throw gatewayServiceException;
				}
				catch(Exception exception){
							logger.error("Getting exception in retrieve release features, Exception Description :: "+exception.getLocalizedMessage());					
							throw new ReleaseException("Getting exception in retrieve release features, Exception Description :: "+exception.getMessage(),exception,
									ReleaseErrorCode.RELEASE_CONTROLLER_UNKNOWN_EXCEPTION,HttpStatus.INTERNAL_SERVER_ERROR);
						}

			} else {
				logger.info("Feature Id :: " + feature.getId() + "\t Estimate ::" + feature.getEffort());
				featureWithEstimates.setEffort(feature.getEffort());
				totalEffort += feature.getEffort();
			}

			if (totalEffort < maxStoryPoint) {
				releaseFeatures.add(featureWithEstimates);
			} else {
				break;
			}
		}
		return releaseFeatures;
		// return orderedFeatures;
	}

	private String buildURLToOrderFeatures() {
		StringBuilder url = new StringBuilder("");
		url.append(loadBalence_URLPrefix).append(orderFeatureEndpoint);
		return url.toString();
	}

	private String buildURLForGetStoriesFromFeature() {
		StringBuilder url = new StringBuilder("");
		url.append(loadBalence_URLPrefix).append(extractStoriesEndpoint);
		return url.toString();
	}

	private String buildURLForEstimateFeatureEffort() {
		StringBuilder url = new StringBuilder("");
		url.append(loadBalence_URLPrefix).append(estimateEffortEndpoint);
		return url.toString();
	}
}

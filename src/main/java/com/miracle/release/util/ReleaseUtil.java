package com.miracle.release.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import com.miracle.common.api.bean.APIMicroServiceBean;
import com.miracle.common.api.bean.Feature;
import com.miracle.effort.bean.StoryMetadataBean;
import com.miracle.ordering.bean.UnorderedFeaturesBean;
import com.miracle.release.bean.FeatureWithEstimates;
import com.miracle.release.bean.RetrieveReleaseFeatureRequest;
import com.miracle.release.exception.ReleaseErrorCode;
import com.miracle.release.exception.ReleaseException;
import com.miracle.story.bean.FeatureMetadetails;
import com.miracle.story.bean.FeatureStoryDetails;

@Controller
public class ReleaseUtil {
	@Autowired
	RestTemplate restTemplate;

	@Value("${masterbot.retryCount}")
	public String retryCount;
	@Value("${masterbot.maxDelayTimeInSec}")
	public String maxDelayTimeInSec;

	public static final Logger logger = LoggerFactory.getLogger(ReleaseUtil.class);

	public List<Feature> postUnorderedFeaturesDetails(String url, UnorderedFeaturesBean unorderedFeaturesBean,
			Map<String, String> headerDetails, List<MediaType> acceptableMediaTypes) throws Exception {
		boolean isException = false;
		StringBuilder exceptionMessage = new StringBuilder("");
		int count = 0;
		List<Feature> orderedFeature = new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		for (Map.Entry<String, String> entry : headerDetails.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<UnorderedFeaturesBean> entity = new HttpEntity<>(unorderedFeaturesBean, headers);

		ResponseEntity<List<Feature>> responseEntity = null;
		while (count < Integer.parseInt(retryCount)) {
			try {

				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<List<Feature>>() {
						});
				if (responseEntity.getStatusCode() == HttpStatus.OK) {
					orderedFeature = (List<Feature>) responseEntity.getBody();
					isException = false;
					break;
				} else {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					isException = true;
					count++;
				}

			} catch (Exception exception) {
				try {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					isException = true;
					count++;

				} catch (InterruptedException interruptedException) {

					logger.error(
							"Getting exception while hit  " + url + " ,Retry Count :: " + count
									+ ", Exception Description  ::" + interruptedException.getMessage(),interruptedException);
					throw new ReleaseException(
							"Getting exception while hit  " + url + " ,Retry Count :: " + count
									+ ", Exception Description  ::" + interruptedException.getMessage() + "\r\n",
							interruptedException);
				}
			}
		}
		// after the max retries count is meet and still the response is not received
		// through an exception
		if (isException) {
			if (responseEntity == null) {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException(
						"Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),
						ReleaseErrorCode.UNABLE_TO_ORDER_FEATURE_LIST,
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException(
						"Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),
						ReleaseErrorCode.UNABLE_TO_ORDER_FEATURE_LIST,
						responseEntity.getStatusCode());
			}
		}
		logger.info("Ordered Feature :: " + orderedFeature);
		return orderedFeature;

	}

	public FeatureStoryDetails postfeatureMetadata(String url, FeatureMetadetails featureMetadetails,
			Map<String, String> headerDetails, List<MediaType> acceptableMediaTypes) throws Exception {

		StringBuilder exceptionMessage = new StringBuilder("");
		int count = 0;
		boolean isException = false;
		FeatureStoryDetails featureStoryDetails = new FeatureStoryDetails();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		for (Map.Entry<String, String> entry : headerDetails.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<FeatureMetadetails> entity = new HttpEntity<>(featureMetadetails, headers);
		ResponseEntity<FeatureStoryDetails> responseEntity = null;
		while (count < Integer.parseInt(retryCount)) {
			try {

				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<FeatureStoryDetails>() {
						});
				if (responseEntity.getStatusCode() == HttpStatus.OK) {
					featureStoryDetails = (FeatureStoryDetails) responseEntity.getBody();
					isException = false;
					break;
				} else {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					isException = true;
					count++;
				}

			} catch (Exception exception) {
				try {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					isException = true;
					count++;

				} catch (InterruptedException interruptedException) {

					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage());
					throw new Exception("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage() + "\r\n");
				}
			}
		}
		// after the max retries count is meet and still the response is not received
		// through an exception
		if (isException) {
			if (responseEntity == null) {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException(
						"Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_STORIES_FROM_FEATURE,
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException("Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_STORIES_FROM_FEATURE,
						responseEntity.getStatusCode());
			}
		}
		logger.info(" Feature Story Details :: " + featureStoryDetails);
		return featureStoryDetails;
	}
	
	public double estimateEffort(String url, StoryMetadataBean storyMetadataBean, Map<String, String> headerDetails,
			List<MediaType> acceptableMediaTypes) throws Exception {
		StringBuilder exceptionMessage = new StringBuilder("");
		double featureEstimate = 0;
		int count = 0;
		boolean isException = false;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		for (Map.Entry<String, String> entry : headerDetails.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		// MediaType.APPLICATION_JSON
		HttpEntity<StoryMetadataBean> entity = new HttpEntity<>(storyMetadataBean, headers);
		ResponseEntity<Double> responseEntity = null;

		while (count < Integer.parseInt(retryCount)) {
			try {

				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, Double.class);
				if (responseEntity.getStatusCode() == HttpStatus.OK) {
					featureEstimate = responseEntity.getBody();
					isException = false;
					break;
				} else {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;
				}

			} catch (Exception exception) {
				try {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;

				} catch (InterruptedException interruptedException) {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " ,  Exception Description ::" + interruptedException.getMessage());

					throw new Exception("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " ,  Exception Description ::" + interruptedException.getMessage() + "\r\n");
				}
			}
		}
		// after the max retries count is meet and still the response is not
		// received
		// through an exception
		// after the max retries count is meet and still the response is not received
				// through an exception
			if (isException) {
				if (responseEntity == null) {
					logger.error("Getting exception while hit  " + url + " , Exception Description :: "
							+ exceptionMessage.toString());
					throw new ReleaseException(
							"Getting exception while hit  " + url + " , Exception Description :: "
									+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_ESTIMATE_EFFORT,
							HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					logger.error("Getting exception while hit  " + url + " , Exception Description :: "
							+ exceptionMessage.toString());
					throw new ReleaseException("Getting exception while hit  " + url + " , Exception Description :: "
									+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_ESTIMATE_EFFORT,
							responseEntity.getStatusCode());
				}
			}
		logger.info("Estimated Effort :: " + featureEstimate + "\t for " + storyMetadataBean);
		return featureEstimate;
	}

	public List<Feature> getUnorderedFeatures(String url, APIMicroServiceBean apiMicroServiceBean,
			Map<String, String> headerDetails, List<MediaType> acceptableMediaTypes) throws Exception {

		StringBuilder exceptionMessage = new StringBuilder("");
		int count = 0;
		boolean isException = false;
		List<Feature> unorderedFeature = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		for (Map.Entry<String, String> entry : headerDetails.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<APIMicroServiceBean> entity = new HttpEntity<>(apiMicroServiceBean, headers);

		ResponseEntity<List<Feature>> responseEntity = null;
		while (count < Integer.parseInt(retryCount)) {
			try {

				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<List<Feature>>() {
						});
				if (responseEntity.getStatusCode() == HttpStatus.OK) {
					unorderedFeature = (List<Feature>) responseEntity.getBody();
					isException = false;
					break;
				} else {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;
				}

			} catch (Exception exception) {
				try {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;

				} catch (InterruptedException interruptedException) {

					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage());
					throw new Exception("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage() + "\r\n");
				}
			}
		}
		// after the max retries count is meet and still the response is not received
		// through an exception
		if (isException) {
			if (responseEntity == null) {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException(
						"Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_FEATURES,
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException("Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_FEATURES,
						responseEntity.getStatusCode());
			}
		}
		logger.info("Features extrcted from icescrum  :: " + unorderedFeature);
		return unorderedFeature;
	}

	public List<FeatureWithEstimates> retrieveReleaseFeatures(String url,
			RetrieveReleaseFeatureRequest retrieveReleaseFeatureRequest, Map<String, String> headerDetails,
			List<MediaType> acceptableMediaTypes) throws Exception {

		StringBuilder exceptionMessage = new StringBuilder("");
		int count = 0;
		boolean isException = false;
		List<FeatureWithEstimates> releaseFeatures = new ArrayList<FeatureWithEstimates>();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(acceptableMediaTypes);
		for (Map.Entry<String, String> entry : headerDetails.entrySet()) {
			headers.set(entry.getKey(), entry.getValue());
		}
		HttpEntity<RetrieveReleaseFeatureRequest> entity = new HttpEntity<>(retrieveReleaseFeatureRequest, headers);

		ResponseEntity<List<FeatureWithEstimates>> responseEntity = null;
		while (count < Integer.parseInt(retryCount)) {
			try {

				responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity,
						new ParameterizedTypeReference<List<FeatureWithEstimates>>() {
						});
				if (responseEntity.getStatusCode() == HttpStatus.OK) {
					releaseFeatures = (List<FeatureWithEstimates>) responseEntity.getBody();
					isException = false;
					break;
					
				} else {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ " , Status Code :: " + responseEntity.getStatusCode() + ", Exception Description ::"
							+ responseEntity.toString() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;
				}

			} catch (Exception exception) {
				try {
					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage());
					// Append exception to the exception message
					exceptionMessage.append("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description ::" + exception.getMessage() + "\r\n");
					// wait for the configured time
					Thread.sleep(1000 * Long.parseLong(maxDelayTimeInSec));
					count++;
					isException = true;

				} catch (InterruptedException interruptedException) {

					logger.error("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage());
					throw new Exception("Getting exception while hit  " + url + " ,Retry Count :: " + count
							+ ", Exception Description  ::" + interruptedException.getMessage() + "\r\n");
				}
			}
		}
		// after the max retries count is meet and still the response is not received
		// through an exception
		if (isException) {
			if (responseEntity == null) {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException(
						"Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_FEATURES,
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				logger.error("Getting exception while hit  " + url + " , Exception Description :: "
						+ exceptionMessage.toString());
				throw new ReleaseException("Getting exception while hit  " + url + " , Exception Description :: "
								+ exceptionMessage.toString(),ReleaseErrorCode.UNABLE_TO_GET_FEATURES,
						responseEntity.getStatusCode());
			}
		}
		logger.info("Release Feature List with estimate:: " + releaseFeatures);
		return releaseFeatures;
	}
}

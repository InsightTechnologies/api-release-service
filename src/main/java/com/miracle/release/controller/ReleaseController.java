package com.miracle.release.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miracle.feature.response.FeatureJson;
import com.miracle.feature.response.FeatureResponse;

@RestController
public class ReleaseController {

	@GetMapping(value = "/releaseFeatures")
	public ResponseEntity<FeatureResponse> buildReleaseFeatures(
			@RequestParam(value = "featureJson") FeatureJson featureJson,
			@RequestParam(value = "storyStates") List<Object> storyStates,
			@RequestParam(value = "maxStoryPoints") Integer maxStoryPoints,
			@RequestParam(value = "filter") String filter, @RequestParam(value = "projectName") String projectName) {
		// Invoke other services to retrieve ordered features, stories, efforts
		// Build Release features with effort
		FeatureResponse response = new FeatureResponse();
		// send response using FeatureResponse
		return new ResponseEntity<FeatureResponse>(response, HttpStatus.OK);
	}

}

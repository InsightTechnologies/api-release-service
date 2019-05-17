package com.miracle.release.bean;

import java.util.List;

public class RetrieveReleaseFeatureResponse {
	private List<FeatureWithEstimates> releaseFeatures;

	public List<FeatureWithEstimates> getReleaseFeatures() {
		return releaseFeatures;
	}

	public void setReleaseFeatures(List<FeatureWithEstimates> releaseFeatures) {
		this.releaseFeatures = releaseFeatures;
	}
	
}

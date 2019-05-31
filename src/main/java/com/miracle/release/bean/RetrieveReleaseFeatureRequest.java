package com.miracle.release.bean;

import java.util.List;

import com.miracle.common.api.bean.Feature;

public class RetrieveReleaseFeatureRequest {
	private double maxStoryPoint;
	private String filterType;
	private List<Feature> customFeatures;
	private List<String> storyStates;
	private List<String> featureStates;
	private String projectName;
	/**
	 * @return the maxStoryPoint
	 */
	public double getMaxStoryPoint() {
		return maxStoryPoint;
	}
	/**
	 * @param maxStoryPoint the maxStoryPoint to set
	 */
	public void setMaxStoryPoint(double maxStoryPoint) {
		this.maxStoryPoint = maxStoryPoint;
	}
	/**
	 * @return the filterType
	 */
	public String getFilterType() {
		return filterType;
	}
	/**
	 * @param filterType the filterType to set
	 */
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}
	/**
	 * @return the customFeatures
	 */
	public List<Feature> getCustomFeatures() {
		return customFeatures;
	}
	/**
	 * @param customFeatures the customFeatures to set
	 */
	public void setCustomFeatures(List<Feature> customFeatures) {
		this.customFeatures = customFeatures;
	}
	/**
	 * @return the storyStates
	 */
	public List<String> getStoryStates() {
		return storyStates;
	}
	/**
	 * @param storyStates the storyStates to set
	 */
	public void setStoryStates(List<String> storyStates) {
		this.storyStates = storyStates;
	}
	/**
	 * @return the featureStates
	 */
	public List<String> getFeatureStates() {
		return featureStates;
	}
	/**
	 * @param featureStates the featureStates to set
	 */
	public void setFeatureStates(List<String> featureStates) {
		this.featureStates = featureStates;
	}
	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "RetrieveReleaseFeatureRequest [maxStoryPoint=" + maxStoryPoint + ", filterType=" + filterType
				+ ", customFeatures=" + customFeatures + ", storyStates=" + storyStates + ", featureStates="
				+ featureStates + ", projectName=" + projectName + "]";
	}
	
	
		
}

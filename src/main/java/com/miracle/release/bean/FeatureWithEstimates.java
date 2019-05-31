package com.miracle.release.bean;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"featureID", "uid","featureName","effort"})
public class FeatureWithEstimates {
	private int featureID;
	private int uid;
	private String featureName;
	private double effort;
	/**
	 * @return the featureID
	 */
	public int getFeatureID() {
		return featureID;
	}
	/**
	 * @param featureID the featureID to set
	 */
	public void setFeatureID(int featureID) {
		this.featureID = featureID;
	}
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the featureName
	 */
	public String getFeatureName() {
		return featureName;
	}
	/**
	 * @param featureName the featureName to set
	 */
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	/**
	 * @return the effort
	 */
	public double getEffort() {
		return effort;
	}
	/**
	 * @param effort the effort to set
	 */
	public void setEffort(double effort) {
		this.effort = effort;
	}
	
	/* 
	 */
	@Override
	public String toString() {
		return "FeatureWithEstimates [featureID=" + featureID + ", uid=" + uid + ", featureName=" + featureName
				+ ", effort=" + effort + "]";
	}
	
	
	
}

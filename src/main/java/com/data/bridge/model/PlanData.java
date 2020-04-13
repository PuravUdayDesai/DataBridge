package com.data.bridge.model;

import javax.validation.constraints.NotNull;

public class PlanData {
	
	@NotNull(message="PlanId cannot be NULL")
	Long planId;
	@NotNull(message="PlanName cannot be NULL")
	String planName;
	@NotNull(message="PlanDescription cannot be NULL")
	String planDescription;
	@NotNull(message="UploadAllowed cannot be NULL")
	Integer uploadAllowed;
	@NotNull(message="DownloadAllowed cannot be NULL")
	Integer downloadAllowed;
	@NotNull(message="PaidAllowed cannot be NULL")
	Integer paidAllowed;
	@NotNull(message="PlanPrice cannot be NULL")
	Integer planPrice;
	
	public PlanData() {
		
	}

	public PlanData(@NotNull(message = "PlanId cannot be NULL") Long planId,
			@NotNull(message = "PlanName cannot be NULL") String planName,
			@NotNull(message = "PlanDescription cannot be NULL") String planDescription,
			@NotNull(message = "UploadAllowed cannot be NULL") Integer uploadAllowed,
			@NotNull(message = "DownloadAllowed cannot be NULL") Integer downloadAllowed,
			@NotNull(message = "PaidAllowed cannot be NULL") Integer paidAllowed,
			@NotNull(message="PlanPrice cannot be NULL")Integer planPrice) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.planDescription = planDescription;
		this.uploadAllowed = uploadAllowed;
		this.downloadAllowed = downloadAllowed;
		this.paidAllowed = paidAllowed;
		this.planPrice= planPrice;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDescription() {
		return planDescription;
	}

	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}

	public Integer getUploadAllowed() {
		return uploadAllowed;
	}

	public void setUploadAllowed(Integer uploadAllowed) {
		this.uploadAllowed = uploadAllowed;
	}

	public Integer getDownloadAllowed() {
		return downloadAllowed;
	}

	public void setDownloadAllowed(Integer downloadAllowed) {
		this.downloadAllowed = downloadAllowed;
	}

	public Integer getPaidAllowed() {
		return paidAllowed;
	}

	public void setPaidAllowed(Integer paidAllowed) {
		this.paidAllowed = paidAllowed;
	}

	public Integer getPlanPrice() {
		return planPrice;
	}

	public void setPlanPrice(Integer planPrice) {
		this.planPrice = planPrice;
	}
	
	
	
}

package com.data.bridge.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MemberSingUpData {

	@NotNull(message="UserName cannot be NULL")
	String userName;
	@NotNull(message="EmailId cannot be NULL")
	String emailId;
	@NotNull(message="ContactNumber cannot be NULL")
	String contactNumber;
	@NotNull(message="Password cannot be NULL")
	String password;
	@NotNull(message="SecurityQuestion1 cannot be NULL")
	String securityQuestion1;
	@NotNull(message="SecurityQuestion2 cannot be NULL")
	String securityQuestion2;
	@NotNull(message="SecurityQuestion3 cannot be NULL")
	String securityQuestion3;
	@NotNull(message="Description cannot be NULL")
	String description;
	@NotNull(message="PlanId cannot be NULL")
	Integer planId;
	@NotNull(message="CreatedOn cannot be NULL")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "IST")
	Timestamp createdOn;
	
	public MemberSingUpData() {
		
	}

	public MemberSingUpData(@NotNull(message = "UserName cannot be NULL") String userName,
			@NotNull(message = "EmailId cannot be NULL") String emailId,
			@NotNull(message = "ContactNumber cannot be NULL") String contactNumber,
			@NotNull(message = "Password cannot be NULL") String password,
			@NotNull(message = "SecurityQuestion1 cannot be NULL") String securityQuestion1,
			@NotNull(message = "SecurityQuestion2 cannot be NULL") String securityQuestion2,
			@NotNull(message = "SecurityQuestion3 cannot be NULL") String securityQuestion3,
			@NotNull(message = "Description cannot be NULL") String description,
			@NotNull(message = "PlanId cannot be NULL") Integer planId,
			@NotNull(message = "CreatedOn cannot be NULL") Timestamp createdOn) {
		super();
		this.userName = userName;
		this.emailId = emailId;
		this.contactNumber = contactNumber;
		this.password = password;
		this.securityQuestion1 = securityQuestion1;
		this.securityQuestion2 = securityQuestion2;
		this.securityQuestion3 = securityQuestion3;
		this.description = description;
		this.planId = planId;
		this.createdOn = createdOn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecurityQuestion1() {
		return securityQuestion1;
	}

	public void setSecurityQuestion1(String securityQuestion1) {
		this.securityQuestion1 = securityQuestion1;
	}

	public String getSecurityQuestion2() {
		return securityQuestion2;
	}

	public void setSecurityQuestion2(String securityQuestion2) {
		this.securityQuestion2 = securityQuestion2;
	}

	public String getSecurityQuestion3() {
		return securityQuestion3;
	}

	public void setSecurityQuestion3(String securityQuestion3) {
		this.securityQuestion3 = securityQuestion3;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	
}

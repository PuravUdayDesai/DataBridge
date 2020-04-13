package com.data.bridge.model;

import java.sql.Timestamp;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DocumentUploadDetails {
	
	@NotNull(message="MemberId cannot be NULL")
	Long memberId;
	@NotNull(message="EconomicAccess cannot be NULL")
	Double economicAccess;
	@NotNull(message="DocumentName cannot be NULL")
	String documentName;
	@NotNull(message="DocumentDescription cannot be NULL")
	String documentDescription;
	@NotNull(message="CreationDate cannot be NULL")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "IST")
	Timestamp creationDate;
	
	public DocumentUploadDetails() {
		
	}

	public DocumentUploadDetails(@NotNull(message = "MemberId cannot be NULL") Long memberId,
			@NotNull(message = "EconomicAccess cannot be NULL") Double economicAccess,
			@NotNull(message = "DocumentName cannot be NULL") String documentName,
			@NotNull(message = "DocumentDescription cannot be NULL") String documentDescription,
			@NotNull(message = "CreationDate cannot be NULL") Timestamp creationDate) {
		super();
		this.memberId = memberId;
		this.economicAccess = economicAccess;
		this.documentName = documentName;
		this.documentDescription = documentDescription;
		this.creationDate = creationDate;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Double getEconomicAccess() {
		return economicAccess;
	}

	public void setEconomicAccess(Double economicAccess) {
		this.economicAccess = economicAccess;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}
	
}

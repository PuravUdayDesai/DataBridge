package com.data.bridge.model;

import java.sql.Date;

import javax.validation.constraints.NotNull;

public class DocumentDetailView {
	@NotNull(message="DocumentId cannot be NULL")	
	Long documentId;
	@NotNull(message="DocumentName cannot be NULL")
	String documentName;
	@NotNull(message="DocumentDescription cannot be NULL")
	String documentDescription;
	@NotNull(message="EconomicAccess cannot be NULL")
	Double economicAccess;
	@NotNull(message="CreationDate cannot be NULL")
	Date creationDate;
	@NotNull(message="FileType cannot be NULL")
	String fileType;
	@NotNull(message="CurrentMemberAccess cannot be NULL")
	Boolean currentMemberAccess;
	@NotNull(message="DocumentURL cannot be NULL")
	String documentURL;
	
	public DocumentDetailView() {
		
	}

	public DocumentDetailView(@NotNull(message = "DocumentId cannot be NULL") Long documentId,
			@NotNull(message = "DocumentName cannot be NULL") String documentName,
			@NotNull(message = "DocumentDescription cannot be NULL") String documentDescription,
			@NotNull(message = "EconomicAccess cannot be NULL") Double economicAccess,
			@NotNull(message = "CreationDate cannot be NULL") Date creationDate,
			@NotNull(message = "FileType cannot be NULL") String fileType,
			@NotNull(message = "CurrentMemberAccess cannot be NULL") Boolean currentMemberAccess,
			@NotNull(message = "DocumentURL cannot be NULL") String documentURL) {
		super();
		this.documentId = documentId;
		this.documentName = documentName;
		this.documentDescription = documentDescription;
		this.economicAccess = economicAccess;
		this.creationDate = creationDate;
		this.fileType = fileType;
		this.currentMemberAccess = currentMemberAccess;
		this.documentURL = documentURL;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
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

	public Double getEconomicAccess() {
		return economicAccess;
	}

	public void setEconomicAccess(Double economicAccess) {
		this.economicAccess = economicAccess;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Boolean getCurrentMemberAccess() {
		return currentMemberAccess;
	}

	public void setCurrentMemberAccess(Boolean currentMemberAccess) {
		this.currentMemberAccess = currentMemberAccess;
	}

	public String getDocumentURL() {
		return documentURL;
	}

	public void setDocumentURL(String documentURL) {
		this.documentURL = documentURL;
	}

}

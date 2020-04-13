package com.data.bridge.model;

import javax.validation.constraints.NotNull;

public class DocumentView {
	
	@NotNull(message="DocumentId cannot be NULL")
	Long documentId;
	@NotNull(message="DocumentName cannot be NULL")
	String documentName;
	@NotNull(message="EconomicAccess cannot be NULL")
	Double economicAccess;
	@NotNull(message="MemberViewAccess cannot be NULL")
	Long memberViewAccess;
	@NotNull(message="CurrentMemberAcess cannot be NULL")
	Boolean currentMemberAcess;
	
	public DocumentView() {
		
	}

	public DocumentView(
			@NotNull(message="DocumentId cannot be NULL")Long documentId,
			@NotNull(message = "DocumentName cannot be NULL") String documentName,
			@NotNull(message = "EconomicAccess cannot be NULL") Double economicAccess,
			@NotNull(message = "MemberViewAccess cannot be NULL") Long memberViewAccess,
			@NotNull(message = "CurrentMemberAcess cannot be NULL") Boolean currentMemberAcess) {
		super();
		this.documentId=documentId;
		this.documentName = documentName;
		this.economicAccess = economicAccess;
		this.memberViewAccess = memberViewAccess;
		this.currentMemberAcess = currentMemberAcess;
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

	public Double getEconomicAccess() {
		return economicAccess;
	}

	public void setEconomicAccess(Double economicAccess) {
		this.economicAccess = economicAccess;
	}

	public Long getMemberViewAccess() {
		return memberViewAccess;
	}

	public void setMemberViewAccess(Long memberViewAccess) {
		this.memberViewAccess = memberViewAccess;
	}

	public Boolean getCurrentMemberAcess() {
		return currentMemberAcess;
	}

	public void setCurrentMemberAcess(Boolean currentMemberAcess) {
		this.currentMemberAcess = currentMemberAcess;
	}
	
}

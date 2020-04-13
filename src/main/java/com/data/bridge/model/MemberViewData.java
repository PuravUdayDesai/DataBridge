package com.data.bridge.model;

import javax.validation.constraints.NotNull;

public class MemberViewData {

	@NotNull(message="MemberId cannot be NULL")
	Long memberId;
	@NotNull(message="MemberName cannot be NULL")
	String memberName;
	@NotNull(message="NumberOfUploads cannot be NULL")
	Long numberOfUploads;
	@NotNull(message="NumberOfSubscribers cannot be NULL")
	Long numberOfSubscribers;
	@NotNull(message="CurrentMemberAcess cannot be NULL")
	Boolean currentMemberAcess;
	
	public MemberViewData() {
		
	}

	public MemberViewData(
			@NotNull(message="MemberId cannot be NULL")Long memberId,
			@NotNull(message = "MemberName cannot be NULL") String memberName,
			@NotNull(message = "NumberOfUploads cannot be NULL") Long numberOfUploads,
			@NotNull(message = "NumberOfSubscribers cannot be NULL") Long numberOfSubscribers,
			@NotNull(message = "CurrentMemberAcess cannot be NULL") Boolean currentMemberAcess) {
		super();
		this.memberId=memberId;
		this.memberName = memberName;
		this.numberOfUploads = numberOfUploads;
		this.numberOfSubscribers = numberOfSubscribers;
		this.currentMemberAcess = currentMemberAcess;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Long getNumberOfUploads() {
		return numberOfUploads;
	}

	public void setNumberOfUploads(Long numberOfUploads) {
		this.numberOfUploads = numberOfUploads;
	}

	public Long getNumberOfSubscribers() {
		return numberOfSubscribers;
	}

	public void setNumberOfSubscribers(Long numberOfSubscribers) {
		this.numberOfSubscribers = numberOfSubscribers;
	}

	public Boolean getCurrentMemberAcess() {
		return currentMemberAcess;
	}

	public void setCurrentMemberAcess(Boolean currentMemberAcess) {
		this.currentMemberAcess = currentMemberAcess;
	}
	
}

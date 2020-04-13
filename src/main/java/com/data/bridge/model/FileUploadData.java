package com.data.bridge.model;

import javax.validation.constraints.NotNull;

public class FileUploadData {

	@NotNull(message="FilePath cannot be NULL")
	String filePath;
	@NotNull(message="FileSize cannot be NULL")
	Long fileSize;
	@NotNull(message="FileType cannot be NULL")
	String fileType;
	
	public FileUploadData() {
		
	}

	public FileUploadData(@NotNull(message = "FilePath cannot be NULL") String filePath,
			@NotNull(message = "FileSize cannot be NULL") Long fileSize,
			@NotNull(message = "FileType cannot be NULL") String fileType) {
		super();
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

}

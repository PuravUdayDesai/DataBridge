package com.data.bridge.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fileDownload")
public class FileDownloadController {
	private static final String EXTERNAL_FILE_PATH = "E:\\";

	// http://localhost:8080/download/file?filePath=1/1/2020-04-01%2010.10.11.0&fileName=my.ogg
	@RequestMapping("/view")
	public void viewFile(HttpServletRequest request, 
									HttpServletResponse response,
									@RequestParam("filePath") String filePath, 
									@RequestParam("fileName") String fileName) throws IOException {
		
		System.out.println("File Path: " + filePath + "\\" + fileName);
		File file = new File(EXTERNAL_FILE_PATH + filePath + "\\" + fileName);
		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}
	
	@RequestMapping("/download")
	public void downloadFile(HttpServletRequest request, 
									HttpServletResponse response,
									@RequestParam("filePath") String filePath, 
									@RequestParam("fileName") String fileName) throws IOException {
		
		File file = new File(EXTERNAL_FILE_PATH + filePath + "\\" + fileName);
		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("attachment;filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());

		}
	}
	
	
	
}

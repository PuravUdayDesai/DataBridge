package com.data.bridge.aspect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.data.bridge.exception.FileStorageException;
import com.data.bridge.model.FileUploadData;

public class FileUploaderAspect {
	public static String replaceColonToPeriod(String t)
	{
		String newStr=t.replace(':', '.');
		return newStr;
	}
	
	public static FileUploadData fileUpload(MultipartFile file, String filePath) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        String path="E:\\"+FileUploaderAspect.replaceColonToPeriod(filePath);
        File f=new File(path);
        if(!f.exists())
        {
        Boolean b=f.mkdirs();
        if(!b)
        {
        	throw new IOException("Cannot Create Director Specified: "+path);
        }
        }
        String qualifiedPath=path+"\\"+fileName;
        Path targetLocation = Paths.get(qualifiedPath);
        FileUploadData fud=new FileUploadData(qualifiedPath,file.getSize(),file.getContentType());
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fud;
	}
	
}

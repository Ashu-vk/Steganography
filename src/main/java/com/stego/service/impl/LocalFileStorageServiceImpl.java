package com.stego.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.stego.service.FileStoratgeSystem;

public class LocalFileStorageServiceImpl implements FileStoratgeSystem {

	static String fileUrl = "http://localhost:8080/files/";
	
	   private final String basePath;

	    public LocalFileStorageServiceImpl(String basePath) {
	        this.basePath = basePath;
	    }

	    @Override
	    public String upload(byte[] file, String fileName) {
	        fileName = fileName.substring(0, fileName.lastIndexOf('.')) +fileName.substring(fileName.lastIndexOf('.'));
	        try (FileOutputStream fos = new FileOutputStream(basePath + "/" + fileName)) {
	            fos.write(file);
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to upload file locally", e);
	        }
	        return fileName; // return path or identifier
	    }

	    @Override
	    public String download(String fileKey) {
	        return fileUrl + fileKey;
	    }
	
}

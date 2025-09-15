package com.stego.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stego.model.RestImageData;
import com.stego.service.EncryptService;

@RestController
public class EncryptionController {

	@Autowired
	private EncryptService encryptService;
	
	@PostMapping("/rest/api/message")
	public String encryptMessage(@RequestParam("file") MultipartFile image,
			 @RequestParam("message") String Message) {
		encryptService.encrypt(image, Message);
		
		return "this is public Controller" + image.getOriginalFilename();
	}
	
	@GetMapping("/rest/api/reveal/{imageId}")
	public String decryptMessage(@PathVariable("imageId") Long imageId) {
		return encryptService.decrypt(imageId);
	}
	
	@GetMapping("/rest/api/images")
	public List<RestImageData> getImage() {
	    List<RestImageData> images = encryptService.getImages();
	        return images;
	}
	
}

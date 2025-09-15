package com.stego.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.stego.domain.ImageData;
import com.stego.model.RestImageData;

public interface EncryptService {

	public void encrypt(MultipartFile image, String message) ;

	String decrypt(Long imageId);

	List<RestImageData> getImages();

	Optional<ImageData> getImage(Long id);
}

package com.stego.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stego.domain.ImageData;
import com.stego.model.RestImageData;
import com.stego.repository.ImageDataRepo;
import com.stego.service.EncryptService;
import com.stego.service.FileStoratgeSystem;

@Service
public class EncryptServiceImpl implements EncryptService {

	@Autowired
	private ImageDataRepo repository;

	@Override
	public void encrypt(MultipartFile image, String message) {
		BufferedImage bfi = null;
		byte[] imageArr = null;
		try {
			bfi = ImageIO.read(image.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = bfi.getWidth(), height = bfi.getHeight();
		byte[] msgByte = message.getBytes();

		// Store message length first (4 bytes = 32 bits)
		int messageLength = msgByte.length;
		System.err.println("Uploading length " + messageLength);
		byte[] lengthBytes = ByteBuffer.allocate(4).putInt(messageLength).array();

		// Combine length + message
		byte[] fullData = new byte[4 + msgByte.length];
		System.arraycopy(lengthBytes, 0, fullData, 0, 4);
		System.arraycopy(msgByte, 0, fullData, 4, msgByte.length);

		int messageIndex = 0;
		for (int i = 0; i < height; i++) {
		    for (int j = 0; j < width; j++) {
		        int rgb = bfi.getRGB(j, i);
		        int red = (rgb >> 16) & 0xFF;
		        int blue = (rgb >> 0) & 0xFF;
		        int green = (rgb >> 8) & 0xFF;
		        
		        if (messageIndex < fullData.length * 8) {
		            int bit = (fullData[messageIndex / 8] >> (messageIndex % 8)) & 1;
		            blue = (blue & 0xFE) | bit;
		            messageIndex++;
		        }
		        
		        rgb = (red << 16) | (green << 8) | blue;
		        bfi.setRGB(j, i, rgb);
		    }
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bfi, "png", baos);
			imageArr = baos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileStoratgeSystem storageSystem = new LocalFileStorageServiceImpl("/steganography/src/main/resources/static/files/");
		String uplaodedFile= storageSystem.upload(imageArr, image.getOriginalFilename());
		ImageData imageData = new ImageData();
		imageData.setName(image.getOriginalFilename());
		imageData.setModifiedName(uplaodedFile);
		imageData.setImageLocation("/steganography/src/main/resources/static/files/");
		imageData.setImageUrl(storageSystem.download(uplaodedFile));
		imageData.setExtension(image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.')));
		imageData = repository.save(imageData);
	}

	@Override
	public String decrypt(Long imageId) {
		Optional<ImageData> data = getImage(imageId);
		BufferedImage image = null;
 		if(data.isPresent()) {
 			  try {
				image = ImageIO.read(new File(data.get().getImageLocation()+"/" + data.get().getModifiedName()));
			} catch (IOException e) {
				System.out.println("An error occurred while reading the image.");
				e.printStackTrace();
			}
		} else {
			return "DOesn't hide anything or Used any other Algorithm";
		}
		// First extract message length (32 bits)
		 int width = image.getWidth(), height = image.getHeight();
		    
		    // First extract message length (32 bits = 4 bytes)
		    int messageLength = extractMessageLength(image, width, height);
		    System.err.println(messageLength);
		    
		    if (messageLength <= 0 || messageLength > 10000) { // Sanity check
		        throw new IllegalArgumentException("Invalid message length: " + messageLength);
		    }
		    
		    // Extract the actual message
		    byte[] messageBytes = extractMessageData(image, width, height, messageLength);
		    
		    return new String(messageBytes);
	
	}
	
	
	
	private int extractMessageLength(BufferedImage bfi, int width, int height) {
    byte[] lengthBytes = new byte[4];
    int byteIndex = 0;
    int currentByte = 0;
    int bitsInCurrentByte = 0;
    
    // Extract first 32 bits (4 bytes) for length
    for (int i = 0; i < height && byteIndex < 4; i++) {
        for (int j = 0; j < width && byteIndex < 4; j++) {
            int rgb = bfi.getRGB(j, i);
            int blue = rgb & 0xFF;
            int bit = blue & 1;
            // MSB FIRst
//            currentByte = (currentByte << 1) | bit;
         // LSB-first assembly
            currentByte = currentByte | (bit << bitsInCurrentByte);
            bitsInCurrentByte++;
            
            if (bitsInCurrentByte == 8) {
                lengthBytes[byteIndex++] = (byte) currentByte;
                currentByte = 0;
                bitsInCurrentByte = 0;
            }
        }
    }
    
    return ByteBuffer.wrap(lengthBytes).getInt();
}

private byte[] extractMessageData(BufferedImage bfi, int width, int height, int messageLength) {
    byte[] messageBytes = new byte[messageLength];
    int bitIndex = 0;
    int byteIndex = 0;
    int currentByte = 0;
    int bitsInCurrentByte = 0;
    
    // Skip first 32 bits (length) and extract message
    for (int i = 0; i < height && byteIndex < messageLength; i++) {
        for (int j = 0; j < width && byteIndex < messageLength; j++) {
            bitIndex++;
            
            // Skip first 32 bits (length data)
            if (bitIndex <= 32) {
                continue;
            }
            
            int rgb = bfi.getRGB(j, i);
            int blue = rgb & 0xFF;
            int bit = blue & 1;
            
            // MSB FIRst
//          currentByte = (currentByte << 1) | bit;
       // LSB-first assembly
          currentByte = currentByte | (bit << bitsInCurrentByte);
            bitsInCurrentByte++;
            
            if (bitsInCurrentByte == 8) {
                messageBytes[byteIndex++] = (byte) currentByte;
                currentByte = 0;
                bitsInCurrentByte = 0;
            }
        }
    }
    
    return messageBytes;
}
	
	@Override
	public List<RestImageData> getImages() {
		List<RestImageData> images = new ArrayList<RestImageData>();
		 images=repository.findAll().stream().map(dt -> new RestImageData(dt)).toList();
		return images;
	}
	@Override
	public Optional<ImageData> getImage(Long id) {
		return repository.findById(id);
	}
}

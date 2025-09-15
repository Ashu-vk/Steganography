package com.stego;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SteganographyApplication  implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SteganographyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//	decrypt();
	}
	public String encrypt() {
		System.err.println("Hello");
		String msg= "Hi  Testing project";
		File file =new File("/steganography/src/main/resources/static/img/just be nice.jpg");
		BufferedImage bfi = null;
		try {
			bfi = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int width = bfi.getWidth(), height = bfi.getHeight();
		byte[] msgByte = msg.getBytes();
		 int messageIndex = 0;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				int rgb= bfi.getRGB(j, i);
				int red = (rgb>>16)& 0xFF;
				int blue = (rgb>>0)& 0xFF;
				int green = (rgb>>8)& 0xFF;
				 if (messageIndex < msgByte.length * 8) {
	                    int bit = (msgByte[messageIndex / 8] >> (7 - (messageIndex % 8))) & 1;
	                    blue = (blue & 0xFE) | bit;  // Update last bit of blue
	                    messageIndex++;
	                }
	                // Combine back the RGB values
	                rgb = (red << 16) | (green << 8) | blue;
	                bfi.setRGB(j, i, rgb);
			}
		}
		File outPutFolder =new File("/steganography/src/main/resources/static/img/output");
		try {
			outPutFolder.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(bfi, "png", new File("/steganography/src/main/resources/static/img/output/output_image.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Message encoded in image successfully.");
		return msg;
	}
	public String decrypt() {
		 try {
	            // Load the image (where the message was hidden)
	            File inputFile = new File("/steganography/src/main/resources/static/img/output/output_image.png");
	            BufferedImage image = ImageIO.read(inputFile);

	            // Specify the maximum number of characters to extract (for example, assume a message of 100 characters)
	            int messageLength = 100;  // This is arbitrary; adjust based on the expected length of the hidden message
	            int totalBits = messageLength * 8;  // Each character is 8 bits, so total bits = message length * 8

	            byte[] messageBytes = new byte[messageLength];  // Store the extracted message bytes
	            int messageIndex = 0;  // Bit position in the message
	            int byteIndex = 0;  // Byte position in the message

	            // Traverse the image pixel by pixel (row by row, column by column)
	            for (int y = 0; y < image.getHeight() && messageIndex < totalBits; y++) {
	                for (int x = 0; x < image.getWidth() && messageIndex < totalBits; x++) {
	                    // Get the RGB value of the pixel
	                    int rgb = image.getRGB(x, y);

	                    // Extract the blue component (bits 0-7)
	                    int blue = rgb & 0xFF;

	                    // Extract the least significant bit (LSB) of the blue component
	                    int bit = blue & 1;

	                    // Add the extracted bit to the correct position in the current byte
	                    messageBytes[byteIndex] = (byte) ((messageBytes[byteIndex] << 1) | bit);

	                    messageIndex++;

	                    // Once 8 bits (1 byte) are collected, move to the next byte
	                    if (messageIndex % 8 == 0) {
	                        byteIndex++;
	                    }
	                }
	            }

	            // Convert the extracted bytes to a string (the hidden message)
	            String extractedMessage = new String(messageBytes);
	            System.out.println("Extracted Message: " + extractedMessage);

	        } catch (IOException e) {
	            System.out.println("An error occurred while reading the image.");
	            e.printStackTrace();
	        }
		return null;
	    
	}

}

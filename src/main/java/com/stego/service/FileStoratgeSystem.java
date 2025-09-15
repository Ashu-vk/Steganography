package com.stego.service;

public interface FileStoratgeSystem {
	public String upload(byte[] file, String fileName);
	public String download(String file);
}

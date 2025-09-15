package com.stego.model;

import com.stego.domain.ImageData;

public class RestImageData {
	
	private Long id;
    
	private String name;
	private String imageUrl;
	
	private String extension;
	
	private String mimeType;
	private Long uploadedById;
	private String imageLocation;
	private String modifiedName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getUploadedById() {
		return uploadedById;
	}

	public void setUploadedById(Long uploadedById) {
		this.uploadedById = uploadedById;
	}

	public RestImageData() {
		super();
	}
	
	public RestImageData(ImageData data) {
		super();
		this.id = data.getId();
		this.name = data.getName();
		this.extension = data.getExtension();
		this.mimeType = data.getMimeType();
		this.uploadedById = data.getUploadedById();
		this.imageLocation = data.getImageLocation();
		this.imageUrl = data.getImageUrl();
		this.modifiedName = data.getModifiedName();;
		}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getModifiedName() {
		return modifiedName;
	}

	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}

	

}

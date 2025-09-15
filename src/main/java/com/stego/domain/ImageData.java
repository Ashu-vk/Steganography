package com.stego.domain;

import com.stego.model.RestImageData;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	
	private String name;
	
	private String extension;
	private String mimeType;
	private String imageUrl;
	
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

	public Long getUploadedById() {
		return uploadedById;
	}

	public void setUploadedById(Long uploadedById) {
		this.uploadedById = uploadedById;
	}

	public  ImageData() {
		super();
	}
	public  ImageData(RestImageData data) {
		super();
		this.id = data.getId();
		this.name = data.getName();
		this.extension = data.getExtension();
		this.setMimeType(data.getMimeType());
		this.uploadedById = data.getUploadedById();
		this.imageLocation = data.getImageLocation();
		this.imageUrl = data.getImageUrl();
		this.modifiedName = data.getModifiedName();
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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

package com.stego.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stego.domain.ImageData;

public interface ImageDataRepo extends JpaRepository<ImageData, Long> {

}

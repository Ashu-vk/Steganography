package com.stego.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stego.entity.RegisteredClientEntity;
@Repository
public interface RegisteredClientJpaRepository extends JpaRepository<RegisteredClientEntity, String> {
	Optional<RegisteredClientEntity> findByClientId(String clientId);
}

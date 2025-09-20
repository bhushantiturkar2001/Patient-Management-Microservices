package com.pm.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pm.authservice.model.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, UUID> {
	
	Optional<User> findByEmail(String email);

}

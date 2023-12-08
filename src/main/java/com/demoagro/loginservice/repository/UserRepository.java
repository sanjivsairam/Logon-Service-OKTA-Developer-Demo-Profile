package com.demoagro.loginservice.repository;

import java.util.Optional;

import com.demoagro.loginservice.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author rsairam
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String userName);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUserSession_RefreshToken(String refreshToken);
}

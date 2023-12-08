package com.demoagro.loginservice.repository;

import java.util.Optional;

import com.demoagro.loginservice.model.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rsairam
 *
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

	Optional<UserSession> findByUser_UserId(Long userId);
}

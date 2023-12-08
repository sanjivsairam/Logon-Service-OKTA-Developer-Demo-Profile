package com.demoagro.loginservice.repository;

import com.demoagro.loginservice.model.entity.AuditLogging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rsairam
 *
 */
@Repository
public interface AuditLoggingRepository extends JpaRepository<AuditLogging, Integer>{

}

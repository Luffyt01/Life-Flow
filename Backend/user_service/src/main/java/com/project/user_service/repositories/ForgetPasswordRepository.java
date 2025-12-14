package com.project.user_service.repositories;

import com.project.user_service.entities.ForgetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgetPasswordRepository extends JpaRepository<ForgetPasswordEntity, Long> {

}

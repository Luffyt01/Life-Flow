package com.project.inventory_service.repositories;

import com.project.inventory_service.entities.ExpiryManagementEntity;
import com.project.inventory_service.entities.enums.AlertLevel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpiryManagementRepository extends JpaRepository<ExpiryManagementEntity, String> {

 @Query("SELECT  e from  ExpiryManagementEntity e where e.alertLevel= :alertLevel")
 List<ExpiryManagementEntity> findByAlertLevel(AlertLevel alertLevel);

 @Modifying
 @Query(value = """
    UPDATE expiry_management em
    INNER JOIN blood_inventory bi ON em.bag_id = bi.bag_id
    SET 
        em.days_until_expiry = DATEDIFF(bi.expiry_date, CURDATE()),
        em.alert_level = CASE 
            WHEN DATEDIFF(bi.expiry_date, CURDATE()) > 4 THEN 'NORMAL'
            WHEN DATEDIFF(bi.expiry_date, CURDATE()) BETWEEN 2 AND 4 THEN 'WARNING'
            WHEN DATEDIFF(bi.expiry_date, CURDATE()) = 1 THEN 'CRITICAL'
            ELSE 'EXPIRED'
        END
    WHERE bi.expiry_date IS NOT NULL
    """, nativeQuery = true)
 @Transactional
 int updateExpiryStatus();
}

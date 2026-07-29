package com.hcl.faultalertengine.repository;

import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

    Optional<Alert> findByDeviceIdAndAlertTypeAndStatus(
            String deviceId,
            String alertType,
            AlertStatus status
    );

    List<Alert> findByStatus(AlertStatus status);

    List<Alert> findByDeviceIdAndStatus(String deviceId, AlertStatus status);
}

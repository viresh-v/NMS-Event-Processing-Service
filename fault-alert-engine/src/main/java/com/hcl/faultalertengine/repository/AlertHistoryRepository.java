package com.hcl.faultalertengine.repository;

import com.hcl.faultalertengine.entity.AlertHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertHistoryRepository extends JpaRepository<AlertHistory, Long> {
}

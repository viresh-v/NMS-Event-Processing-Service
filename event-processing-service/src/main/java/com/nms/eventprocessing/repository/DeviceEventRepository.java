package com.nms.eventprocessing.repository;

import com.nms.eventprocessing.entity.DeviceEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceEventRepository extends JpaRepository<DeviceEvent, Long> {
}

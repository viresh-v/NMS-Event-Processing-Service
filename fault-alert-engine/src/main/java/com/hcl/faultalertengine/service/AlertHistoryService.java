package com.hcl.faultalertengine.service;

import com.hcl.faultalertengine.entity.Alert;
import com.hcl.faultalertengine.enums.AlertAction;
import com.hcl.faultalertengine.enums.AlertStatus;

public interface AlertHistoryService {

    void recordHistory(Alert alert, AlertStatus oldStatus, AlertStatus newStatus, AlertAction action, String remarks);
}

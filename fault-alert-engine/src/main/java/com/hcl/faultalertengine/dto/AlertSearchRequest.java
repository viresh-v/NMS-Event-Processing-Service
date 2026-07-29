package com.hcl.faultalertengine.dto;

import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertSearchRequest {

    private String deviceId;
    private String alertType;
    private Severity severity;
    private AlertStatus status;

}

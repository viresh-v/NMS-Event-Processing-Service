package com.hcl.faultalertengine.rule;

import com.hcl.faultalertengine.dto.AlertMetadata;
import com.hcl.faultalertengine.dto.DeviceEvent;
import com.hcl.faultalertengine.entity.Alert;

import java.util.Optional;

public interface Rule {
    Optional<AlertMetadata> evaluate(DeviceEvent event);
}

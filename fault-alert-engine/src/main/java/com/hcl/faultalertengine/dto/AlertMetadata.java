package com.hcl.faultalertengine.dto;

import com.hcl.faultalertengine.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertMetadata {

    private String alertType;
    private Severity severity;
    private String message;

}

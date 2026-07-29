package com.hcl.faultalertengine.entity;

import com.hcl.faultalertengine.enums.AlertAction;
import com.hcl.faultalertengine.enums.AlertStatus;
import com.hcl.faultalertengine.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long alertId;
    private String deviceId;
    private String alertType;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private AlertStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private AlertStatus newStatus;

    @Enumerated(EnumType.STRING)
    private AlertAction action;

    private String remarks;
    private LocalDateTime changedTime;

}

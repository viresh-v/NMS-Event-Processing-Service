package com.hcl.faultalertengine.controller;

import com.hcl.faultalertengine.dto.AlertResponse;
import com.hcl.faultalertengine.dto.AlertSearchRequest;
import com.hcl.faultalertengine.payload.ApiResponse;
import com.hcl.faultalertengine.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Alert Management",
        description = "Operations related to Fault Alerts"
)
@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @Operation(
            summary = "Get all alerts",
            description = "Returns all alerts available in the system"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Alerts retrived successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getAllAlerts(){
        return ResponseEntity.ok(
                ApiResponse.<List<AlertResponse>>builder()
                        .success(true)
                        .message("Alerts retrieved successfully")
                        .data(alertService.getAllAlerts())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(
            summary = "Get alerts by ID",
            description = "Returns an alert for the given ID"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Alert found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Alert not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertResponse>> getAlertById(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.<AlertResponse>builder()
                        .success(true)
                        .message("Alert retrieved successfully")
                        .data(alertService.getAlertById(id))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(
            summary = "Get all Open alerts",
            description = "Returns all open alerts available in the system"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Open Alerts retrived successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )
    })
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<List<AlertResponse>>> getOpenAlerts(){
        return ResponseEntity.ok(
                ApiResponse.<List<AlertResponse>>builder()
                        .success(true)
                        .message("Open Alerts retrieved successfully")
                        .data(alertService.getOpenAlerts())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(
            summary = "Acknowledge an alert",
            description = "Changes alert status to ACKNOWLEDGED"
    )
    @PutMapping("/{id}/acknowledge")
    public ResponseEntity<ApiResponse<AlertResponse>> acknowledgeAlert(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.<AlertResponse>builder()
                        .success(true)
                        .message("Alert acknowledged successfully")
                        .data(alertService.acknowledgeAlert(id))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @Operation(
            summary = "Resolve an alert",
            description = "Changes alert status to RESOLVED"
    )
    @PutMapping("/{id}/resolve")
    public ResponseEntity<ApiResponse<AlertResponse>> resolveAlert(@PathVariable Long id){
        return ResponseEntity.ok(
                ApiResponse.<AlertResponse>builder()
                        .success(true)
                        .message("Alert resolved successfully")
                        .data(alertService.resolveAlert(id))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<AlertResponse>>> searchAlerts(@ParameterObject AlertSearchRequest request, @ParameterObject @PageableDefault(
            size = 10,
            sort = "createdTime",
            direction = Sort.Direction.DESC)
    Pageable pageable){
        System.out.println("DeviceId :" + request.getDeviceId());
        System.out.println("Severity :" + request.getSeverity());
        System.out.println("Status :" + request.getStatus());
        System.out.println("AlertType :" + request.getAlertType());

        return ResponseEntity.ok(ApiResponse.<Page<AlertResponse>>builder()
                .success(true)
                .message("Alerts retrived successfully")
                .data(alertService.searchAlerts(request, pageable))
                .timestamp(LocalDateTime.now())
                .build());
    }
}

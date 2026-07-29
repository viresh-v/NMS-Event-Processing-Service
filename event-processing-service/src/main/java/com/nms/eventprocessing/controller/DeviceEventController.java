package com.nms.eventprocessing.controller;

import com.nms.eventprocessing.entity.DeviceEvent;
import com.nms.eventprocessing.service.EventProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
@Tag(name = "Processed Events", description = "APIs for retrieving processed tower health events")
public class DeviceEventController {

    private final EventProcessingService eventProcessingService;

    public DeviceEventController(EventProcessingService eventProcessingService) {
        this.eventProcessingService = eventProcessingService;
    }

    @GetMapping
    @Operation(summary = "Get all processed events", description = "Returns every valid tower health event stored by the service.")
    public ResponseEntity<List<DeviceEvent>> getAllProcessedEvents() {
        return ResponseEntity.ok(eventProcessingService.getAllProcessedEvents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get processed event by id", description = "Returns one processed tower health event by its database id.")
    public ResponseEntity<DeviceEvent> getProcessedEventById(@PathVariable Long id) {
        return eventProcessingService.getProcessedEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

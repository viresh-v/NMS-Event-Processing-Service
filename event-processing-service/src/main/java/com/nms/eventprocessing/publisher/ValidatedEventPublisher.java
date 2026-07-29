package com.nms.eventprocessing.publisher;

import com.nms.eventprocessing.entity.DeviceEvent;

public interface ValidatedEventPublisher {

    void publish(DeviceEvent deviceEvent);
}

package com.hcl.nms.simulator.producer;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import com.hcl.nms.simulator.config.SimulatorProperties;
import com.hcl.nms.simulator.exception.KafkaPublishException;
import com.hcl.nms.simulator.model.DeviceMetric;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DeviceProducer {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DeviceProducer.class);

    private final KafkaTemplate<String, DeviceMetric> kafkaTemplate;

    private final SimulatorProperties simulatorProperties;

    public void publish(DeviceMetric metric) {

        CompletableFuture<SendResult<String, DeviceMetric>> future =
                kafkaTemplate.send(
                        simulatorProperties.getTopicName(),
                        metric.getDeviceId(),
                        metric);

        future.whenComplete((result, exception) -> {

            if (exception != null) {

                LOGGER.error(
                        "Failed to publish device [{}]",
                        metric.getDeviceId(),
                        exception);

                throw new KafkaPublishException(
                        "Unable to publish message",
                        exception);

            }

            LOGGER.info(
                    "Published Device [{}] to topic [{}], partition [{}], offset [{}]",
                    metric.getDeviceId(),
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        });

    }

}
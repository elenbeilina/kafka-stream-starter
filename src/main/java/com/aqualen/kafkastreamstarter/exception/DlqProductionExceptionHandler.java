package com.aqualen.kafkastreamstarter.exception;

import com.aqualen.kafkastreamstarter.config.SpringContext;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;

import java.util.Map;

/**
 * Exception handler that sends bad record to DLQ, if something happens on producing side.
 */
@NoArgsConstructor
public class DlqProductionExceptionHandler implements ProductionExceptionHandler {

    private DlqProducer producer;

    @Override
    public ProductionExceptionHandlerResponse handle(ProducerRecord<byte[], byte[]> kafkaRecord, Exception ex) {
        producer.sendToDlq(kafkaRecord, ex);

        return ProductionExceptionHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> configs) {
        producer = SpringContext.getBean(DlqProducer.class);
    }
}

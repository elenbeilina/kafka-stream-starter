package com.aqualen.kafkastreamstarter.exception;

import com.aqualen.kafkastreamstarter.config.SpringContext;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.util.Map;

/**
 * Exception handler that sends bad record to DLQ, if something happens on deserialization side.
 */
@NoArgsConstructor
public class DlqDeserializationExceptionHandler implements DeserializationExceptionHandler {

    private DlqProducer producer;

    @Override
    public DeserializationHandlerResponse handle(
            ProcessorContext context, ConsumerRecord<byte[], byte[]> kafkaRecord, Exception exception
    ) {
        producer.sendToDlq(kafkaRecord, exception);

        return DeserializationHandlerResponse.CONTINUE;
    }

    @Override
    public void configure(Map<String, ?> configs) {
        producer = SpringContext.getBean(DlqProducer.class);
    }
}

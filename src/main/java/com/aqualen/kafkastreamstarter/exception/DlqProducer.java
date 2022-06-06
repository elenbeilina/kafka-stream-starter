package com.aqualen.kafkastreamstarter.exception;

import com.aqualen.kafkastreamstarter.log.SimpleLog;
import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Dead letter queue producer with overloaded methods
 * for each of the record variations.
 */
@Component
@RequiredArgsConstructor
public class DlqProducer {

    private final KafkaTemplate<byte[], byte[]> kafkaTemplate;
    private final KafkaStreamsProperties properties;

    @SimpleLog
    public void sendToDlq(ProducerRecord<byte[], byte[]> kafkaRecord, Exception exception) {
        kafkaTemplate.send(constructRecord(kafkaRecord.key(), kafkaRecord.value(), exception));
    }

    @SimpleLog
    public void sendToDlq(ConsumerRecord<byte[], byte[]> kafkaRecord, Exception exception) {
        kafkaTemplate.send(constructRecord(kafkaRecord.key(), kafkaRecord.value(), exception));
    }

    @SimpleLog
    public void sendToDlq(Record<String, String> kafkaRecord, Exception exception) {
        kafkaTemplate.send(constructRecord(
                getBytes(kafkaRecord.key()),
                kafkaRecord.value().getBytes(StandardCharsets.UTF_8),
                exception));
    }

    @SimpleLog
    public void sendToDlq(KeyValue<String, String> kafkaRecord, Exception exception) {
        kafkaTemplate.send(constructRecord(
                getBytes(kafkaRecord.key),
                kafkaRecord.value.getBytes(StandardCharsets.UTF_8),
                exception));
    }

    private ProducerRecord<byte[], byte[]> constructRecord(
            byte[] key, byte[] value, Exception ex
    ) {
        return new ProducerRecord<>(properties.getDlqTopic(), null,
                key, value, new RecordHeaders(new RecordHeader[]{
                new RecordHeader("error", ex.toString().getBytes(StandardCharsets.UTF_8))
        }));
    }

    private byte[] getBytes(String key) {
        return Objects.nonNull(key) ? key.getBytes(StandardCharsets.UTF_8) : null;
    }
}

package com.aqualen.kafkastreamstarter.dlq;

import com.aqualen.kafkastreamstarter.config.KafkaProducerTestConfig;
import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.streams.KeyValue;
import org.json.JSONException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = KafkaProducerTestConfig.class)
class DlqProducerTest {

    BlockingQueue<ConsumerRecord<byte[], byte[]>> records;
    KafkaMessageListenerContainer<byte[], byte[]> container;
    @Autowired
    private EmbeddedKafkaBroker broker;
    @Autowired
    private DlqProducer kafkaProducer;
    @Autowired
    private KafkaStreamsProperties properties;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", broker));
        DefaultKafkaConsumerFactory<byte[], byte[]> consumerFactory =
                new DefaultKafkaConsumerFactory<>(configs, new ByteArrayDeserializer(), new ByteArrayDeserializer());
        ContainerProperties containerProperties = new ContainerProperties(properties.getDlqTopic());
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<byte[], byte[]>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, broker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @Test
    void sendToDlqProducerRecord() throws Exception {
        kafkaProducer.sendToDlq(new ProducerRecord<>(
                "test1", "test2".getBytes(StandardCharsets.UTF_8)), new JSONException("Test!"));

        receivedCheck();
    }

    @Test
    void sendToDlqConsumerRecord() throws Exception {
        kafkaProducer.sendToDlq(new ConsumerRecord<>(
                "test1", 0, 0, null, "test2".getBytes(StandardCharsets.UTF_8)), new JSONException("Test!"));

        receivedCheck();
    }

    @Test
    void sendToDlqKeyValue() throws Exception {
        kafkaProducer.sendToDlq(new KeyValue<>(
                "key", "test2"), new JSONException("Test!"));

        receivedCheck();
    }

    private void receivedCheck() throws InterruptedException {
        ConsumerRecord<byte[], byte[]> singleRecord = records.poll(200, TimeUnit.MILLISECONDS);
        assertThat(singleRecord).isNotNull();
        assertThat(new String(singleRecord.headers().lastHeader("error").value()))
                .isEqualTo("org.json.JSONException: Test!");
        assertThat(new String(singleRecord.value()))
                .isEqualTo("test2");
    }

}
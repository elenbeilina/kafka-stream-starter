package com.aqualen.kafkastreamstarter.config;

import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

public class KafkaProducerTestConfig {

    @Bean
    public EmbeddedKafkaBroker broker() {
        return new EmbeddedKafkaBroker(1);
    }

    @Bean
    public KafkaTemplate<byte[], byte[]> template(EmbeddedKafkaBroker broker) {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean
    public KafkaStreamsProperties properties() {
        return KafkaStreamsProperties.builder()
                .dlqTopic("test")
                .build();
    }

    @Bean
    public DlqProducer producer(KafkaTemplate<byte[], byte[]> template,
                                KafkaStreamsProperties properties) {
        return new DlqProducer(template, properties);
    }
}

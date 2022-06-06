package com.aqualen.kafkastreamstarter.config;

import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;
import com.aqualen.kafkastreamstarter.util.SslUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaStreamsProperties.class)
public class KafkaConfig {

    private final KafkaStreamsProperties streamsProperties;

    @Bean
    @ConditionalOnMissingBean
    public KafkaTemplate<byte[], byte[]> kafkaTemplate(KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();

        if (SslUtil.isSslDisabled(streamsProperties.getSsl())) {
            SslUtil.disableSsl(props);
        }

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @Bean
    @ConditionalOnMissingBean(DlqProducer.class)
    DlqProducer dlqProducer(KafkaStreamsProperties kafkaStreamsProperties,
                            KafkaTemplate<byte[], byte[]> kafkaTemplate) {
        return new DlqProducer(kafkaTemplate, kafkaStreamsProperties);
    }

}

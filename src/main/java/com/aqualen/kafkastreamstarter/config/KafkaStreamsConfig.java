package com.aqualen.kafkastreamstarter.config;

import com.aqualen.kafkastreamstarter.exception.DlqDeserializationExceptionHandler;
import com.aqualen.kafkastreamstarter.exception.DlqProductionExceptionHandler;
import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;
import com.aqualen.kafkastreamstarter.util.SslUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaStreamsProperties.class)
public class KafkaStreamsConfig {

    private final KafkaStreamsProperties kafkaStreamsProperties;

    @Bean
    @ConditionalOnMissingBean
    KafkaStreamsConfiguration defaultKafkaStreamsConfig(KafkaProperties configuredKafkaProperties) {
        Map<String, Object> configProps = configuredKafkaProperties.buildStreamsProperties();

        if (SslUtil.isSslDisabled(kafkaStreamsProperties.getSsl())) {
            SslUtil.disableSsl(configProps);
        }

        configProps.put(
                StreamsConfig.DEFAULT_PRODUCTION_EXCEPTION_HANDLER_CLASS_CONFIG,
                DlqProductionExceptionHandler.class
        );
        configProps.put(
                StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
                DlqDeserializationExceptionHandler.class
        );

        configProps.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        configProps.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        return new KafkaStreamsConfiguration(configProps);
    }
}

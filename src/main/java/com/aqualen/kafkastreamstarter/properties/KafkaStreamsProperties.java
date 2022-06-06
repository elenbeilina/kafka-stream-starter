package com.aqualen.kafkastreamstarter.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@Validated
@Configuration
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka")
public class KafkaStreamsProperties {

    private Ssl ssl;

    @NotBlank
    private String sourceTopic;
    @NotBlank
    private String sinkTopic;
    @NotBlank
    private String dlqTopic;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ssl {
        private boolean enabled;
    }

}

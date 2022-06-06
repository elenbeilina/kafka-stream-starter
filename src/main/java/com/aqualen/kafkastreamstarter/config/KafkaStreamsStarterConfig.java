package com.aqualen.kafkastreamstarter.config;

import com.aqualen.kafkastreamstarter.exception.DlqAspect;
import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.log.SimpleLogAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class KafkaStreamsStarterConfig {

    private final DlqProducer dlqProducer;

    @Bean
    SpringContext springContext() {
        return new SpringContext();
    }

    @Bean
    SimpleLogAspect simpleLogAspect() {
        return new SimpleLogAspect();
    }

    @Bean
    DlqAspect dlqAspect() {
        return new DlqAspect(dlqProducer);
    }
}

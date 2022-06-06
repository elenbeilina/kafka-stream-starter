package com.aqualen.kafkastreamstarter.config;

import com.aqualen.kafkastreamstarter.exception.DlqAspect;
import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.log.SimpleLogAspect;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DlqConfig {

    @Bean
    public DlqProducer producer() {
        return Mockito.mock(DlqProducer.class);
    }

    @Bean
    DlqAspect aspect(DlqProducer producer) {
        return new DlqAspect(producer);
    }

    @Bean
    SimpleLogAspect simpleLogAspect() {
        return new SimpleLogAspect();
    }
}

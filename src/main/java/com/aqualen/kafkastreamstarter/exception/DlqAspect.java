package com.aqualen.kafkastreamstarter.exception;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.kafka.streams.KeyValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aspect for dlq.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DlqAspect {

    private final DlqProducer producer;

    /**
     * Advice for sending to DLQ, if exception happened
     *
     * @param joinPoint args must be String key, String value
     */
    @SneakyThrows
    @Around(value = "@annotation(com.aqualen.kafkastreamstarter.exception.Dlq)")
    public Object sendToDlq(ProceedingJoinPoint joinPoint) {
        List<Object> keyValue = Arrays.stream(joinPoint.getArgs()).collect(Collectors.toList());
        String key = (String) keyValue.get(0);
        String value = (String) keyValue.get(1);

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            producer.sendToDlq(new KeyValue<>(key, value), e);
            return null;
        }
    }
}

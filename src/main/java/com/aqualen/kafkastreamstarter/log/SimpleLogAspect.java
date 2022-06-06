package com.aqualen.kafkastreamstarter.log;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Aspect for simplifying logging process.
 */
@Slf4j
@Aspect
@Component
public class SimpleLogAspect {

    @SneakyThrows
    @Around("@annotation(com.aqualen.kafkastreamstarter.log.SimpleLog)")
    public Object logMethod(ProceedingJoinPoint joinPoint) {

        String template = "%s with parameters (%s): execution %s";
        String signature = joinPoint.getSignature().toShortString();
        String values = getParametersValues(joinPoint);

        log.info(String.format(template, signature, values, "started"));
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        log.info(String.format(template, signature, values,
                "finished in " + (System.currentTimeMillis() - start) + " ms"));

        return proceed;
    }

    private static String getParametersValues(ProceedingJoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();

        if (arguments == null || arguments.length == 0) {
            return "no parameters";
        }

        return Stream.of(arguments).map(o -> o == null ? "null" : o.toString())
                .collect(Collectors.joining(", "));
    }
}

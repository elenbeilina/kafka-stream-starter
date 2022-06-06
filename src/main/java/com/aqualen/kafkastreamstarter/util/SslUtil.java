package com.aqualen.kafkastreamstarter.util;

import com.aqualen.kafkastreamstarter.properties.KafkaStreamsProperties;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SslUtil {

    private static final Predicate<String> sslRelatedProps = key -> key.contains("ssl") || key.contains("security");

    private SslUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void disableSsl(Map<String, Object> configProps) {
        Set<String> sslRelatedValues = configProps.keySet().stream()
                .filter(sslRelatedProps)
                .collect(Collectors.toSet());
        sslRelatedValues.forEach(configProps::remove);
    }

    public static boolean isSslDisabled(KafkaStreamsProperties.Ssl ssl) {
        return !(Objects.nonNull(ssl) &&
                ssl.isEnabled());
    }

}

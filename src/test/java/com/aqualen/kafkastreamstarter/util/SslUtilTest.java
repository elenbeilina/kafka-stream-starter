package com.aqualen.kafkastreamstarter.util;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SslUtilTest {

    @Test
    @SneakyThrows
    void testConstructor() {
        Constructor<SslUtil> constructor = SslUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}
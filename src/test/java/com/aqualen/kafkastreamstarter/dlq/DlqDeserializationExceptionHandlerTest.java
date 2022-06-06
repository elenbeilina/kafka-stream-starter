package com.aqualen.kafkastreamstarter.dlq;

import com.aqualen.kafkastreamstarter.exception.DlqDeserializationExceptionHandler;
import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import org.apache.kafka.streams.errors.DeserializationExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DlqDeserializationExceptionHandlerTest {

    @Mock
    private DlqProducer producer;
    @InjectMocks
    private DlqDeserializationExceptionHandler handler;

    @Test
    void handle() {
        DeserializationExceptionHandler.DeserializationHandlerResponse result =
                handler.handle(null, null, null);

        assertThat(result).isNotNull();
    }

}
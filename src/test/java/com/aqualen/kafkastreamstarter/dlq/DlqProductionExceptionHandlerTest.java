package com.aqualen.kafkastreamstarter.dlq;

import com.aqualen.kafkastreamstarter.exception.DlqProducer;
import com.aqualen.kafkastreamstarter.exception.DlqProductionExceptionHandler;
import org.apache.kafka.streams.errors.ProductionExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DlqProductionExceptionHandlerTest {

    @Mock
    private DlqProducer producer;
    @InjectMocks
    private DlqProductionExceptionHandler handler;

    @Test
    void handle() {
        ProductionExceptionHandler.ProductionExceptionHandlerResponse result =
                handler.handle(null, null);

        assertThat(result).isNotNull();
    }
}
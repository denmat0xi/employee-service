package com.example.employeeservice.producer;

import com.example.employeeservice.event.EmployeeCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test suite for {@link EmployeeEventProducerImpl}.
 * Verifies that events are correctly serialized and published to the Kafka broker.
 */
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"employee.service.employeeData"})
class EmployeeEventProducerImplTest {

    @Autowired
    private EmployeeEventProducerImpl producer;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    private KafkaMessageListenerContainer<String, String> container;
    private BlockingQueue<ConsumerRecord<String, String>> records;

    /**
     * Configuration to ensure correct Jackson operation with Java 8 date/time types.
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
    }

    /**
     * Dynamically sets the Kafka bootstrap server address for the test.
     */
    @DynamicPropertySource
    static void setKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> "${spring.embedded.kafka.brokers}");
    }

    @BeforeEach
    void setUp() {
        records = new LinkedBlockingQueue<>();
        ContainerProperties containerProperties = new ContainerProperties("employee.service.employeeData");
        containerProperties.setMessageListener((MessageListener<String, String>) records::add);

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(embeddedKafkaBroker, "test-group", true);
        DefaultKafkaConsumerFactory<String, String> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer());

        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        container.start();

        ContainerTestUtils.waitForAssignment(container, 1);
        records.clear();
    }

    @AfterEach
    void tearDown() {
        container.stop();
    }

    @Test
    @DisplayName("Should successfully publish EmployeeCreatedEvent to Kafka")
    void shouldPublishEventSuccessfully() throws Exception {
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(123L, "test@example.com", LocalDateTime.now());

        producer.sendEmployeeCreatedEvent(event);

        ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);
        assertThat(received).isNotNull();

        EmployeeCreatedEvent receivedEvent = objectMapper.readValue(received.value(), EmployeeCreatedEvent.class);
        assertThat(receivedEvent.employeeId()).isEqualTo(123L);
        assertThat(receivedEvent.email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should correctly handle sequential sending of multiple messages")
    void shouldHandleMultipleMessages() throws Exception {
        producer.sendEmployeeCreatedEvent(new EmployeeCreatedEvent(1L, "first@example.com", LocalDateTime.now()));
        producer.sendEmployeeCreatedEvent(new EmployeeCreatedEvent(2L, "second@example.com", LocalDateTime.now()));

        ConsumerRecord<String, String> rec1 = records.poll(5, TimeUnit.SECONDS);
        ConsumerRecord<String, String> rec2 = records.poll(5, TimeUnit.SECONDS);

        assertThat(rec1).isNotNull();
        assertThat(rec2).isNotNull();
        assertThat(objectMapper.readValue(rec1.value(), EmployeeCreatedEvent.class).employeeId()).isEqualTo(1L);
        assertThat(objectMapper.readValue(rec2.value(), EmployeeCreatedEvent.class).employeeId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("Should correctly serialize events with long strings")
    void shouldSerializeCorrectlyWithLongStrings() throws Exception {
        String longEmail = "a".repeat(100) + "@example.com";
        EmployeeCreatedEvent event = new EmployeeCreatedEvent(999L, longEmail, LocalDateTime.now());

        producer.sendEmployeeCreatedEvent(event);

        ConsumerRecord<String, String> record = records.poll(5, TimeUnit.SECONDS);
        assertThat(record).isNotNull();

        EmployeeCreatedEvent received = objectMapper.readValue(record.value(), EmployeeCreatedEvent.class);
        assertThat(received.email()).isEqualTo(longEmail);
    }
}
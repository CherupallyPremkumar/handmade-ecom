package com.handmade.ecommerce.notification.service;

import com.handmade.ecommerce.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitMQNotificationServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    private RabbitMQNotificationService notificationService;

    private static final String EXCHANGE = "test.exchange";
    private static final String ROUTING_KEY = "test.routing.key";

    @BeforeEach
    void setUp() {
        notificationService = new RabbitMQNotificationService(rabbitTemplate);
        ReflectionTestUtils.setField(notificationService, "notificationExchange", EXCHANGE);
        ReflectionTestUtils.setField(notificationService, "notificationRoutingKey", ROUTING_KEY);
    }

    @Test
    void sendCustomerNotification_shouldCreateAndSendNotification() {
        // Given
        String customerId = "customer123";
        String subject = "Test Subject";
        String message = "Test Message";
        String notificationType = "TEST_TYPE";

        // When
        notificationService.sendCustomerNotification(customerId, subject, message, notificationType);

        // Then
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), notificationCaptor.capture());
        
        Notification capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification);
        assertNotNull(capturedNotification.getId());
        assertEquals(customerId, capturedNotification.getRecipientId());
        assertEquals("CUSTOMER", capturedNotification.getRecipientType());
        assertEquals(subject, capturedNotification.getSubject());
        assertEquals(message, capturedNotification.getMessage());
        assertEquals(notificationType, capturedNotification.getNotificationType());
        assertEquals("PENDING", capturedNotification.getStatus());
        assertNotNull(capturedNotification.getTimestamp());
    }

    @Test
    void sendArtisanNotification_shouldCreateAndSendNotification() {
        // Given
        String artisanId = "artisan456";
        String subject = "Test Subject";
        String message = "Test Message";
        String notificationType = "TEST_TYPE";

        // When
        notificationService.sendArtisanNotification(artisanId, subject, message, notificationType);

        // Then
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), notificationCaptor.capture());
        
        Notification capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification);
        assertEquals(artisanId, capturedNotification.getRecipientId());
        assertEquals("ARTISAN", capturedNotification.getRecipientType());
    }

    @Test
    void sendAdminNotification_shouldCreateAndSendNotification() {
        // Given
        String subject = "Test Admin Subject";
        String message = "Test Admin Message";
        String notificationType = "ADMIN_TEST_TYPE";

        // When
        notificationService.sendAdminNotification(subject, message, notificationType);

        // Then
        verify(rabbitTemplate).convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), notificationCaptor.capture());
        
        Notification capturedNotification = notificationCaptor.getValue();
        assertNotNull(capturedNotification);
        assertEquals("ADMIN", capturedNotification.getRecipientId());
        assertEquals("ADMIN", capturedNotification.getRecipientType());
    }

    @Test
    void sendNotification_shouldHandleException() {
        // Given
        String customerId = "customer123";
        String subject = "Test Subject";
        String message = "Test Message";
        String notificationType = "TEST_TYPE";
        
        doThrow(new RuntimeException("Test exception"))
            .when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(Notification.class));

        // When/Then - should not throw exception
        assertDoesNotThrow(() -> {
            notificationService.sendCustomerNotification(customerId, subject, message, notificationType);
        });
    }
} 
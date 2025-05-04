package com.handmade.ecommerce.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a notification to be sent to a recipient.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification implements Serializable {
    
    private String id;
    private String recipientId;
    private String recipientType; // CUSTOMER, ARTISAN, ADMIN
    private String subject;
    private String message;
    private String notificationType; // ORDER_CREATED, ORDER_UPDATED, SHIPPING_UPDATE, etc.
    private String status; // PENDING, SENT, FAILED
    private LocalDateTime timestamp;
    private LocalDateTime sentTimestamp;
    private Integer retryCount;
} 
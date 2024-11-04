package com.swpgavf.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Reference to the associated order
    private Long amount; // Amount in cents
    private String currency; // Currency code (e.g., "usd")
    private String status; // Payment status (e.g., "succeeded", "pending", "failed")
    private String paymentIntentId; // Stripe payment intent ID
    private LocalDateTime createdAt; // Timestamp for when the payment was created
}

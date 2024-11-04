package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private String paymentIntentId;
    private String status; // Payment status
    private String clientSecret; // Required for client-side confirmation
}

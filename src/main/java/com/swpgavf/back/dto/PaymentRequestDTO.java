package com.swpgavf.back.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String paymentMethodId; // Payment method ID from the client
}

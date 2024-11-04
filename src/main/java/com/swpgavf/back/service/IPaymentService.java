package com.swpgavf.back.service;

import com.stripe.model.PaymentIntent;
import com.swpgavf.back.dto.PaymentResponseDTO;
import com.swpgavf.back.entity.Order;

public interface IPaymentService {
    PaymentResponseDTO createPaymentIntent(Order order, String paymentMethodId) throws Exception;
}

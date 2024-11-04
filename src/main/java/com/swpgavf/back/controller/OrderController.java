package com.swpgavf.back.controller;

import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.dto.PaymentRequestDTO;
import com.swpgavf.back.dto.PaymentResponseDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.service.IOrderService;
import com.swpgavf.back.service.IPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;
    private final IPaymentService paymentService;

    public OrderController(IOrderService orderService, IPaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO createdOrder = orderService.create(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponseDTO> payOrder(
            @PathVariable Long orderId,
            @RequestBody PaymentRequestDTO paymentRequestDTO) {

        // Validate paymentRequestDTO
        if (paymentRequestDTO.getPaymentMethodId() == null || paymentRequestDTO.getPaymentMethodId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        Order order = orderService.findOrderById(orderId); // Fetch order by ID

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Handle order not found
        }

        try {
            PaymentResponseDTO paymentResponse = paymentService.createPaymentIntent(order, paymentRequestDTO.getPaymentMethodId());
            orderService.updateOrderStatus(orderId, "PAID"); // Update order status
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            // Log the error and return a bad request response
            System.err.println("Payment processing failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}

package com.swpgavf.back.controller;

import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.dto.PaymentRequestDTO;
import com.swpgavf.back.dto.PaymentResponseDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.service.IOrderService;
import com.swpgavf.back.service.IPaymentService;
import com.swpgavf.back.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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

    @GetMapping("/excel")
    public void generateExcel(
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.ms-excel");
        String headerValue;


        if (fecha != null) {
            headerValue = "attachment; filename=ReporteVentas_" + fecha + ".xlsx";
        } else if (startDate != null && endDate != null) {
            headerValue = "attachment; filename=ReporteVentas_" + startDate + "_a_" + endDate + ".xlsx";
        } else {
            headerValue = "attachment; filename=ReporteVentas.xlsx";
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);


        if (fecha != null) {
            orderService.generateExcel(response, fecha, fecha);
        } else if (startDate != null && endDate != null) {
            orderService.generateExcel(response, startDate, endDate);
        } else {
            throw new IllegalArgumentException("Debe proporcionar una fecha o un rango de fechas válido.");
        }
    }



    @GetMapping("/pdf")
    public void generatePdf(
            @RequestParam(required = false) LocalDate fecha,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");

        String headerValue;
        if (fecha != null) {
            headerValue = "attachment; filename=" + fecha + "-reporte.pdf";
        } else {
            headerValue = "attachment; filename=" + startDate + "-a-" + endDate + "-reporte.pdf";
        }

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, headerValue);

        if (fecha != null) {
            orderService.generatePDF(response, fecha, fecha);
        } else {
            orderService.generatePDF(response, startDate, endDate);
        }
    }

}

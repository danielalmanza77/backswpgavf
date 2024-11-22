package com.swpgavf.back.service;

import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.entity.Order;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.filters.ExpiresFilter;

import java.io.IOException;
import java.util.List;

public interface IOrderService {
    OrderResponseDTO create(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getAllOrders();

    Order findOrderById(Long orderId);

    void updateOrderStatus(Long orderId, String status);

    void generateExcel (HttpServletResponse response) throws IOException;
}

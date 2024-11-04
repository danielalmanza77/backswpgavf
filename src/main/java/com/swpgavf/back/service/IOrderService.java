package com.swpgavf.back.service;

import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.entity.Order;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO create(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getAllOrders();

    Order findOrderById(Long orderId);

    void updateOrderStatus(Long orderId, String status);
}

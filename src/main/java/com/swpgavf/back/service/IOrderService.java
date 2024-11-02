package com.swpgavf.back.service;

import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;

import java.util.List;

public interface IOrderService {
    OrderResponseDTO create(OrderRequestDTO orderRequestDTO);

    List<OrderResponseDTO> getAllOrders();
}

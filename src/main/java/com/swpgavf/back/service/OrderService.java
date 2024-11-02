package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.repository.IOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public OrderService(IOrderRepository orderRepository, ObjectMapper objectMapper, ProductService productService) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @Override
    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        //gets dto, map to entity , then save the entity , return tp dto
        Order order = mapToEntity(orderRequestDTO);
        orderRepository.save(order);
        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        //gets entities returns them as dtos
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private OrderResponseDTO mapToDTO(Order order) {
        return objectMapper.convertValue(order, OrderResponseDTO.class);
    }

    private Order mapToEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(orderRequestDTO.getStatus());

        // Use getProductEntityById to get Product entities for the order
        List<Product> products = orderRequestDTO.getProductIds().stream()
                .map(productService::getProductEntityById)
                .toList();

        order.setProducts(products);
        return order;
    }
}

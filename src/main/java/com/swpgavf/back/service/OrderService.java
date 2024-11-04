package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.exception.ResourceNotFoundException; // Create this custom exception
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
        Order order = mapToEntity(orderRequestDTO);
        orderRepository.save(order);
        return mapToDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = findOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order); // Persist the updated order
    }

    private OrderResponseDTO mapToDTO(Order order) {
        return objectMapper.convertValue(order, OrderResponseDTO.class);
    }

    private Order mapToEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(orderRequestDTO.getStatus());

        // Use getProductsEntityByIds to fetch all products at once
        List<Product> products = productService.getProductsEntityByIds(orderRequestDTO.getProductIds());
        order.setProducts(products);

        order.setProducts(products);

        System.out.println("Products: " + products);

        // Calculate total amount based on products
        order.calculateTotalAmount(); // Call the method to set the amount
        System.out.println("Calculated amount: " + order.getAmount());

        // Set the currency if it's not already set in the DTO
        order.setCurrency("usd"); // or retrieve it from somewhere if needed

        return order;
    }
}

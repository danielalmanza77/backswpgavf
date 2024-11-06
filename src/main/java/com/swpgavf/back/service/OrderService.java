package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.OrderRequestDTO;
import com.swpgavf.back.dto.OrderResponseDTO;
import com.swpgavf.back.dto.ProductOrderItemDTO;
import com.swpgavf.back.entity.Order;
import com.swpgavf.back.entity.OrderItem;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.exception.ResourceNotFoundException;
import com.swpgavf.back.repository.IOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        orderRepository.save(order);  // Persist the updated order
    }

    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setOrderDate(order.getOrderDate());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setAmount(order.getAmount());
        responseDTO.setCurrency(order.getCurrency());

        // Map order items (product + quantity) to ProductOrderItemDTO
        List<ProductOrderItemDTO> productOrderItems = order.getOrderItems().stream()
                .map(orderItem -> {
                    Product product = orderItem.getProduct();
                    ProductOrderItemDTO itemDTO = new ProductOrderItemDTO();
                    itemDTO.setId(product.getId());
                    itemDTO.setSku(product.getSku());
                    itemDTO.setName(product.getName());
                    itemDTO.setDescription(product.getDescription());
                    itemDTO.setCategory(product.getCategory());
                    itemDTO.setStock(product.getStock());
                    itemDTO.setPrice(product.getPrice());
                    itemDTO.setBrand(product.getBrand());
                    itemDTO.setImageUrls(product.getImageUrls());
                    itemDTO.setAvailable(product.getAvailable());
                    itemDTO.setQuantity(orderItem.getQuantity()); // Set quantity
                    return itemDTO;
                })
                .collect(Collectors.toList());

        responseDTO.setProducts(productOrderItems);
        return responseDTO;
    }

    private Order mapToEntity(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setOrderDate(orderRequestDTO.getOrderDate());
        order.setStatus(orderRequestDTO.getStatus());

        // Fetch products and create the corresponding OrderItem objects
        List<OrderItem> orderItems = orderRequestDTO.getProducts().stream()
                .map(orderItemDTO -> {
                    // Assuming the OrderItemDTO has getProductId() and getQuantity()
                    Product product = productService.getProductEntityById(orderItemDTO.getProductId()); // Adjust this line if productId is named differently in OrderItemDTO

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(product); // Set the product
                    orderItem.setQuantity(orderItemDTO.getQuantity()); // Set the quantity from OrderItemDTO
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems); // Make sure your Order entity has a field for the list of OrderItems
        order.calculateTotalAmount(); // This should calculate the total amount based on the OrderItems
        order.setCurrency("usd"); // Or dynamically set the currency

        return order;
    }

}

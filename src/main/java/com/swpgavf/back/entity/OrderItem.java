package com.swpgavf.back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;  // Reference to the product in the order

    private Integer quantity;  // Quantity of the product in the order

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;  // Reference to the order this item belongs to

    @Override
    public String toString() {
        return "OrderItem{id=" + id + ", quantity=" + quantity + ", product=" + product.getId() + "}";
    }
}

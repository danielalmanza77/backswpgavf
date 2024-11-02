package com.swpgavf.back.controller;

import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.service.IProductService;
import com.swpgavf.back.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getById(id));
    }
}

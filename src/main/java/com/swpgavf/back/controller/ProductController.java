package com.swpgavf.back.controller;

import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.service.IProductService;
import com.swpgavf.back.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    private final IProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO productRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/presigned-urls")
    public List<String> getPresignedUrls(@RequestBody List<String> imagePaths) {
        System.out.println("Received image paths: " + imagePaths);
        return productService.generatePresignedUrls(imagePaths);
    }

    @PatchMapping("/{id}/availability")
    public void updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        productService.updateAvailability(id, available);
    }


    @PostMapping("/{id}/availability")
    public void toggleAvailability(@PathVariable Long id) {
        productService.toggleAvailability(id);
    }
}

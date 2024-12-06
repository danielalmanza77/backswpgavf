package com.swpgavf.back.controller;

import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.dto.ReviewResponseDTO;
import com.swpgavf.back.dto.StockReductionDTO;
import com.swpgavf.back.entity.Product;
import com.swpgavf.back.repository.IProductRepository;
import com.swpgavf.back.service.IProductService;
import com.swpgavf.back.service.IReviewService;
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
    private final IReviewService reviewService;
    private final IProductRepository productRepository;

    public ProductController(ProductService productService, IReviewService reviewService, IProductRepository productRepository) {
        this.productService = productService;
        this.reviewService = reviewService;
        this.productRepository = productRepository;
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

    @GetMapping("/reviews/{id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsOfProductByProductId (@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.getReviewsOfProductByProductId(id));
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<String> reduceStock(@RequestBody List<StockReductionDTO> stockReductionList) {
        try {
            // Iterate over the list of stock reductions
            for (StockReductionDTO stockReduction : stockReductionList) {
                Product product = productService.getProductEntityById(stockReduction.getProductId());

                if (product != null) {
                    int newStock = product.getStock() - stockReduction.getQuantity();

                    // Check if there is enough stock
                    if (newStock < 0) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("No hay suficiente stock para el producto con ID: " + product.getId());
                    }

                    // Update the stock
                    product.setStock(newStock);
                    productRepository.save(product);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Producto no encontrado con ID: " + stockReduction.getProductId());
                }
            }

            return ResponseEntity.ok("Stock reducido con éxito para los productos");
        } catch (Exception e) {
            // Handle errors and return a bad request response
            System.err.println("Error reduciendo el stock: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ocurrió un error al reducir el stock");
        }
    }
}

package com.swpgavf.back.service;

import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;
import com.swpgavf.back.entity.Product;

import java.util.List;

public interface IProductService {
    ProductResponseDTO create(ProductRequestDTO productRequestDTO);

    ProductResponseDTO getById(Long id);

    List<String> generatePresignedUrls(List<String> imagePaths);

    List<ProductResponseDTO> getAll();

    void updateAvailability(Long id, boolean available);

    void delete(Long id);

    void toggleAvailability(Long id);

    void actualizarStock(Long idProducto, int cantidad);

    Product getProductEntityById(Long id);
}

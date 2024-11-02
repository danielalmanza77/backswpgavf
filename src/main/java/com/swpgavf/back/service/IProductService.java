package com.swpgavf.back.service;

import com.swpgavf.back.dto.ProductRequestDTO;
import com.swpgavf.back.dto.ProductResponseDTO;

public interface IProductService {
    ProductResponseDTO create(ProductRequestDTO productRequestDTO);

    ProductResponseDTO getById(Long id);
}

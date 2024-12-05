package com.swpgavf.back.controller;

import com.swpgavf.back.dto.MovimientoKardexDTO;
import com.swpgavf.back.service.IProductService;
import com.swpgavf.back.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kardex")
public class KardexController {

    private final IProductService productService;

    public KardexController(ProductService productService) {
        this.productService = productService;
    }


    @PostMapping("/movimiento")
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoKardexDTO movimiento) {
        try {
            productService.actualizarStock(movimiento.getIdProducto(), movimiento.getCantidad());
            return ResponseEntity.ok("Movimiento registrado con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

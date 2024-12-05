package com.swpgavf.back.controller;

import com.swpgavf.back.dto.KardexRequestDTO;
import com.swpgavf.back.dto.KardexResponseDTO;
import com.swpgavf.back.dto.MovimientoKardexDTO;
import com.swpgavf.back.service.IKardexService;
import com.swpgavf.back.service.IProductService;
import com.swpgavf.back.service.ProductService;
import com.swpgavf.back.service.KardexService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kardex")
public class KardexController {

    private final IProductService productService;
    private final IKardexService kardexService;

    public KardexController(ProductService productService, KardexService kardexService) {
        this.productService = productService;
        this.kardexService = kardexService;
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

    @PostMapping
    public ResponseEntity<KardexResponseDTO> addKardexEntry(@RequestBody KardexRequestDTO kardexRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kardexService.addKardexEntry(kardexRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<KardexResponseDTO>> getAllKardexEntries() {
        return ResponseEntity.status(HttpStatus.OK).body(kardexService.getAllKardexEntries());
    }

}

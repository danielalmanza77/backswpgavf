package com.swpgavf.back.service;

import com.swpgavf.back.dto.KardexRequestDTO;
import com.swpgavf.back.dto.KardexResponseDTO;

import java.util.List;

public interface IKardexService {
    KardexResponseDTO addKardexEntry(KardexRequestDTO kardexRequestDTO);

    List<KardexResponseDTO> getAllKardexEntries();
}

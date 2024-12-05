package com.swpgavf.back.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swpgavf.back.dto.KardexRequestDTO;
import com.swpgavf.back.dto.KardexResponseDTO;
import com.swpgavf.back.entity.Kardex;
import com.swpgavf.back.repository.IKardexRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KardexService implements IKardexService {

    private final IKardexRepository kardexRepository;
    private final ObjectMapper objectMapper;

    public KardexService(IKardexRepository kardexRepository, ObjectMapper objectMapper) {
        this.kardexRepository = kardexRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public KardexResponseDTO addKardexEntry(KardexRequestDTO kardexRequestDTO) {
        Kardex kardex = mapToEntity(kardexRequestDTO);
        System.out.println("Setting createdAt: " + kardexRequestDTO.getCreatedAt());
        kardexRepository.save(kardex);
        return mapToDTO(kardex);
    }

    @Override
    public List<KardexResponseDTO> getAllKardexEntries() {
        return kardexRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private KardexResponseDTO mapToDTO(Kardex kardex) {
        return objectMapper.convertValue(kardex, KardexResponseDTO.class);
    }

    private Kardex mapToEntity(KardexRequestDTO kardexRequestDTO) {
        return objectMapper.convertValue(kardexRequestDTO, Kardex.class);
    }
}

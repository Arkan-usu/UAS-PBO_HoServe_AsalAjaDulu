package com.hoserve.service;

import com.hoserve.client.util.DtoMapper;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.entity.Dokter;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PoliklinikRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi DokterService.
 */
@Service
@Transactional(readOnly = true)
public class DokterServiceImpl implements DokterService {

    private final DokterRepository dokterRepository;
    private final PoliklinikRepository poliklinikRepository;

    public DokterServiceImpl(DokterRepository dokterRepository,
                             PoliklinikRepository poliklinikRepository) {
        this.dokterRepository = dokterRepository;
        this.poliklinikRepository = poliklinikRepository;
    }

    @Override
    public List<DokterResponse> getDokterByPoliklinik(Long poliklinikId) {
        return dokterRepository.findByPoliklinikId(poliklinikId)
                .stream()
                .map(DtoMapper::toDokterResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DokterResponse> getAllDokter() {
        return dokterRepository.findAll()
                .stream()
                .map(DtoMapper::toDokterResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DokterResponse getDokterById(Long id) {
        Dokter dokter = dokterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "id", id));
        return DtoMapper.toDokterResponse(dokter);
    }

    @Override
    public List<PoliklinikResponse> getAllPoliklinik() {
        return poliklinikRepository.findAll()
                .stream()
                .map(DtoMapper::toPoliklinikResponse)
                .collect(Collectors.toList());
    }
}

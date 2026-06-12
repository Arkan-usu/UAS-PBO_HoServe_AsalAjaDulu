package com.hoserve.service;

import com.hoserve.client.util.DtoMapper;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.entity.Pasien;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.PasienRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi PasienService.
 * Menerapkan POLYMORPHISM (method overloading pada cariPasien).
 */
@Service
@Transactional(readOnly = true)
public class PasienServiceImpl implements PasienService {

    private final PasienRepository pasienRepository;

    public PasienServiceImpl(PasienRepository pasienRepository) {
        this.pasienRepository = pasienRepository;
    }

    /**
     * POLYMORPHISM: Cari pasien berdasarkan ID (Long).
     */
    @Override
    public PasienResponse cariPasien(Long id) {
        Pasien pasien = pasienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "id", id));
        return DtoMapper.toPasienResponse(pasien);
    }

    /**
     * POLYMORPHISM: Cari pasien berdasarkan nama (String).
     * Method overloading - nama method sama, parameter berbeda.
     */
    @Override
    public List<PasienResponse> cariPasien(String nama) {
        return pasienRepository.findByNamaContainingIgnoreCase(nama)
                .stream()
                .map(DtoMapper::toPasienResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PasienResponse getProfilByUsername(String username) {
        Pasien pasien = pasienRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "username", username));
        return DtoMapper.toPasienResponse(pasien);
    }
}

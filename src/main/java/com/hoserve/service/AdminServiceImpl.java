package com.hoserve.service;

import com.hoserve.client.util.DtoMapper;
import com.hoserve.dto.request.DokterRequest;
import com.hoserve.dto.request.PasienRequest;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.Pasien;
import com.hoserve.entity.Poliklinik;
import com.hoserve.exception.BadRequestException;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PasienRepository;
import com.hoserve.repository.PoliklinikRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi AdminService.
 * Berisi logika bisnis untuk operasi CRUD oleh Admin.
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private final DokterRepository dokterRepository;
    private final PasienRepository pasienRepository;
    private final PoliklinikRepository poliklinikRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(DokterRepository dokterRepository,
                            PasienRepository pasienRepository,
                            PoliklinikRepository poliklinikRepository,
                            PasswordEncoder passwordEncoder) {
        this.dokterRepository = dokterRepository;
        this.pasienRepository = pasienRepository;
        this.poliklinikRepository = poliklinikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== DOKTER ====================

    @Override
    public DokterResponse tambahDokter(DokterRequest request) {
        if (dokterRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username sudah digunakan: " + request.getUsername());
        }

        Dokter dokter = new Dokter();
        dokter.setNama(request.getNama());
        dokter.setEmail(request.getEmail());
        dokter.setUsername(request.getUsername());
        dokter.setPassword(passwordEncoder.encode(request.getPassword()));
        dokter.setSpesialisasi(request.getSpesialisasi());
        dokter.setNomorTelepon(request.getNomorTelepon());

        if (request.getPoliklinikId() != null) {
            Poliklinik poliklinik = poliklinikRepository.findById(request.getPoliklinikId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poliklinik", "id", request.getPoliklinikId()));
            dokter.setPoliklinik(poliklinik);
        }

        Dokter saved = dokterRepository.save(dokter);
        return DtoMapper.toDokterResponse(saved);
    }

    @Override
    public DokterResponse updateDokter(Long id, DokterRequest request) {
        Dokter dokter = dokterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "id", id));

        dokter.setNama(request.getNama());
        dokter.setEmail(request.getEmail());
        dokter.setSpesialisasi(request.getSpesialisasi());
        dokter.setNomorTelepon(request.getNomorTelepon());

        // Update password hanya jika diisi
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            dokter.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getPoliklinikId() != null) {
            Poliklinik poliklinik = poliklinikRepository.findById(request.getPoliklinikId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poliklinik", "id", request.getPoliklinikId()));
            dokter.setPoliklinik(poliklinik);
        }

        Dokter updated = dokterRepository.save(dokter);
        return DtoMapper.toDokterResponse(updated);
    }

    @Override
    public void hapusDokter(Long id) {
        if (!dokterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dokter", "id", id);
        }
        dokterRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DokterResponse getDokterById(Long id) {
        Dokter dokter = dokterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "id", id));
        return DtoMapper.toDokterResponse(dokter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DokterResponse> getAllDokter() {
        return dokterRepository.findAll()
                .stream()
                .map(DtoMapper::toDokterResponse)
                .collect(Collectors.toList());
    }

    // ==================== PASIEN ====================

    @Override
    public PasienResponse updatePasien(Long id, PasienRequest request) {
        Pasien pasien = pasienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "id", id));

        pasien.setNama(request.getNama());
        pasien.setEmail(request.getEmail());
        pasien.setTanggalLahir(request.getTanggalLahir());
        pasien.setAlamat(request.getAlamat());
        pasien.setNomorTelepon(request.getNomorTelepon());

        Pasien updated = pasienRepository.save(pasien);
        return DtoMapper.toPasienResponse(updated);
    }

    @Override
    public void hapusPasien(Long id) {
        if (!pasienRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pasien", "id", id);
        }
        pasienRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PasienResponse getPasienById(Long id) {
        Pasien pasien = pasienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "id", id));
        return DtoMapper.toPasienResponse(pasien);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PasienResponse> getAllPasien() {
        return pasienRepository.findAll()
                .stream()
                .map(DtoMapper::toPasienResponse)
                .collect(Collectors.toList());
    }

    // ==================== POLIKLINIK ====================

    @Override
    public PoliklinikResponse tambahPoliklinik(Poliklinik request) {
        if (poliklinikRepository.existsByNamaPoliklinik(request.getNamaPoliklinik())) {
            throw new BadRequestException("Poliklinik sudah ada: " + request.getNamaPoliklinik());
        }

        Poliklinik saved = poliklinikRepository.save(request);
        return DtoMapper.toPoliklinikResponse(saved);
    }

    @Override
    public PoliklinikResponse updatePoliklinik(Long id, Poliklinik request) {
        Poliklinik poliklinik = poliklinikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poliklinik", "id", id));

        poliklinik.setNamaPoliklinik(request.getNamaPoliklinik());
        poliklinik.setDeskripsi(request.getDeskripsi());
        poliklinik.setLokasi(request.getLokasi());

        Poliklinik updated = poliklinikRepository.save(poliklinik);
        return DtoMapper.toPoliklinikResponse(updated);
    }

    @Override
    public void hapusPoliklinik(Long id) {
        if (!poliklinikRepository.existsById(id)) {
            throw new ResourceNotFoundException("Poliklinik", "id", id);
        }
        poliklinikRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PoliklinikResponse getPoliklinikById(Long id) {
        Poliklinik poliklinik = poliklinikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Poliklinik", "id", id));
        return DtoMapper.toPoliklinikResponse(poliklinik);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PoliklinikResponse> getAllPoliklinik() {
        return poliklinikRepository.findAll()
                .stream()
                .map(DtoMapper::toPoliklinikResponse)
                .collect(Collectors.toList());
    }
}

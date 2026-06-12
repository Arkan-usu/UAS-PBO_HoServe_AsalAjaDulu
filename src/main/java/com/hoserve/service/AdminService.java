package com.hoserve.service;

import com.hoserve.dto.request.DokterRequest;
import com.hoserve.dto.request.PasienRequest;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.entity.Poliklinik;

import java.util.List;

/**
 * Interface Service untuk operasi Admin (CRUD).
 * Menerapkan ABSTRACTION.
 */
public interface AdminService {

    // === Dokter Management ===
    DokterResponse tambahDokter(DokterRequest request);
    DokterResponse updateDokter(Long id, DokterRequest request);
    void hapusDokter(Long id);
    DokterResponse getDokterById(Long id);
    List<DokterResponse> getAllDokter();

    // === Pasien Management ===
    PasienResponse updatePasien(Long id, PasienRequest request);
    void hapusPasien(Long id);
    PasienResponse getPasienById(Long id);
    List<PasienResponse> getAllPasien();

    // === Poliklinik Management ===
    PoliklinikResponse tambahPoliklinik(Poliklinik poliklinik);
    PoliklinikResponse updatePoliklinik(Long id, Poliklinik poliklinik);
    void hapusPoliklinik(Long id);
    PoliklinikResponse getPoliklinikById(Long id);
    List<PoliklinikResponse> getAllPoliklinik();
}

package com.hoserve.service;

import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PoliklinikResponse;

import java.util.List;

/**
 * Interface Service untuk operasi Dokter.
 * Menerapkan ABSTRACTION.
 */
public interface DokterService {

    /**
     * Mendapatkan daftar dokter berdasarkan poliklinik.
     */
    List<DokterResponse> getDokterByPoliklinik(Long poliklinikId);

    /**
     * Mendapatkan semua dokter.
     */
    List<DokterResponse> getAllDokter();

    /**
     * Mendapatkan detail dokter berdasarkan ID.
     */
    DokterResponse getDokterById(Long id);

    /**
     * Mendapatkan semua poliklinik.
     */
    List<PoliklinikResponse> getAllPoliklinik();
}

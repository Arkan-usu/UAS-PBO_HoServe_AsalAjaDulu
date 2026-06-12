package com.hoserve.service;

import com.hoserve.dto.response.PasienResponse;

import java.util.List;

/**
 * Interface Service untuk operasi Pasien.
 * Menerapkan ABSTRACTION dan POLYMORPHISM (method overloading).
 */
public interface PasienService {

    /**
     * Cari pasien berdasarkan ID.
     * POLYMORPHISM: Method overloading - cariPasien(Long id)
     */
    PasienResponse cariPasien(Long id);

    /**
     * Cari pasien berdasarkan nama.
     * POLYMORPHISM: Method overloading - cariPasien(String nama)
     */
    List<PasienResponse> cariPasien(String nama);

    /**
     * Mendapatkan profil pasien berdasarkan username (dari JWT token).
     */
    PasienResponse getProfilByUsername(String username);
}

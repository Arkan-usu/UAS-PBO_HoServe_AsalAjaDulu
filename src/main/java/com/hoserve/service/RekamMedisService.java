package com.hoserve.service;

import com.hoserve.dto.request.RekamMedisRequest;
import com.hoserve.dto.response.RekamMedisResponse;

import java.util.List;

/**
 * Interface Service untuk operasi Rekam Medis.
 * Menerapkan ABSTRACTION.
 */
public interface RekamMedisService {

    /**
     * Simpan rekam medis baru dan update status reservasi menjadi SELESAI.
     */
    RekamMedisResponse simpanRekamMedis(Long dokterId, RekamMedisRequest request);

    /**
     * Mendapatkan riwayat medis pasien.
     */
    List<RekamMedisResponse> getRiwayatMedis(Long pasienId);

    /**
     * Mendapatkan rekam medis berdasarkan ID reservasi.
     */
    RekamMedisResponse getRekamMedisByReservasi(Long reservasiId);
}

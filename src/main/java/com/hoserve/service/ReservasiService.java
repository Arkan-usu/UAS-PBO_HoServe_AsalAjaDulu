package com.hoserve.service;

import com.hoserve.dto.request.ReservasiRequest;
import com.hoserve.dto.response.ReservasiResponse;
import com.hoserve.enums.StatusReservasi;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface Service untuk operasi Reservasi.
 * Menerapkan ABSTRACTION.
 */
public interface ReservasiService {

    /**
     * Buat reservasi baru oleh pasien.
     */
    ReservasiResponse buatReservasi(Long pasienId, ReservasiRequest request);

    /**
     * Mendapatkan riwayat reservasi pasien.
     */
    List<ReservasiResponse> getRiwayatReservasi(Long pasienId);

    /**
     * Mendapatkan daftar antrean dokter pada tanggal tertentu.
     */
    List<ReservasiResponse> getAntreanDokter(Long dokterId, LocalDate tanggal);

    /**
     * Update status reservasi.
     */
    ReservasiResponse updateStatusReservasi(Long reservasiId, StatusReservasi status);
}

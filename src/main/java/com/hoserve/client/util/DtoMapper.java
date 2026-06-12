package com.hoserve.client.util;

import com.hoserve.dto.response.*;
import com.hoserve.entity.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class untuk konversi Entity <-> DTO.
 * Menghindari circular reference dan mengontrol data yang dikirim ke client.
 */
public class DtoMapper {

    private DtoMapper() {
        // Utility class, prevent instantiation
    }

    // ==================== DOKTER ====================

    public static DokterResponse toDokterResponse(Dokter dokter) {
        DokterResponse response = new DokterResponse();
        response.setId(dokter.getId());
        response.setNama(dokter.getNama());
        response.setEmail(dokter.getEmail());
        response.setUsername(dokter.getUsername());
        response.setSpesialisasi(dokter.getSpesialisasi());
        response.setNomorTelepon(dokter.getNomorTelepon());

        if (dokter.getPoliklinik() != null) {
            response.setNamaPoliklinik(dokter.getPoliklinik().getNamaPoliklinik());
            response.setPoliklinikId(dokter.getPoliklinik().getId());
        }

        if (dokter.getJadwalList() != null) {
            List<DokterResponse.JadwalResponse> jadwalResponses = dokter.getJadwalList()
                    .stream()
                    .map(DtoMapper::toJadwalResponse)
                    .collect(Collectors.toList());
            response.setJadwalList(jadwalResponses);
        }

        return response;
    }

    public static DokterResponse.JadwalResponse toJadwalResponse(JadwalDokter jadwal) {
        DokterResponse.JadwalResponse response = new DokterResponse.JadwalResponse();
        response.setId(jadwal.getId());
        response.setHari(jadwal.getHari().name());
        response.setJamMulai(jadwal.getJamMulai().toString());
        response.setJamSelesai(jadwal.getJamSelesai().toString());
        return response;
    }

    // ==================== PASIEN ====================

    public static PasienResponse toPasienResponse(Pasien pasien) {
        PasienResponse response = new PasienResponse();
        response.setId(pasien.getId());
        response.setNama(pasien.getNama());
        response.setEmail(pasien.getEmail());
        response.setUsername(pasien.getUsername());
        response.setTanggalLahir(pasien.getTanggalLahir());
        response.setAlamat(pasien.getAlamat());
        response.setNomorTelepon(pasien.getNomorTelepon());
        return response;
    }

    // ==================== POLIKLINIK ====================

    public static PoliklinikResponse toPoliklinikResponse(Poliklinik poliklinik) {
        PoliklinikResponse response = new PoliklinikResponse();
        response.setId(poliklinik.getId());
        response.setNamaPoliklinik(poliklinik.getNamaPoliklinik());
        response.setDeskripsi(poliklinik.getDeskripsi());
        response.setLokasi(poliklinik.getLokasi());
        response.setJumlahDokter(
                poliklinik.getDokterList() != null ? poliklinik.getDokterList().size() : 0
        );
        return response;
    }

    // ==================== RESERVASI ====================

    public static ReservasiResponse toReservasiResponse(Reservasi reservasi) {
        ReservasiResponse response = new ReservasiResponse();
        response.setId(reservasi.getId());
        response.setTanggalReservasi(reservasi.getTanggalReservasi());
        response.setJamReservasi(reservasi.getJamReservasi());
        response.setNomorAntrean(reservasi.getNomorAntrean());
        response.setStatus(reservasi.getStatus().name());
        response.setKeluhan(reservasi.getKeluhan());

        if (reservasi.getPasien() != null) {
            response.setNamaPasien(reservasi.getPasien().getNama());
            response.setPasienId(reservasi.getPasien().getId());
        }

        if (reservasi.getDokter() != null) {
            response.setNamaDokter(reservasi.getDokter().getNama());
            response.setDokterId(reservasi.getDokter().getId());
        }

        if (reservasi.getPoliklinik() != null) {
            response.setNamaPoliklinik(reservasi.getPoliklinik().getNamaPoliklinik());
            response.setPoliklinikId(reservasi.getPoliklinik().getId());
        }

        return response;
    }

    // ==================== REKAM MEDIS ====================

    public static RekamMedisResponse toRekamMedisResponse(RekamMedis rekamMedis) {
        RekamMedisResponse response = new RekamMedisResponse();
        response.setId(rekamMedis.getId());
        response.setTanggalPemeriksaan(rekamMedis.getTanggalPemeriksaan());
        response.setKeluhan(rekamMedis.getKeluhan());
        response.setDiagnosis(rekamMedis.getDiagnosis());
        response.setTindakan(rekamMedis.getTindakan());
        response.setResepObat(rekamMedis.getResepObat());
        response.setCatatan(rekamMedis.getCatatan());

        if (rekamMedis.getPasien() != null) {
            response.setNamaPasien(rekamMedis.getPasien().getNama());
            response.setPasienId(rekamMedis.getPasien().getId());
        }

        if (rekamMedis.getDokter() != null) {
            response.setNamaDokter(rekamMedis.getDokter().getNama());
            response.setDokterId(rekamMedis.getDokter().getId());
        }

        if (rekamMedis.getReservasi() != null) {
            response.setReservasiId(rekamMedis.getReservasi().getId());
        }

        return response;
    }
}

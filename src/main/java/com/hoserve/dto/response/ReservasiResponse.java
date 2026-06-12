package com.hoserve.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO response untuk data reservasi.
 */
public class ReservasiResponse {

    private Long id;
    private LocalDate tanggalReservasi;
    private LocalTime jamReservasi;
    private Integer nomorAntrean;
    private String status;
    private String keluhan;
    private String namaPasien;
    private Long pasienId;
    private String namaDokter;
    private Long dokterId;
    private String namaPoliklinik;
    private Long poliklinikId;

    // === Constructors ===

    public ReservasiResponse() {
    }

    // === Getter & Setter ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTanggalReservasi() {
        return tanggalReservasi;
    }

    public void setTanggalReservasi(LocalDate tanggalReservasi) {
        this.tanggalReservasi = tanggalReservasi;
    }

    public LocalTime getJamReservasi() {
        return jamReservasi;
    }

    public void setJamReservasi(LocalTime jamReservasi) {
        this.jamReservasi = jamReservasi;
    }

    public Integer getNomorAntrean() {
        return nomorAntrean;
    }

    public void setNomorAntrean(Integer nomorAntrean) {
        this.nomorAntrean = nomorAntrean;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public Long getPasienId() {
        return pasienId;
    }

    public void setPasienId(Long pasienId) {
        this.pasienId = pasienId;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }

    public Long getDokterId() {
        return dokterId;
    }

    public void setDokterId(Long dokterId) {
        this.dokterId = dokterId;
    }

    public String getNamaPoliklinik() {
        return namaPoliklinik;
    }

    public void setNamaPoliklinik(String namaPoliklinik) {
        this.namaPoliklinik = namaPoliklinik;
    }

    public Long getPoliklinikId() {
        return poliklinikId;
    }

    public void setPoliklinikId(Long poliklinikId) {
        this.poliklinikId = poliklinikId;
    }
}

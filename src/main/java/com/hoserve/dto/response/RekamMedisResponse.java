package com.hoserve.dto.response;

import java.time.LocalDateTime;

/**
 * DTO response untuk data rekam medis.
 */
public class RekamMedisResponse {

    private Long id;
    private LocalDateTime tanggalPemeriksaan;
    private String keluhan;
    private String diagnosis;
    private String tindakan;
    private String resepObat;
    private String catatan;
    private String namaPasien;
    private Long pasienId;
    private String namaDokter;
    private Long dokterId;
    private Long reservasiId;

    // === Constructors ===

    public RekamMedisResponse() {
    }

    // === Getter & Setter ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTanggalPemeriksaan() {
        return tanggalPemeriksaan;
    }

    public void setTanggalPemeriksaan(LocalDateTime tanggalPemeriksaan) {
        this.tanggalPemeriksaan = tanggalPemeriksaan;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTindakan() {
        return tindakan;
    }

    public void setTindakan(String tindakan) {
        this.tindakan = tindakan;
    }

    public String getResepObat() {
        return resepObat;
    }

    public void setResepObat(String resepObat) {
        this.resepObat = resepObat;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
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

    public Long getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(Long reservasiId) {
        this.reservasiId = reservasiId;
    }
}

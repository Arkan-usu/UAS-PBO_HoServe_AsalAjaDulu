package com.hoserve.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO untuk request pengisian rekam medis oleh dokter.
 */
public class RekamMedisRequest {

    @NotNull(message = "ID reservasi tidak boleh kosong")
    private Long reservasiId;

    @NotBlank(message = "Keluhan tidak boleh kosong")
    private String keluhan;

    @NotBlank(message = "Diagnosis tidak boleh kosong")
    private String diagnosis;

    private String tindakan;

    private String resepObat;

    private String catatan;

    // === Constructors ===

    public RekamMedisRequest() {
    }

    public RekamMedisRequest(Long reservasiId, String keluhan, String diagnosis,
                             String tindakan, String resepObat, String catatan) {
        this.reservasiId = reservasiId;
        this.keluhan = keluhan;
        this.diagnosis = diagnosis;
        this.tindakan = tindakan;
        this.resepObat = resepObat;
        this.catatan = catatan;
    }

    // === Getter & Setter ===

    public Long getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(Long reservasiId) {
        this.reservasiId = reservasiId;
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
}

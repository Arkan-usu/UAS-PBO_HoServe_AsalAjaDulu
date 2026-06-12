package com.hoserve.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO untuk request pembuatan reservasi.
 */
public class ReservasiRequest {

    @NotNull(message = "ID dokter tidak boleh kosong")
    private Long dokterId;

    private Long poliklinikId;

    @NotNull(message = "Tanggal reservasi tidak boleh kosong")
    private LocalDate tanggalReservasi;

    private LocalTime jamReservasi;

    @NotBlank(message = "Keluhan tidak boleh kosong")
    private String keluhan;

    // === Constructors ===

    public ReservasiRequest() {
    }

    public ReservasiRequest(Long dokterId, Long poliklinikId, LocalDate tanggalReservasi,
                            LocalTime jamReservasi, String keluhan) {
        this.dokterId = dokterId;
        this.poliklinikId = poliklinikId;
        this.tanggalReservasi = tanggalReservasi;
        this.jamReservasi = jamReservasi;
        this.keluhan = keluhan;
    }

    // === Getter & Setter ===

    public Long getDokterId() {
        return dokterId;
    }

    public void setDokterId(Long dokterId) {
        this.dokterId = dokterId;
    }

    public Long getPoliklinikId() {
        return poliklinikId;
    }

    public void setPoliklinikId(Long poliklinikId) {
        this.poliklinikId = poliklinikId;
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

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }
}

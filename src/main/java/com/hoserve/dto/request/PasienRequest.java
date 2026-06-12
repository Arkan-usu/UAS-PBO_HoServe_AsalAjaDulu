package com.hoserve.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * DTO untuk request update data pasien oleh admin.
 */
public class PasienRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @Email(message = "Format email tidak valid")
    private String email;

    private LocalDate tanggalLahir;

    private String alamat;

    private String nomorTelepon;

    // === Constructors ===

    public PasienRequest() {
    }

    public PasienRequest(String nama, String email, LocalDate tanggalLahir,
                         String alamat, String nomorTelepon) {
        this.nama = nama;
        this.email = email;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
    }

    // === Getter & Setter ===

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }
}

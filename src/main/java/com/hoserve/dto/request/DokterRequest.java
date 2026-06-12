package com.hoserve.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO untuk request tambah/update dokter oleh admin.
 */
public class DokterRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Username tidak boleh kosong")
    private String username;

    private String password;

    @NotBlank(message = "Spesialisasi tidak boleh kosong")
    private String spesialisasi;

    private String nomorTelepon;

    private Long poliklinikId;

    // === Constructors ===

    public DokterRequest() {
    }

    public DokterRequest(String nama, String email, String username, String password,
                         String spesialisasi, String nomorTelepon, Long poliklinikId) {
        this.nama = nama;
        this.email = email;
        this.username = username;
        this.password = password;
        this.spesialisasi = spesialisasi;
        this.nomorTelepon = nomorTelepon;
        this.poliklinikId = poliklinikId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public Long getPoliklinikId() {
        return poliklinikId;
    }

    public void setPoliklinikId(Long poliklinikId) {
        this.poliklinikId = poliklinikId;
    }
}

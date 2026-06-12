package com.hoserve.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * DTO untuk request registrasi pasien baru.
 */
public class RegisterRequest {

    @NotBlank(message = "Nama tidak boleh kosong")
    private String nama;

    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 4, max = 50, message = "Username harus antara 4-50 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    private LocalDate tanggalLahir;

    private String alamat;

    private String nomorTelepon;

    // === Constructors ===

    public RegisterRequest() {
    }

    public RegisterRequest(String nama, String email, String username, String password,
                           LocalDate tanggalLahir, String alamat, String nomorTelepon) {
        this.nama = nama;
        this.email = email;
        this.username = username;
        this.password = password;
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

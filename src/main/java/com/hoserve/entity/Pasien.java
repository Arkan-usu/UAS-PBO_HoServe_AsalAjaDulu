package com.hoserve.entity;

import com.hoserve.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Pasien - Child class dari User.
 * Menerapkan INHERITANCE (extends User), ENCAPSULATION, dan POLYMORPHISM.
 */
@Entity
@Table(name = "pasiens")
public class Pasien extends User {

    @Column(name = "tanggal_lahir")
    private LocalDate tanggalLahir;

    private String alamat;

    @Column(name = "nomor_telepon")
    private String nomorTelepon;

    @OneToMany(mappedBy = "pasien", cascade = CascadeType.ALL)
    private List<Reservasi> reservasiList = new ArrayList<>();

    // === Constructors ===

    public Pasien() {
        super();
        setRole(Role.PASIEN);
    }

    public Pasien(String nama, String email, String username, String password,
                  LocalDate tanggalLahir, String alamat, String nomorTelepon) {
        super(nama, email, username, password, Role.PASIEN);
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
    }

    // === Getter & Setter (ENCAPSULATION) ===

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

    public List<Reservasi> getReservasiList() {
        return reservasiList;
    }

    public void setReservasiList(List<Reservasi> reservasiList) {
        this.reservasiList = reservasiList;
    }

    // === POLYMORPHISM: Override method dari parent class ===

    @Override
    public String getDisplayInfo() {
        return String.format("Pasien: %s | Tgl Lahir: %s | Alamat: %s | Telp: %s",
                getNama(), tanggalLahir, alamat, nomorTelepon);
    }
}

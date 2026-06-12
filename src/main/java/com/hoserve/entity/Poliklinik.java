package com.hoserve.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Poliklinik - Merepresentasikan unit/departemen klinik.
 */
@Entity
@Table(name = "polikliniks")
public class Poliklinik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama poliklinik tidak boleh kosong")
    @Column(name = "nama_poliklinik", nullable = false, unique = true)
    private String namaPoliklinik;

    private String deskripsi;

    private String lokasi;

    @OneToMany(mappedBy = "poliklinik")
    private List<Dokter> dokterList = new ArrayList<>();

    // === Constructors ===

    public Poliklinik() {
    }

    public Poliklinik(String namaPoliklinik, String deskripsi, String lokasi) {
        this.namaPoliklinik = namaPoliklinik;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
    }

    // === Getter & Setter (ENCAPSULATION) ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaPoliklinik() {
        return namaPoliklinik;
    }

    public void setNamaPoliklinik(String namaPoliklinik) {
        this.namaPoliklinik = namaPoliklinik;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public List<Dokter> getDokterList() {
        return dokterList;
    }

    public void setDokterList(List<Dokter> dokterList) {
        this.dokterList = dokterList;
    }
}

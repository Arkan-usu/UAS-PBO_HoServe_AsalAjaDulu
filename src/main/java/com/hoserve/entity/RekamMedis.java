package com.hoserve.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Entity RekamMedis - Merepresentasikan catatan medis pasien.
 */
@Entity
@Table(name = "rekam_medis")
public class RekamMedis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tanggal_pemeriksaan", nullable = false)
    private LocalDateTime tanggalPemeriksaan;

    @NotBlank(message = "Keluhan tidak boleh kosong")
    @Column(nullable = false)
    private String keluhan;

    @NotBlank(message = "Diagnosis tidak boleh kosong")
    @Column(nullable = false)
    private String diagnosis;

    private String tindakan;

    @Column(name = "resep_obat")
    private String resepObat;

    private String catatan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasien_id", nullable = false)
    private Pasien pasien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservasi_id", unique = true)
    private Reservasi reservasi;

    // === Constructors ===

    public RekamMedis() {
        this.tanggalPemeriksaan = LocalDateTime.now();
    }

    public RekamMedis(String keluhan, String diagnosis, String tindakan, String resepObat,
                      String catatan, Pasien pasien, Dokter dokter, Reservasi reservasi) {
        this.tanggalPemeriksaan = LocalDateTime.now();
        this.keluhan = keluhan;
        this.diagnosis = diagnosis;
        this.tindakan = tindakan;
        this.resepObat = resepObat;
        this.catatan = catatan;
        this.pasien = pasien;
        this.dokter = dokter;
        this.reservasi = reservasi;
    }

    // === Getter & Setter (ENCAPSULATION) ===

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

    public Pasien getPasien() {
        return pasien;
    }

    public void setPasien(Pasien pasien) {
        this.pasien = pasien;
    }

    public Dokter getDokter() {
        return dokter;
    }

    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
    }

    public Reservasi getReservasi() {
        return reservasi;
    }

    public void setReservasi(Reservasi reservasi) {
        this.reservasi = reservasi;
    }
}

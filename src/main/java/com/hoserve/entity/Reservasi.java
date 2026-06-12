package com.hoserve.entity;

import com.hoserve.enums.StatusReservasi;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity Reservasi - Merepresentasikan pemesanan/booking pasien.
 */
@Entity
@Table(name = "reservasis")
public class Reservasi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tanggal reservasi tidak boleh kosong")
    @Column(name = "tanggal_reservasi", nullable = false)
    private LocalDate tanggalReservasi;

    @Column(name = "jam_reservasi")
    private LocalTime jamReservasi;

    @Column(name = "nomor_antrean")
    private Integer nomorAntrean;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReservasi status;

    @NotBlank(message = "Keluhan tidak boleh kosong")
    @Column(nullable = false)
    private String keluhan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pasien_id", nullable = false)
    private Pasien pasien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliklinik_id")
    private Poliklinik poliklinik;

    // === Constructors ===

    public Reservasi() {
        this.status = StatusReservasi.MENUNGGU;
    }

    public Reservasi(LocalDate tanggalReservasi, LocalTime jamReservasi, String keluhan,
                     Pasien pasien, Dokter dokter, Poliklinik poliklinik) {
        this.tanggalReservasi = tanggalReservasi;
        this.jamReservasi = jamReservasi;
        this.keluhan = keluhan;
        this.pasien = pasien;
        this.dokter = dokter;
        this.poliklinik = poliklinik;
        this.status = StatusReservasi.MENUNGGU;
    }

    // === Getter & Setter (ENCAPSULATION) ===

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

    public StatusReservasi getStatus() {
        return status;
    }

    public void setStatus(StatusReservasi status) {
        this.status = status;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
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

    public Poliklinik getPoliklinik() {
        return poliklinik;
    }

    public void setPoliklinik(Poliklinik poliklinik) {
        this.poliklinik = poliklinik;
    }
}

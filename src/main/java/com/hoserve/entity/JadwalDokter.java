package com.hoserve.entity;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Entity JadwalDokter - Merepresentasikan jadwal praktik dokter.
 */
@Entity
@Table(name = "jadwal_dokters")
public class JadwalDokter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek hari;

    @Column(name = "jam_mulai", nullable = false)
    private LocalTime jamMulai;

    @Column(name = "jam_selesai", nullable = false)
    private LocalTime jamSelesai;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dokter_id", nullable = false)
    private Dokter dokter;

    // === Constructors ===

    public JadwalDokter() {
    }

    public JadwalDokter(DayOfWeek hari, LocalTime jamMulai, LocalTime jamSelesai) {
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }

    // === Getter & Setter (ENCAPSULATION) ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getHari() {
        return hari;
    }

    public void setHari(DayOfWeek hari) {
        this.hari = hari;
    }

    public LocalTime getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(LocalTime jamMulai) {
        this.jamMulai = jamMulai;
    }

    public LocalTime getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(LocalTime jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public Dokter getDokter() {
        return dokter;
    }

    public void setDokter(Dokter dokter) {
        this.dokter = dokter;
    }
}

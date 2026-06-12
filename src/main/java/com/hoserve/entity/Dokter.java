package com.hoserve.entity;

import com.hoserve.enums.Role;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity Dokter - Child class dari User.
 * Menerapkan INHERITANCE (extends User), ENCAPSULATION, dan POLYMORPHISM.
 */
@Entity
@Table(name = "dokters")
public class Dokter extends User {

    @Column(nullable = false)
    private String spesialisasi;

    @Column(name = "nomor_telepon")
    private String nomorTelepon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poliklinik_id")
    private Poliklinik poliklinik;

    @OneToMany(mappedBy = "dokter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JadwalDokter> jadwalList = new ArrayList<>();

    // === Constructors ===

    public Dokter() {
        super();
        setRole(Role.DOKTER);
    }

    public Dokter(String nama, String email, String username, String password,
                  String spesialisasi, String nomorTelepon) {
        super(nama, email, username, password, Role.DOKTER);
        this.spesialisasi = spesialisasi;
        this.nomorTelepon = nomorTelepon;
    }

    // === Getter & Setter (ENCAPSULATION) ===

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

    public Poliklinik getPoliklinik() {
        return poliklinik;
    }

    public void setPoliklinik(Poliklinik poliklinik) {
        this.poliklinik = poliklinik;
    }

    public List<JadwalDokter> getJadwalList() {
        return jadwalList;
    }

    public void setJadwalList(List<JadwalDokter> jadwalList) {
        this.jadwalList = jadwalList;
    }

    // === Helper method ===

    public void addJadwal(JadwalDokter jadwal) {
        jadwalList.add(jadwal);
        jadwal.setDokter(this);
    }

    public void removeJadwal(JadwalDokter jadwal) {
        jadwalList.remove(jadwal);
        jadwal.setDokter(null);
    }

    // === POLYMORPHISM: Override method dari parent class ===

    @Override
    public String getDisplayInfo() {
        String poliNama = (poliklinik != null) ? poliklinik.getNamaPoliklinik() : "Belum ditentukan";
        return String.format("Dr. %s | Spesialisasi: %s | Poli: %s | Telp: %s",
                getNama(), spesialisasi, poliNama, nomorTelepon);
    }
}

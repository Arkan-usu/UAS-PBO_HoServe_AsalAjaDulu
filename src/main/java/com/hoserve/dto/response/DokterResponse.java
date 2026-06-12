package com.hoserve.dto.response;

import java.util.List;

/**
 * DTO response untuk data dokter.
 */
public class DokterResponse {

    private Long id;
    private String nama;
    private String email;
    private String username;
    private String spesialisasi;
    private String nomorTelepon;
    private String namaPoliklinik;
    private Long poliklinikId;
    private List<JadwalResponse> jadwalList;

    // === Constructors ===

    public DokterResponse() {
    }

    // === Getter & Setter ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getNamaPoliklinik() {
        return namaPoliklinik;
    }

    public void setNamaPoliklinik(String namaPoliklinik) {
        this.namaPoliklinik = namaPoliklinik;
    }

    public Long getPoliklinikId() {
        return poliklinikId;
    }

    public void setPoliklinikId(Long poliklinikId) {
        this.poliklinikId = poliklinikId;
    }

    public List<JadwalResponse> getJadwalList() {
        return jadwalList;
    }

    public void setJadwalList(List<JadwalResponse> jadwalList) {
        this.jadwalList = jadwalList;
    }

    // === Inner class untuk Jadwal ===

    public static class JadwalResponse {

        private Long id;
        private String hari;
        private String jamMulai;
        private String jamSelesai;

        public JadwalResponse() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getHari() {
            return hari;
        }

        public void setHari(String hari) {
            this.hari = hari;
        }

        public String getJamMulai() {
            return jamMulai;
        }

        public void setJamMulai(String jamMulai) {
            this.jamMulai = jamMulai;
        }

        public String getJamSelesai() {
            return jamSelesai;
        }

        public void setJamSelesai(String jamSelesai) {
            this.jamSelesai = jamSelesai;
        }
    }
}

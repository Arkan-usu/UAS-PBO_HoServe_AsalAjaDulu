package com.hoserve.dto.response;

/**
 * DTO response untuk data poliklinik.
 */
public class PoliklinikResponse {

    private Long id;
    private String namaPoliklinik;
    private String deskripsi;
    private String lokasi;
    private int jumlahDokter;

    // === Constructors ===

    public PoliklinikResponse() {
    }

    // === Getter & Setter ===

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

    public int getJumlahDokter() {
        return jumlahDokter;
    }

    public void setJumlahDokter(int jumlahDokter) {
        this.jumlahDokter = jumlahDokter;
    }
}

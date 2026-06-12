package com.hoserve.controller;

import com.hoserve.dto.request.DokterRequest;
import com.hoserve.dto.request.PasienRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.entity.Poliklinik;
import com.hoserve.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller untuk operasi Admin.
 * Semua endpoint memerlukan role ADMIN.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // ==================== DOKTER MANAGEMENT ====================

    /**
     * GET /api/admin/dokter - List semua dokter
     */
    @GetMapping("/dokter")
    public ResponseEntity<ApiResponse<List<DokterResponse>>> getAllDokter() {
        List<DokterResponse> dokterList = adminService.getAllDokter();
        return ResponseEntity.ok(ApiResponse.success("Data dokter berhasil diambil", dokterList));
    }

    /**
     * GET /api/admin/dokter/{id} - Detail dokter
     */
    @GetMapping("/dokter/{id}")
    public ResponseEntity<ApiResponse<DokterResponse>> getDokterById(@PathVariable Long id) {
        DokterResponse dokter = adminService.getDokterById(id);
        return ResponseEntity.ok(ApiResponse.success("Data dokter ditemukan", dokter));
    }

    /**
     * POST /api/admin/dokter - Tambah dokter baru
     */
    @PostMapping("/dokter")
    public ResponseEntity<ApiResponse<DokterResponse>> tambahDokter(@Valid @RequestBody DokterRequest request) {
        DokterResponse dokter = adminService.tambahDokter(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Dokter berhasil ditambahkan", dokter));
    }

    /**
     * PUT /api/admin/dokter/{id} - Update data dokter
     */
    @PutMapping("/dokter/{id}")
    public ResponseEntity<ApiResponse<DokterResponse>> updateDokter(
            @PathVariable Long id, @Valid @RequestBody DokterRequest request) {
        DokterResponse dokter = adminService.updateDokter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Data dokter berhasil diupdate", dokter));
    }

    /**
     * DELETE /api/admin/dokter/{id} - Hapus dokter
     */
    @DeleteMapping("/dokter/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusDokter(@PathVariable Long id) {
        adminService.hapusDokter(id);
        return ResponseEntity.ok(ApiResponse.success("Dokter berhasil dihapus"));
    }

    // ==================== PASIEN MANAGEMENT ====================

    /**
     * GET /api/admin/pasien - List semua pasien
     */
    @GetMapping("/pasien")
    public ResponseEntity<ApiResponse<List<PasienResponse>>> getAllPasien() {
        List<PasienResponse> pasienList = adminService.getAllPasien();
        return ResponseEntity.ok(ApiResponse.success("Data pasien berhasil diambil", pasienList));
    }

    /**
     * GET /api/admin/pasien/{id} - Detail pasien
     */
    @GetMapping("/pasien/{id}")
    public ResponseEntity<ApiResponse<PasienResponse>> getPasienById(@PathVariable Long id) {
        PasienResponse pasien = adminService.getPasienById(id);
        return ResponseEntity.ok(ApiResponse.success("Data pasien ditemukan", pasien));
    }

    /**
     * PUT /api/admin/pasien/{id} - Update data pasien
     */
    @PutMapping("/pasien/{id}")
    public ResponseEntity<ApiResponse<PasienResponse>> updatePasien(
            @PathVariable Long id, @Valid @RequestBody PasienRequest request) {
        PasienResponse pasien = adminService.updatePasien(id, request);
        return ResponseEntity.ok(ApiResponse.success("Data pasien berhasil diupdate", pasien));
    }

    /**
     * DELETE /api/admin/pasien/{id} - Hapus pasien
     */
    @DeleteMapping("/pasien/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusPasien(@PathVariable Long id) {
        adminService.hapusPasien(id);
        return ResponseEntity.ok(ApiResponse.success("Pasien berhasil dihapus"));
    }

    // ==================== POLIKLINIK MANAGEMENT ====================

    /**
     * GET /api/admin/poliklinik - List semua poliklinik
     */
    @GetMapping("/poliklinik")
    public ResponseEntity<ApiResponse<List<PoliklinikResponse>>> getAllPoliklinik() {
        List<PoliklinikResponse> poliklinikList = adminService.getAllPoliklinik();
        return ResponseEntity.ok(ApiResponse.success("Data poliklinik berhasil diambil", poliklinikList));
    }

    /**
     * POST /api/admin/poliklinik - Tambah poliklinik baru
     */
    @PostMapping("/poliklinik")
    public ResponseEntity<ApiResponse<PoliklinikResponse>> tambahPoliklinik(@Valid @RequestBody Poliklinik poliklinik) {
        PoliklinikResponse response = adminService.tambahPoliklinik(poliklinik);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Poliklinik berhasil ditambahkan", response));
    }

    /**
     * PUT /api/admin/poliklinik/{id} - Update poliklinik
     */
    @PutMapping("/poliklinik/{id}")
    public ResponseEntity<ApiResponse<PoliklinikResponse>> updatePoliklinik(
            @PathVariable Long id, @Valid @RequestBody Poliklinik poliklinik) {
        PoliklinikResponse response = adminService.updatePoliklinik(id, poliklinik);
        return ResponseEntity.ok(ApiResponse.success("Poliklinik berhasil diupdate", response));
    }

    /**
     * DELETE /api/admin/poliklinik/{id} - Hapus poliklinik
     */
    @DeleteMapping("/poliklinik/{id}")
    public ResponseEntity<ApiResponse<Void>> hapusPoliklinik(@PathVariable Long id) {
        adminService.hapusPoliklinik(id);
        return ResponseEntity.ok(ApiResponse.success("Poliklinik berhasil dihapus"));
    }
}

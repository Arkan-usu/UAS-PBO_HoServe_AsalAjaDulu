package com.hoserve.controller;

import com.hoserve.dto.request.ReservasiRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.dto.response.ReservasiResponse;
import com.hoserve.entity.Pasien;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.PasienRepository;
import com.hoserve.service.DokterService;
import com.hoserve.service.ReservasiService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller untuk operasi Pasien.
 * Semua endpoint memerlukan role PASIEN.
 */
@RestController
@RequestMapping("/api/pasien")
public class PasienController {

    private final ReservasiService reservasiService;
    private final DokterService dokterService;
    private final PasienRepository pasienRepository;

    public PasienController(ReservasiService reservasiService,
                            DokterService dokterService,
                            PasienRepository pasienRepository) {
        this.reservasiService = reservasiService;
        this.dokterService = dokterService;
        this.pasienRepository = pasienRepository;
    }

    /**
     * POST /api/pasien/reservasi - Buat reservasi baru
     */
    @PostMapping("/reservasi")
    public ResponseEntity<ApiResponse<ReservasiResponse>> buatReservasi(
            @Valid @RequestBody ReservasiRequest request,
            Authentication authentication) {

        Long pasienId = getPasienIdFromAuth(authentication);
        ReservasiResponse response = reservasiService.buatReservasi(pasienId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Reservasi berhasil dibuat. Nomor antrean Anda: " + response.getNomorAntrean(),
                        response));
    }

    /**
     * GET /api/pasien/reservasi/riwayat - Riwayat reservasi pasien
     */
    @GetMapping("/reservasi/riwayat")
    public ResponseEntity<ApiResponse<List<ReservasiResponse>>> getRiwayatReservasi(
            Authentication authentication) {

        Long pasienId = getPasienIdFromAuth(authentication);
        List<ReservasiResponse> riwayat = reservasiService.getRiwayatReservasi(pasienId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat reservasi berhasil diambil", riwayat));
    }

    /**
     * GET /api/pasien/dokter - List dokter (untuk pilih saat reservasi)
     */
    @GetMapping("/dokter")
    public ResponseEntity<ApiResponse<List<DokterResponse>>> getDokterList() {
        List<DokterResponse> dokterList = dokterService.getAllDokter();
        return ResponseEntity.ok(ApiResponse.success("Data dokter berhasil diambil", dokterList));
    }

    /**
     * GET /api/pasien/dokter/poliklinik/{poliklinikId} - Dokter berdasarkan poliklinik
     */
    @GetMapping("/dokter/poliklinik/{poliklinikId}")
    public ResponseEntity<ApiResponse<List<DokterResponse>>> getDokterByPoliklinik(
            @PathVariable Long poliklinikId) {
        List<DokterResponse> dokterList = dokterService.getDokterByPoliklinik(poliklinikId);
        return ResponseEntity.ok(ApiResponse.success("Data dokter berhasil diambil", dokterList));
    }

    /**
     * GET /api/pasien/poliklinik - List poliklinik
     */
    @GetMapping("/poliklinik")
    public ResponseEntity<ApiResponse<List<PoliklinikResponse>>> getPoliklinikList() {
        List<PoliklinikResponse> poliklinikList = dokterService.getAllPoliklinik();
        return ResponseEntity.ok(ApiResponse.success("Data poliklinik berhasil diambil", poliklinikList));
    }

    // === Helper ===

    private Long getPasienIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        Pasien pasien = pasienRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "username", username));
        return pasien.getId();
    }
}

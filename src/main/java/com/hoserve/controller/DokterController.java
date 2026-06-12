package com.hoserve.controller;

import com.hoserve.dto.request.RekamMedisRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.RekamMedisResponse;
import com.hoserve.dto.response.ReservasiResponse;
import com.hoserve.entity.Dokter;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.DokterRepository;
import com.hoserve.service.RekamMedisService;
import com.hoserve.service.ReservasiService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller untuk operasi Dokter.
 * Semua endpoint memerlukan role DOKTER.
 */
@RestController
@RequestMapping("/api/dokter")
public class DokterController {

    private final ReservasiService reservasiService;
    private final RekamMedisService rekamMedisService;
    private final DokterRepository dokterRepository;

    public DokterController(ReservasiService reservasiService,
                            RekamMedisService rekamMedisService,
                            DokterRepository dokterRepository) {
        this.reservasiService = reservasiService;
        this.rekamMedisService = rekamMedisService;
        this.dokterRepository = dokterRepository;
    }

    /**
     * GET /api/dokter/antrean - Daftar antrean pasien hari ini
     * Optional param: tanggal (default: hari ini)
     */
    @GetMapping("/antrean")
    public ResponseEntity<ApiResponse<List<ReservasiResponse>>> getAntrean(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tanggal,
            Authentication authentication) {

        Long dokterId = getDokterIdFromAuth(authentication);

        // Default ke hari ini jika tanggal tidak diisi
        if (tanggal == null) {
            tanggal = LocalDate.now();
        }

        List<ReservasiResponse> antrean = reservasiService.getAntreanDokter(dokterId, tanggal);
        return ResponseEntity.ok(
                ApiResponse.success("Daftar antrean berhasil diambil (" + tanggal + ")", antrean));
    }

    /**
     * POST /api/dokter/rekam-medis - Input rekam medis
     */
    @PostMapping("/rekam-medis")
    public ResponseEntity<ApiResponse<RekamMedisResponse>> inputRekamMedis(
            @Valid @RequestBody RekamMedisRequest request,
            Authentication authentication) {

        Long dokterId = getDokterIdFromAuth(authentication);
        RekamMedisResponse response = rekamMedisService.simpanRekamMedis(dokterId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rekam medis berhasil disimpan", response));
    }

    /**
     * GET /api/dokter/rekam-medis/pasien/{pasienId} - Riwayat medis pasien
     */
    @GetMapping("/rekam-medis/pasien/{pasienId}")
    public ResponseEntity<ApiResponse<List<RekamMedisResponse>>> getRiwayatMedis(
            @PathVariable Long pasienId) {

        List<RekamMedisResponse> riwayat = rekamMedisService.getRiwayatMedis(pasienId);
        return ResponseEntity.ok(ApiResponse.success("Riwayat medis pasien berhasil diambil", riwayat));
    }

    // === Helper ===

    private Long getDokterIdFromAuth(Authentication authentication) {
        String username = authentication.getName();
        Dokter dokter = dokterRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "username", username));
        return dokter.getId();
    }
}

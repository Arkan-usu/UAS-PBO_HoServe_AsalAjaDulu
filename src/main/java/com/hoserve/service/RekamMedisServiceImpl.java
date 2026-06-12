package com.hoserve.service;

import com.hoserve.client.util.DtoMapper;
import com.hoserve.dto.request.RekamMedisRequest;
import com.hoserve.dto.response.RekamMedisResponse;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.RekamMedis;
import com.hoserve.entity.Reservasi;
import com.hoserve.enums.StatusReservasi;
import com.hoserve.exception.BadRequestException;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.RekamMedisRepository;
import com.hoserve.repository.ReservasiRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi RekamMedisService.
 * Berisi logika bisnis untuk pengisian rekam medis oleh dokter.
 */
@Service
@Transactional
public class RekamMedisServiceImpl implements RekamMedisService {

    private final RekamMedisRepository rekamMedisRepository;
    private final ReservasiRepository reservasiRepository;
    private final DokterRepository dokterRepository;

    public RekamMedisServiceImpl(RekamMedisRepository rekamMedisRepository,
                                 ReservasiRepository reservasiRepository,
                                 DokterRepository dokterRepository) {
        this.rekamMedisRepository = rekamMedisRepository;
        this.reservasiRepository = reservasiRepository;
        this.dokterRepository = dokterRepository;
    }

    @Override
    public RekamMedisResponse simpanRekamMedis(Long dokterId, RekamMedisRequest request) {
        // Cari reservasi
        Reservasi reservasi = reservasiRepository.findById(request.getReservasiId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservasi", "id", request.getReservasiId()));

        // Validasi: reservasi harus milik dokter yang login
        if (!reservasi.getDokter().getId().equals(dokterId)) {
            throw new BadRequestException("Reservasi ini bukan milik dokter yang sedang login");
        }

        // Validasi: cek apakah sudah ada rekam medis untuk reservasi ini
        if (rekamMedisRepository.findByReservasiId(request.getReservasiId()).isPresent()) {
            throw new BadRequestException("Rekam medis untuk reservasi ini sudah ada");
        }

        // Cari dokter
        Dokter dokter = dokterRepository.findById(dokterId)
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "id", dokterId));

        // Buat rekam medis
        RekamMedis rekamMedis = new RekamMedis();
        rekamMedis.setTanggalPemeriksaan(LocalDateTime.now());
        rekamMedis.setKeluhan(request.getKeluhan());
        rekamMedis.setDiagnosis(request.getDiagnosis());
        rekamMedis.setTindakan(request.getTindakan());
        rekamMedis.setResepObat(request.getResepObat());
        rekamMedis.setCatatan(request.getCatatan());
        rekamMedis.setPasien(reservasi.getPasien());
        rekamMedis.setDokter(dokter);
        rekamMedis.setReservasi(reservasi);

        // Update status reservasi menjadi SELESAI
        reservasi.setStatus(StatusReservasi.SELESAI);
        reservasiRepository.save(reservasi);

        RekamMedis saved = rekamMedisRepository.save(rekamMedis);
        return DtoMapper.toRekamMedisResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RekamMedisResponse> getRiwayatMedis(Long pasienId) {
        return rekamMedisRepository.findByPasienIdOrderByTanggalPemeriksaanDesc(pasienId)
                .stream()
                .map(DtoMapper::toRekamMedisResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RekamMedisResponse getRekamMedisByReservasi(Long reservasiId) {
        RekamMedis rekamMedis = rekamMedisRepository.findByReservasiId(reservasiId)
                .orElseThrow(() -> new ResourceNotFoundException("RekamMedis", "reservasiId", reservasiId));
        return DtoMapper.toRekamMedisResponse(rekamMedis);
    }
}

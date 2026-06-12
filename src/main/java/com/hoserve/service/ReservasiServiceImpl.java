package com.hoserve.service;

import com.hoserve.client.util.DtoMapper;
import com.hoserve.dto.request.ReservasiRequest;
import com.hoserve.dto.response.ReservasiResponse;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.Pasien;
import com.hoserve.entity.Poliklinik;
import com.hoserve.entity.Reservasi;
import com.hoserve.enums.StatusReservasi;
import com.hoserve.exception.BadRequestException;
import com.hoserve.exception.ResourceNotFoundException;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PasienRepository;
import com.hoserve.repository.PoliklinikRepository;
import com.hoserve.repository.ReservasiRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementasi ReservasiService.
 * Berisi logika bisnis untuk reservasi: cek jadwal, hitung antrean, dll.
 */
@Service
@Transactional
public class ReservasiServiceImpl implements ReservasiService {

    private final ReservasiRepository reservasiRepository;
    private final PasienRepository pasienRepository;
    private final DokterRepository dokterRepository;
    private final PoliklinikRepository poliklinikRepository;

    public ReservasiServiceImpl(ReservasiRepository reservasiRepository,
                                PasienRepository pasienRepository,
                                DokterRepository dokterRepository,
                                PoliklinikRepository poliklinikRepository) {
        this.reservasiRepository = reservasiRepository;
        this.pasienRepository = pasienRepository;
        this.dokterRepository = dokterRepository;
        this.poliklinikRepository = poliklinikRepository;
    }

    @Override
    public ReservasiResponse buatReservasi(Long pasienId, ReservasiRequest request) {
        // Validasi tanggal tidak boleh di masa lalu
        if (request.getTanggalReservasi().isBefore(LocalDate.now())) {
            throw new BadRequestException("Tanggal reservasi tidak boleh di masa lalu");
        }

        // Cari pasien
        Pasien pasien = pasienRepository.findById(pasienId)
                .orElseThrow(() -> new ResourceNotFoundException("Pasien", "id", pasienId));

        // Cari dokter
        Dokter dokter = dokterRepository.findById(request.getDokterId())
                .orElseThrow(() -> new ResourceNotFoundException("Dokter", "id", request.getDokterId()));

        // Cari poliklinik (dari dokter jika tidak diisi)
        Poliklinik poliklinik = null;
        if (request.getPoliklinikId() != null) {
            poliklinik = poliklinikRepository.findById(request.getPoliklinikId())
                    .orElseThrow(() -> new ResourceNotFoundException("Poliklinik", "id", request.getPoliklinikId()));
        } else if (dokter.getPoliklinik() != null) {
            poliklinik = dokter.getPoliklinik();
        }

        // Hitung nomor antrean otomatis
        int jumlahReservasi = reservasiRepository.countByDokterIdAndTanggalReservasi(
                dokter.getId(), request.getTanggalReservasi());
        int nomorAntrean = jumlahReservasi + 1;

        // Buat reservasi
        Reservasi reservasi = new Reservasi();
        reservasi.setTanggalReservasi(request.getTanggalReservasi());
        reservasi.setJamReservasi(request.getJamReservasi());
        reservasi.setKeluhan(request.getKeluhan());
        reservasi.setNomorAntrean(nomorAntrean);
        reservasi.setStatus(StatusReservasi.MENUNGGU);
        reservasi.setPasien(pasien);
        reservasi.setDokter(dokter);
        reservasi.setPoliklinik(poliklinik);

        Reservasi saved = reservasiRepository.save(reservasi);
        return DtoMapper.toReservasiResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservasiResponse> getRiwayatReservasi(Long pasienId) {
        return reservasiRepository.findByPasienIdOrderByTanggalReservasiDesc(pasienId)
                .stream()
                .map(DtoMapper::toReservasiResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservasiResponse> getAntreanDokter(Long dokterId, LocalDate tanggal) {
        // Tampilkan semua reservasi kecuali yang DIBATALKAN
        return reservasiRepository.findByDokterIdAndTanggalReservasiAndStatusNot(
                        dokterId, tanggal, StatusReservasi.DIBATALKAN)
                .stream()
                .map(DtoMapper::toReservasiResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ReservasiResponse updateStatusReservasi(Long reservasiId, StatusReservasi status) {
        Reservasi reservasi = reservasiRepository.findById(reservasiId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservasi", "id", reservasiId));

        reservasi.setStatus(status);
        Reservasi updated = reservasiRepository.save(reservasi);
        return DtoMapper.toReservasiResponse(updated);
    }
}

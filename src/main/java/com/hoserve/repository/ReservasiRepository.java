package com.hoserve.repository;

import com.hoserve.entity.Reservasi;
import com.hoserve.enums.StatusReservasi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository untuk entity Reservasi.
 */
@Repository
public interface ReservasiRepository extends JpaRepository<Reservasi, Long> {

    List<Reservasi> findByPasienId(Long pasienId);

    List<Reservasi> findByPasienIdOrderByTanggalReservasiDesc(Long pasienId);

    List<Reservasi> findByDokterIdAndTanggalReservasi(Long dokterId, LocalDate tanggal);

    List<Reservasi> findByDokterIdAndTanggalReservasiAndStatusNot(
            Long dokterId, LocalDate tanggal, StatusReservasi status);

    int countByDokterIdAndTanggalReservasi(Long dokterId, LocalDate tanggal);

    List<Reservasi> findByStatus(StatusReservasi status);
}

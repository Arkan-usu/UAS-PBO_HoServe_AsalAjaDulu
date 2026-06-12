package com.hoserve.repository;

import com.hoserve.entity.JadwalDokter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.DayOfWeek;
import java.util.List;

/**
 * Repository untuk entity JadwalDokter.
 */
@Repository
public interface JadwalDokterRepository extends JpaRepository<JadwalDokter, Long> {

    List<JadwalDokter> findByDokterId(Long dokterId);

    List<JadwalDokter> findByDokterIdAndHari(Long dokterId, DayOfWeek hari);
}

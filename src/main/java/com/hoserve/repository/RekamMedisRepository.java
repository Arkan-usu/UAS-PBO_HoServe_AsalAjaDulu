package com.hoserve.repository;

import com.hoserve.entity.RekamMedis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository untuk entity RekamMedis.
 */
@Repository
public interface RekamMedisRepository extends JpaRepository<RekamMedis, Long> {

    List<RekamMedis> findByPasienIdOrderByTanggalPemeriksaanDesc(Long pasienId);

    List<RekamMedis> findByDokterIdOrderByTanggalPemeriksaanDesc(Long dokterId);

    Optional<RekamMedis> findByReservasiId(Long reservasiId);
}

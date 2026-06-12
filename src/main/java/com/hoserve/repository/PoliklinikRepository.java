package com.hoserve.repository;

import com.hoserve.entity.Poliklinik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository untuk entity Poliklinik.
 */
@Repository
public interface PoliklinikRepository extends JpaRepository<Poliklinik, Long> {

    Optional<Poliklinik> findByNamaPoliklinik(String namaPoliklinik);

    boolean existsByNamaPoliklinik(String namaPoliklinik);
}

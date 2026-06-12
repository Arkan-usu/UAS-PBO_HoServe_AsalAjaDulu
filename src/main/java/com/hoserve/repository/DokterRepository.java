package com.hoserve.repository;

import com.hoserve.entity.Dokter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository untuk entity Dokter.
 * Menerapkan ABSTRACTION - interface tanpa detail implementasi.
 */
@Repository
public interface DokterRepository extends JpaRepository<Dokter, Long> {

    Optional<Dokter> findByUsername(String username);

    Optional<Dokter> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<Dokter> findByPoliklinikId(Long poliklinikId);

    List<Dokter> findBySpesialisasi(String spesialisasi);

    List<Dokter> findByNamaContainingIgnoreCase(String nama);
}

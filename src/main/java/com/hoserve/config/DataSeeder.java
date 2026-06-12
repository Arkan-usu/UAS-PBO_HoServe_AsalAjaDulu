package com.hoserve.config;

import com.hoserve.entity.Admin;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.JadwalDokter;
import com.hoserve.entity.Poliklinik;
import com.hoserve.repository.AdminRepository;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PoliklinikRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Data Seeder - Menginisialisasi data awal saat aplikasi pertama kali jalan.
 * Membuat admin default, poliklinik contoh, dan dokter contoh.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final AdminRepository adminRepository;
    private final DokterRepository dokterRepository;
    private final PoliklinikRepository poliklinikRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AdminRepository adminRepository,
                      DokterRepository dokterRepository,
                      PoliklinikRepository poliklinikRepository,
                      PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.dokterRepository = dokterRepository;
        this.poliklinikRepository = poliklinikRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedPoliklinik();
        seedDokter();
        logger.info("=== DATA SEEDER SELESAI ===");
        logger.info("Admin default  -> username: admin, password: admin123");
        logger.info("Dokter contoh  -> username: dr.sarah, password: dokter123");
        logger.info("Dokter contoh  -> username: dr.budi, password: dokter123");
    }

    private void seedAdmin() {
        if (adminRepository.count() == 0) {
            Admin admin = new Admin(
                    "Administrator",
                    "admin@hoserve.com",
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "081234567890"
            );
            adminRepository.save(admin);
            logger.info("✅ Admin default berhasil dibuat");
        }
    }

    private void seedPoliklinik() {
        if (poliklinikRepository.count() == 0) {
            Poliklinik poli1 = new Poliklinik("Poli Umum", "Pelayanan kesehatan umum", "Lantai 1, Ruang A");
            Poliklinik poli2 = new Poliklinik("Poli Gigi", "Pelayanan kesehatan gigi dan mulut", "Lantai 1, Ruang B");
            Poliklinik poli3 = new Poliklinik("Poli Anak", "Pelayanan kesehatan anak", "Lantai 2, Ruang A");

            poliklinikRepository.save(poli1);
            poliklinikRepository.save(poli2);
            poliklinikRepository.save(poli3);
            logger.info("✅ 3 Poliklinik contoh berhasil dibuat");
        }
    }

    private void seedDokter() {
        if (dokterRepository.count() == 0) {
            Poliklinik poliUmum = poliklinikRepository.findByNamaPoliklinik("Poli Umum").orElse(null);
            Poliklinik poliGigi = poliklinikRepository.findByNamaPoliklinik("Poli Gigi").orElse(null);

            // Dokter 1
            Dokter dokter1 = new Dokter(
                    "Dr. Sarah Wijaya",
                    "sarah@hoserve.com",
                    "dr.sarah",
                    passwordEncoder.encode("dokter123"),
                    "Umum",
                    "081111222333"
            );
            dokter1.setPoliklinik(poliUmum);

            // Jadwal Dokter 1
            JadwalDokter jadwal1 = new JadwalDokter(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));
            JadwalDokter jadwal2 = new JadwalDokter(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(12, 0));
            JadwalDokter jadwal3 = new JadwalDokter(DayOfWeek.FRIDAY, LocalTime.of(13, 0), LocalTime.of(17, 0));
            dokter1.addJadwal(jadwal1);
            dokter1.addJadwal(jadwal2);
            dokter1.addJadwal(jadwal3);

            dokterRepository.save(dokter1);

            // Dokter 2
            Dokter dokter2 = new Dokter(
                    "Dr. Budi Santoso",
                    "budi@hoserve.com",
                    "dr.budi",
                    passwordEncoder.encode("dokter123"),
                    "Gigi",
                    "081444555666"
            );
            dokter2.setPoliklinik(poliGigi);

            // Jadwal Dokter 2
            JadwalDokter jadwal4 = new JadwalDokter(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(15, 0));
            JadwalDokter jadwal5 = new JadwalDokter(DayOfWeek.THURSDAY, LocalTime.of(9, 0), LocalTime.of(15, 0));
            dokter2.addJadwal(jadwal4);
            dokter2.addJadwal(jadwal5);

            dokterRepository.save(dokter2);

            logger.info("✅ 2 Dokter contoh berhasil dibuat dengan jadwal");
        }
    }
}

package com.hoserve.security;

import com.hoserve.entity.Admin;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.Pasien;
import com.hoserve.entity.User;
import com.hoserve.repository.AdminRepository;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PasienRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Custom UserDetailsService yang mencari user dari 3 tabel (Admin, Dokter, Pasien).
 * Menerapkan ABSTRACTION - implementasi dari interface UserDetailsService.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final DokterRepository dokterRepository;
    private final PasienRepository pasienRepository;

    public CustomUserDetailsService(AdminRepository adminRepository,
                                    DokterRepository dokterRepository,
                                    PasienRepository pasienRepository) {
        this.adminRepository = adminRepository;
        this.dokterRepository = dokterRepository;
        this.pasienRepository = pasienRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cari di tabel Admin
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) {
            return buildUserDetails(admin.get());
        }

        // Cari di tabel Dokter
        Optional<Dokter> dokter = dokterRepository.findByUsername(username);
        if (dokter.isPresent()) {
            return buildUserDetails(dokter.get());
        }

        // Cari di tabel Pasien
        Optional<Pasien> pasien = pasienRepository.findByUsername(username);
        if (pasien.isPresent()) {
            return buildUserDetails(pasien.get());
        }

        throw new UsernameNotFoundException("User tidak ditemukan dengan username: " + username);
    }

    /**
     * Konversi entity User menjadi Spring Security UserDetails.
     */
    private UserDetails buildUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    /**
     * Helper: Cari user entity berdasarkan username (mengembalikan entity asli).
     */
    public User findUserEntityByUsername(String username) {
        Optional<Admin> admin = adminRepository.findByUsername(username);
        if (admin.isPresent()) return admin.get();

        Optional<Dokter> dokter = dokterRepository.findByUsername(username);
        if (dokter.isPresent()) return dokter.get();

        Optional<Pasien> pasien = pasienRepository.findByUsername(username);
        if (pasien.isPresent()) return pasien.get();

        throw new UsernameNotFoundException("User tidak ditemukan dengan username: " + username);
    }
}

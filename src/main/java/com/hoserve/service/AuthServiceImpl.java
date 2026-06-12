package com.hoserve.service;

import com.hoserve.dto.request.LoginRequest;
import com.hoserve.dto.request.RegisterRequest;
import com.hoserve.dto.response.LoginResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.entity.Admin;
import com.hoserve.entity.Dokter;
import com.hoserve.entity.Pasien;
import com.hoserve.entity.User;
import com.hoserve.exception.BadRequestException;
import com.hoserve.repository.AdminRepository;
import com.hoserve.repository.DokterRepository;
import com.hoserve.repository.PasienRepository;
import com.hoserve.security.JwtTokenProvider;
import com.hoserve.util.DtoMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementasi AuthService.
 * Menerapkan ABSTRACTION (implements interface) dan logika autentikasi.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final DokterRepository dokterRepository;
    private final PasienRepository pasienRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           PasswordEncoder passwordEncoder,
                           AdminRepository adminRepository,
                           DokterRepository dokterRepository,
                           PasienRepository pasienRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.adminRepository = adminRepository;
        this.dokterRepository = dokterRepository;
        this.pasienRepository = pasienRepository;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // Autentikasi via Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Cari user entity untuk mendapatkan data lengkap
        User user = findUserByUsername(request.getUsername());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponse(token, user.getRole().name(), user.getNama(), user.getId());
    }

    @Override
    public PasienResponse register(RegisterRequest request) {
        // Validasi username unik di semua tabel
        if (isUsernameExists(request.getUsername())) {
            throw new BadRequestException("Username sudah digunakan: " + request.getUsername());
        }

        // Validasi email unik
        if (request.getEmail() != null && isEmailExists(request.getEmail())) {
            throw new BadRequestException("Email sudah digunakan: " + request.getEmail());
        }

        // Buat entity Pasien baru
        Pasien pasien = new Pasien();
        pasien.setNama(request.getNama());
        pasien.setEmail(request.getEmail());
        pasien.setUsername(request.getUsername());
        pasien.setPassword(passwordEncoder.encode(request.getPassword()));
        pasien.setTanggalLahir(request.getTanggalLahir());
        pasien.setAlamat(request.getAlamat());
        pasien.setNomorTelepon(request.getNomorTelepon());

        Pasien savedPasien = pasienRepository.save(pasien);
        return DtoMapper.toPasienResponse(savedPasien);
    }

    // === Helper methods ===

    private User findUserByUsername(String username) {
        // Cari di Admin
        if (adminRepository.findByUsername(username).isPresent()) {
            return adminRepository.findByUsername(username).get();
        }
        // Cari di Dokter
        if (dokterRepository.findByUsername(username).isPresent()) {
            return dokterRepository.findByUsername(username).get();
        }
        // Cari di Pasien
        if (pasienRepository.findByUsername(username).isPresent()) {
            return pasienRepository.findByUsername(username).get();
        }
        throw new BadRequestException("User tidak ditemukan");
    }

    private boolean isUsernameExists(String username) {
        return adminRepository.existsByUsername(username)
                || dokterRepository.existsByUsername(username)
                || pasienRepository.existsByUsername(username);
    }

    private boolean isEmailExists(String email) {
        return adminRepository.existsByEmail(email)
                || dokterRepository.existsByEmail(email)
                || pasienRepository.existsByEmail(email);
    }
}

package com.hoserve.service;

import com.hoserve.dto.request.LoginRequest;
import com.hoserve.dto.request.RegisterRequest;
import com.hoserve.dto.response.LoginResponse;
import com.hoserve.dto.response.PasienResponse;

/**
 * Interface Service untuk autentikasi.
 * Menerapkan ABSTRACTION - hanya mendefinisikan kontrak tanpa implementasi.
 */
public interface AuthService {

    /**
     * Login user dan generate JWT token.
     */
    LoginResponse login(LoginRequest request);

    /**
     * Registrasi pasien baru.
     */
    PasienResponse register(RegisterRequest request);
}

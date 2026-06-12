package com.hoserve.controller;

import com.hoserve.dto.request.LoginRequest;
import com.hoserve.dto.request.RegisterRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.LoginResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller untuk autentikasi (Login & Register).
 * Endpoint ini PUBLIC - tidak perlu JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/login
     * Login dan dapatkan JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse loginResponse = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.success("Login berhasil", loginResponse)
        );
    }

    /**
     * POST /api/auth/register
     * Registrasi pasien baru.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<PasienResponse>> register(@Valid @RequestBody RegisterRequest request) {
        PasienResponse pasienResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Registrasi berhasil", pasienResponse));
    }
}

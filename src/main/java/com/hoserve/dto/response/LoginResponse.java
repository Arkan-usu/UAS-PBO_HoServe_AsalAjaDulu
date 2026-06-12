package com.hoserve.dto.response;

/**
 * DTO response setelah login berhasil.
 * Berisi JWT token, informasi role, dan data user.
 */
public class LoginResponse {

    private String token;
    private String tokenType = "Bearer";
    private String role;
    private String nama;
    private Long userId;

    // === Constructors ===

    public LoginResponse() {
    }

    public LoginResponse(String token, String role, String nama, Long userId) {
        this.token = token;
        this.role = role;
        this.nama = nama;
        this.userId = userId;
    }

    // === Getter & Setter ===

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

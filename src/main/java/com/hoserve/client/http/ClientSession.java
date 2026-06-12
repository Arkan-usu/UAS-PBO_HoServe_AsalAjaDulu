package com.hoserve.client.http;

import com.hoserve.enums.Role;

/**
 * Singleton class to store active user session information and JWT token.
 */
public class ClientSession {

    private static ClientSession instance;

    private String token;
    private Long userId;
    private String username;
    private String nama;
    private Role role;

    private ClientSession() {
    }

    public static synchronized ClientSession getInstance() {
        if (instance == null) {
            instance = new ClientSession();
        }
        return instance;
    }

    public void clear() {
        this.token = null;
        this.userId = null;
        this.username = null;
        this.nama = null;
        this.role = null;
    }

    // === Encapsulation: Getter and Setter ===

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

package com.hoserve.entity;

import com.hoserve.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Kelas abstrak User - Parent class untuk Admin, Dokter, dan Pasien.
 * Menerapkan INHERITANCE (@MappedSuperclass) dan ENCAPSULATION (private fields + getter/setter).
 */
@MappedSuperclass
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Column(nullable = false)
    private String nama;

    @Email(message = "Format email tidak valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Username tidak boleh kosong")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // === Constructors ===

    protected User() {
        // Default constructor untuk JPA
    }

    protected User(String nama, String email, String username, String password, Role role) {
        this.nama = nama;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // === ENCAPSULATION: Private fields dengan Getter & Setter ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // === POLYMORPHISM: Method yang akan di-override oleh child class ===

    /**
     * Mengembalikan informasi tampilan sesuai tipe user.
     * Child class akan meng-override method ini (Polymorphism).
     */
    public abstract String getDisplayInfo();
}

package com.hoserve.entity;

import com.hoserve.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Entity Admin - Child class dari User.
 * Menerapkan INHERITANCE (extends User) dan POLYMORPHISM (override getDisplayInfo).
 */
@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(name = "nomor_telepon")
    private String nomorTelepon;

    // === Constructors ===

    public Admin() {
        super();
        setRole(Role.ADMIN);
    }

    public Admin(String nama, String email, String username, String password, String nomorTelepon) {
        super(nama, email, username, password, Role.ADMIN);
        this.nomorTelepon = nomorTelepon;
    }

    // === Getter & Setter (ENCAPSULATION) ===

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    // === POLYMORPHISM: Override method dari parent class ===

    @Override
    public String getDisplayInfo() {
        return String.format("Admin: %s | Email: %s | Telp: %s", getNama(), getEmail(), nomorTelepon);
    }
}

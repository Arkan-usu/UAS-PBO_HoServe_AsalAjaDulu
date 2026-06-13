# HoServe - Clinic Management System

## Deskripsi Singkat
**HoServe** adalah sebuah aplikasi sistem manajemen klinik (Clinic Management System) yang dikembangkan untuk memenuhi Tugas Akhir / Ujian Akhir Semester mata kuliah Pemrograman Berbasis Objek (PBO). Aplikasi ini dibangun menggunakan Java Spring Boot untuk sisi *backend* dan JavaFX untuk antarmuka *client* (desktop), sehingga menghasilkan pengalaman pengguna yang responsif, aman, dan efisien.

## Fitur-Fitur Utama
Aplikasi HoServe dilengkapi dengan sistem autentikasi dan manajemen akses (Role-Based Access Control) berbasis JWT. Terdapat beberapa fitur utama berdasarkan *role* pengguna:
- **Autentikasi & Keamanan:** Fitur Login dan Register yang aman menggunakan Spring Security dan JWT.
- **Admin Dashboard:** Antarmuka khusus untuk administrator dalam mengelola operasional klinik, data dokter, data pasien, serta sistem secara keseluruhan.
- **Dokter Dashboard:** Antarmuka bagi dokter untuk melihat jadwal praktik, mengelola antrean pasien, dan memberikan catatan rekam medis.
- **Pasien Dashboard:** Antarmuka bagi pasien untuk melakukan pendaftaran, memesan jadwal konsultasi (appointment), dan melihat riwayat pemeriksaan.

## Dependencies Utama
Aplikasi ini menggunakan teknologi dan pustaka berikut:
- **Java 17**
- **Spring Boot 3.2.5** (Web, Data JPA, Security, Validation)
- **H2 Database** (In-Memory Database)
- **JSON Web Token (JWT)** (io.jsonwebtoken)
- **JavaFX 21.0.2** (Controls & FXML)

## Cara Menjalankan Aplikasi

### Persyaratan Sistem (Prerequisites)
Pastikan sistem Anda telah terinstal:
- [Java Development Kit (JDK) 17](https://adoptium.net/)
- [Apache Maven](https://maven.apache.org/)

### Langkah Instalasi
1. **Clone repositori** ini ke dalam komputer lokal Anda:
   ```bash
   git clone <url-repositori-anda>
   cd UAS-PBO_HoServe_AsalAjaDulu
   ```

2. **Unduh dependencies** menggunakan Maven:
   ```bash
   mvn clean install
   ```

3. **Jalankan Aplikasi:**
   Karena aplikasi ini mengintegrasikan Spring Boot dengan JavaFX, Anda dapat menjalankan perintah berikut melalui terminal:
   ```bash
   mvn spring-boot:run
   ```
   *Atau, Anda dapat menjalankan *class* utama `com.hoserve.client.ClientApplication` secara langsung melalui IDE (seperti IntelliJ IDEA atau Eclipse).*

---
**Dikembangkan oleh Kelompok Asal Aja Dulu:**
1. Johannes Indra Christian Pandiangan (241401001)
2. Risky Jonalson Silaen (241401010)
3. Arkaan Ramdhan Rusdi (241401082)
4. Muhammad Diaz William Bevan (241401088)

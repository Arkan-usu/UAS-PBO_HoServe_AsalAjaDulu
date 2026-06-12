package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.HttpClientUtil;
import com.hoserve.client.util.AlertUtil; // 1. IMPORT UTILS BUATANMU
import com.hoserve.dto.request.RegisterRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.PasienResponse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.time.LocalDate;

/**
 * Controller for the Patient Registration UI view.
 */
public class RegisterController {

    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelepon;
    @FXML private DatePicker dpLahir;
    @FXML private TextField txtAlamat;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;
    @FXML private Button btnRegister;

    @FXML
    public void initialize() {
        lblMessage.setText("");
    }

    @FXML
    void handleRegister(ActionEvent event) {
        String nama = txtNama.getText().trim();
        String email = txtEmail.getText().trim();
        String telepon = txtTelepon.getText().trim();
        LocalDate lahir = dpLahir.getValue();
        String alamat = txtAlamat.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        // 2. VALIDASI FIELD KOSONG DENGAN ALERT POP-UP
        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || lahir == null || 
            alamat.isEmpty() || username.isEmpty() || password.isEmpty()) {
            
            lblMessage.setTextFill(Color.web("#ff416c"));
            lblMessage.setText("Semua field wajib diisi!");
            
            // Tampilkan dialog pop-up error
            AlertUtil.showError("Validasi Gagal", "Data Tidak Lengkap", "Silakan isi semua data formulir pendaftaran!");
            return;
        }

        // 3. VALIDASI PANJANG PASSWORD DENGAN ALERT POP-UP
        if (password.length() < 6) {
            lblMessage.setTextFill(Color.web("#ff416c"));
            lblMessage.setText("Password minimal 6 karakter!");
            
            AlertUtil.showError("Validasi Gagal", "Password Terlalu Pendek", "Demi keamanan, password minimal harus 6 karakter.");
            return;
        }

        lblMessage.setTextFill(Color.WHITE);
        lblMessage.setText("Mendaftarkan akun...");
        btnRegister.setDisable(true);

        new Thread(() -> {
            try {
                RegisterRequest request = new RegisterRequest();
                request.setNama(nama);
                request.setEmail(email);
                request.setNomorTelepon(telepon);
                request.setTanggalLahir(lahir);
                request.setAlamat(alamat);
                request.setUsername(username);
                request.setPassword(password);

                ApiResponse<PasienResponse> response = HttpClientUtil.post("/api/auth/register", request, PasienResponse.class);

                Platform.runLater(() -> {
                    btnRegister.setDisable(false);
                    if (response != null && response.isSuccess()) {
                        lblMessage.setTextFill(Color.web("#38ef7d"));
                        lblMessage.setText("Registrasi berhasil! Mengalihkan ke login...");
                        
                        // 4. POP-UP INFORMASI SUKSES REGISTRASI
                        AlertUtil.showInfo("Registrasi Sukses", "Akun Berhasil Dibuat", "Halo " + nama + ", akun pasien Anda telah terdaftar. Silakan tekan OK untuk masuk ke halaman login.");
                        
                        // Menunggu 2 detik, lalu pindah scene
                        new Thread(() -> {
                            try {
                                Thread.sleep(2000);
                                Platform.runLater(() -> {
                                    ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        
                    } else {
                        lblMessage.setTextFill(Color.web("#ff416c"));
                        String errMsg = (response != null) ? response.getMessage() : "Registrasi gagal. Coba username/email lain.";
                        lblMessage.setText(errMsg);
                        
                        // 5. POP-UP GAGAL DARI RESPONS BACKEND SERVER
                        AlertUtil.showError("Registrasi Gagal", "Gagal Menyimpan Data", errMsg);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnRegister.setDisable(false);
                    lblMessage.setTextFill(Color.web("#ff416c"));
                    lblMessage.setText("Error: " + e.getMessage());
                    
                    // 6. POP-UP JIKA TERJADI TIMEOUT ATAU KONEKSI SERVER MATI
                    AlertUtil.showError("Sistem Error", "Gagal Menghubungi Server", "Terjadi kesalahan jaringan: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleGoToLogin(ActionEvent event) {
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
    }

    @FXML
    void handleBackToHome(ActionEvent event) {
        ClientApplication.changeScene("/client/fxml/home.fxml", "HoServe - Clinic Management System", 1024, 768);
    }
}
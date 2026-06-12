package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.HttpClientUtil;
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

    @FXML
    private TextField txtNama;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelepon;

    @FXML
    private DatePicker dpLahir;

    @FXML
    private TextField txtAlamat;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnRegister;

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

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || lahir == null || 
            alamat.isEmpty() || username.isEmpty() || password.isEmpty()) {
            lblMessage.setTextFill(Color.web("#ff416c"));
            lblMessage.setText("Semua field wajib diisi!");
            return;
        }

        if (password.length() < 6) {
            lblMessage.setTextFill(Color.web("#ff416c"));
            lblMessage.setText("Password minimal 6 karakter!");
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
                        
                        // Wait 2 seconds, then transition to login
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
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnRegister.setDisable(false);
                    lblMessage.setTextFill(Color.web("#ff416c"));
                    lblMessage.setText("Error: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleGoToLogin(ActionEvent event) {
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
    }
}

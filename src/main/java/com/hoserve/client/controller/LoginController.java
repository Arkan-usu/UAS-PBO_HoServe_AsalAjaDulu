package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.ClientSession;
import com.hoserve.client.http.HttpClientUtil;
import com.hoserve.dto.request.LoginRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.LoginResponse;
import com.hoserve.enums.Role;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the Login UI view.
 */
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    @FXML
    private Button btnLogin;

    @FXML
    public void initialize() {
        lblError.setText("");
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Username dan password tidak boleh kosong!");
            return;
        }

        lblError.setText("Menghubungkan ke server...");
        btnLogin.setDisable(true);

        // Run the network request in a background thread to prevent UI freezing
        new Thread(() -> {
            try {
                LoginRequest request = new LoginRequest();
                request.setUsername(username);
                request.setPassword(password);

                ApiResponse<LoginResponse> response = HttpClientUtil.post("/api/auth/login", request, LoginResponse.class);

                Platform.runLater(() -> {
                    btnLogin.setDisable(false);
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        LoginResponse loginData = response.getData();

                        // Populate Session singleton
                        ClientSession session = ClientSession.getInstance();
                        session.setToken(loginData.getToken());
                        session.setUserId(loginData.getUserId());
                        session.setUsername(username);
                        session.setNama(loginData.getNama());
                        session.setRole(Role.valueOf(loginData.getRole()));

                        // Redirect to the role-specific dashboard
                        navigateToDashboard(Role.valueOf(loginData.getRole()));
                    } else {
                        String errMsg = (response != null) ? response.getMessage() : "Gagal tersambung ke backend server.";
                        lblError.setText(errMsg);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnLogin.setDisable(false);
                    lblError.setText("Koneksi gagal: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleGoToRegister(ActionEvent event) {
        ClientApplication.changeScene("/client/fxml/register.fxml", "HoServe - Registrasi Pasien Baru", 850, 600);
    }

    private void navigateToDashboard(Role role) {
        if (role == Role.ADMIN) {
            ClientApplication.changeScene("/client/fxml/admin_dashboard.fxml", "HoServe - Panel Administrasi", 1024, 768);
        } else if (role == Role.PASIEN) {
            ClientApplication.changeScene("/client/fxml/pasien_dashboard.fxml", "HoServe - Portal Pasien", 1024, 768);
        } else if (role == Role.DOKTER) {
            ClientApplication.changeScene("/client/fxml/dokter_dashboard.fxml", "HoServe - Portal Dokter", 1024, 768);
        }
    }
}

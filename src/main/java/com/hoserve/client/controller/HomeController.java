package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the Home Portal.
 */
public class HomeController {

    @FXML
    public void initialize() {
        // Initialization if needed
    }

    @FXML
    void handleRedirectToLogin(ActionEvent event) {
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Login", 850, 600);
    }
}

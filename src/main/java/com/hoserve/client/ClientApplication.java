package com.hoserve.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the HoServe JavaFX Desktop Client.
 */
public class ClientApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        // Start on the home screen
        changeScene("/client/fxml/home.fxml", "HoServe - Clinic Management System", 1024, 768);
    }

    /**
     * Helper method to switch between different FXML screens.
     */
    public static void changeScene(String fxmlPath, String title, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource(fxmlPath));
            Parent root = loader.load();
            
            Scene currentScene = primaryStage.getScene();
            
            if (currentScene != null) {
                // Jika aplikasi sudah terbuka, cukup ganti isinya (root) saja.
                // Ini akan mempertahankan ukuran layar dan posisi window saat ini.
                currentScene.setRoot(root);
            } else {
                // Jika ini adalah pertama kalinya aplikasi dibuka (saat start)
                Scene scene = new Scene(root, width, height);
                
                // Load global CSS stylesheet
                var cssResource = ClientApplication.class.getResource("/client/css/style.css");
                if (cssResource != null) {
                    scene.getStylesheets().add(cssResource.toExternalForm());
                } else {
                    System.err.println("Warning: Global CSS stylesheet not found!");
                }
                
                primaryStage.setScene(scene);
                primaryStage.centerOnScreen();
            }
            
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error changing scene to: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.hoserve.client.util;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertUtil {

    public static void showInfo(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content, "#38ef7d"); // Hijau Neon Elegan
    }

    public static void showError(String title, String header, String content) {
        showAlert(AlertType.ERROR, title, header, content, "#ff416c"); // Merah Neon Cerah
    }

    private static void showAlert(AlertType type, String title, String header, String content, String accentColor) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        
        // Hilangkan teks bawaan agar tidak double formatting
        alert.setHeaderText(null);
        alert.setContentText(null);

        // 1. BUANG DEKORASI WINDOWS (Menghilangkan border & title bar OS)
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED); 

        // 2. TRIK AMPUH: Sembunyikan background scene utama agar sudut putih bocor hilang total!
        alert.getDialogPane().getScene().setFill(Color.TRANSPARENT);

        DialogPane dialogPane = alert.getDialogPane();
        
        // 3. STYLING CONTAINER UTAMA (DialogPane)
        dialogPane.setStyle(
            "-fx-background-color: #151c33;" +          // Warna dasar midnight blue (lebih gelap & deep)
            "-fx-border-color: " + accentColor + ";" +   // Garis tepi tipis bersinar sesuai jenis alert
            "-fx-border-width: 1.5px;" +
            "-fx-border-radius: 14px;" +                 // Lengkungan sudut yang lebih halus
            "-fx-background-radius: 14px;" +
            "-fx-padding: 0px;"                          // Reset padding bawaan agar tata letak kustom kita bekerja sempurna
        );

        // Menghilangkan grafik/icon jadul bawaan JavaFX secara total
        Region graphicContainer = (Region) dialogPane.lookup(".graphic-container");
        if (graphicContainer != null) {
            graphicContainer.setVisible(false);
            graphicContainer.setPrefSize(0, 0);
        }

        // 4. DESIGN KUSTOM PANEL ISI (Membangun Layout yang Simetris)
        VBox customContent = new VBox(12);               // Jarak antar elemen teks 12px
        customContent.setAlignment(Pos.CENTER_LEFT);    // Rata kiri agar elegan
        customContent.setPadding(new Insets(25, 25, 20, 25)); // Margin dalam yang luas dan lega

        // Label Judul (Menggantikan header panel yang buram tadi)
        Label lblHeader = new Label(header);
        lblHeader.setStyle(
            "-fx-text-fill: white;" +                    // Font putih bersih agar kontras tinggi
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'Segoe UI', Helvetica;"
        );

        // Label Pesan Deskripsi
        Label lblContent = new Label(content);
        lblContent.setWrapText(true);                    // Bungkus teks otomatis jika terlalu panjang
        lblContent.setMaxWidth(340);                     // Batasi lebar text agar rapi
        lblContent.setStyle(
            "-fx-text-fill: #a0aec0;" +                  // Warna abu-abu terang modern (Slate Gray)
            "-fx-font-size: 13px;" +
            "-fx-font-family: 'Segoe UI', Helvetica;" +
            "-fx-line-spacing: 4px;"
        );

        customContent.getChildren().addAll(lblHeader, lblContent);
        dialogPane.setContent(customContent);

        // 5. STYLING TOMBOL "MENGERTI" AKURAT
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (okButton != null) {
            okButton.setText("Mengerti");
            
            // Masukkan tombol ke dalam wrapper container untuk memberikan margin yang pas di bawah kanan
            okButton.setStyle(
                "-fx-background-color: " + accentColor + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 12px;" +
                "-fx-font-family: 'Segoe UI';" +
                "-fx-background-radius: 8px;" +
                "-fx-padding: 8px 24px;" +              // Ukuran tombol dibuat lebih gemuk dan premium
                "-fx-cursor: hand;"
            );
            
            // Memberikan efek padding khusus pada baris penempatan tombol bawaan JavaFX
            if (dialogPane.lookup(".button-bar") != null) {
                dialogPane.lookup(".button-bar").setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-padding: 0px 25px 20px 0px;"    // Sejajar dengan padding teks di atasnya
                );
            }
        }

        alert.showAndWait();
    }
}
package com.hoserve.client;

public class MainApp {
    public static void main(String[] args) {
        // Trik memanggil launch JavaFX secara tidak langsung agar tidak dicegah oleh JRE standar
        ClientApplication.main(args);
    }
}
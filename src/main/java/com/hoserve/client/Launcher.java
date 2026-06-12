package com.hoserve.client;

public class Launcher {
    public static void main(String[] args) {
        // Ini akan memanggil ClientApplication secara tidak langsung
        // sehingga VS Code tidak akan memunculkan error komponen hilang
        ClientApplication.main(args);
    }
}
package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.ClientSession;
import com.hoserve.client.http.HttpClientUtil;
import com.hoserve.dto.request.ReservasiRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.dto.response.ReservasiResponse;
import com.hoserve.enums.StatusReservasi;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the Patient Dashboard Portal.
 */
public class PasienController {

    @FXML private Button btnNavBooking;
    @FXML private Button btnNavHistory;
    @FXML private Label lblMenuTitle;
    @FXML private Label lblWelcomeMessage;

    // === Panels ===
    @FXML private HBox panelBooking;
    @FXML private VBox panelHistory;

    // === Form Controls ===
    @FXML private ComboBox<PoliklinikResponse> cbPoliklinik;
    @FXML private ComboBox<DokterResponse> cbDokter;
    @FXML private DatePicker dpTanggal;
    @FXML private TextArea txtKeluhan;
    @FXML private Label lblBookingError;
    @FXML private Button btnBook;

    // === Ticket Receipt ===
    @FXML private VBox vboxReceipt;
    @FXML private Label lblReceiptDokter;
    @FXML private Label lblReceiptPoliklinik;
    @FXML private Label lblReceiptAntrean;
    @FXML private Label lblReceiptTanggal;
    @FXML private Label lblReceiptStatus;
    @FXML private Label lblReceiptKeluhan;

    // === History Table ===
    @FXML private TableView<ReservasiResponse> tableHistory;
    @FXML private TableColumn<ReservasiResponse, Long> colHistoryId;
    @FXML private TableColumn<ReservasiResponse, LocalDate> colHistoryTanggal;
    @FXML private TableColumn<ReservasiResponse, String> colHistoryDokter;
    @FXML private TableColumn<ReservasiResponse, String> colHistoryPoliklinik;
    @FXML private TableColumn<ReservasiResponse, String> colHistoryKeluhan;
    @FXML private TableColumn<ReservasiResponse, Integer> colHistoryAntrean;
    @FXML private TableColumn<ReservasiResponse, String> colHistoryStatus;

    private final ObservableList<ReservasiResponse> historyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        lblWelcomeMessage.setText("Selamat datang, " + ClientSession.getInstance().getNama());
        lblBookingError.setText("");

        // 1. Setup Combo Box String Converters
        cbPoliklinik.setConverter(new StringConverter<>() {
            @Override
            public String toString(PoliklinikResponse object) {
                return object != null ? object.getNamaPoliklinik() + " (" + object.getLokasi() + ")" : "Pilih Poliklinik";
            }
            @Override
            public PoliklinikResponse fromString(String string) { return null; }
        });

        cbDokter.setConverter(new StringConverter<>() {
            @Override
            public String toString(DokterResponse object) {
                return object != null ? object.getNama() + " (" + object.getSpesialisasi() + ")" : "Pilih Dokter";
            }
            @Override
            public DokterResponse fromString(String string) { return null; }
        });

        // 2. Setup History Table Columns
        colHistoryId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHistoryTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalReservasi"));
        colHistoryDokter.setCellValueFactory(new PropertyValueFactory<>("namaDokter"));
        colHistoryPoliklinik.setCellValueFactory(new PropertyValueFactory<>("namaPoliklinik"));
        colHistoryKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        colHistoryAntrean.setCellValueFactory(new PropertyValueFactory<>("nomorAntrean"));
        colHistoryStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for colorful Status badges
        colHistoryStatus.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label label = new Label(item);
                    label.getStyleClass().add("badge");
                    switch (item) {
                        case "MENUNGGU":
                            label.getStyleClass().add("badge-waiting");
                            break;
                        case "DITERIMA":
                            label.getStyleClass().add("badge-accepted");
                            break;
                        case "SELESAI":
                            label.getStyleClass().add("badge-completed");
                            break;
                        case "DIBATALKAN":
                            label.getStyleClass().add("badge-cancelled");
                            break;
                    }
                    setGraphic(label);
                    setText(null);
                }
            }
        });

        tableHistory.setItems(historyList);

        // 3. Load initial data
        loadPoliklinikData();
        loadHistoryData();
    }

    // === Navigation Handlers ===

    @FXML
    void showPanelBooking(ActionEvent event) {
        setMenuActive(btnNavBooking, "Buat Reservasi Baru");
        panelBooking.setVisible(true);
        panelHistory.setVisible(false);
    }

    @FXML
    void showPanelHistory(ActionEvent event) {
        setMenuActive(btnNavHistory, "Riwayat Kunjungan");
        panelBooking.setVisible(false);
        panelHistory.setVisible(true);
        loadHistoryData(); // Refresh history
    }

    private void setMenuActive(Button activeBtn, String title) {
        btnNavBooking.getStyleClass().remove("sidebar-btn-active");
        btnNavHistory.getStyleClass().remove("sidebar-btn-active");
        activeBtn.getStyleClass().add("sidebar-btn-active");
        lblMenuTitle.setText(title);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        ClientSession.getInstance().clear();
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
    }

    // === Booking Panel Handlers ===

    @FXML
    void handlePoliklinikSelect(ActionEvent event) {
        PoliklinikResponse selectedPoli = cbPoliklinik.getSelectionModel().getSelectedItem();
        cbDokter.getSelectionModel().clearSelection();
        cbDokter.getItems().clear();

        if (selectedPoli != null) {
            lblBookingError.setText("Memuat daftar dokter...");
            new Thread(() -> {
                try {
                    ApiResponse<List<DokterResponse>> response = HttpClientUtil.getList(
                            "/api/pasien/dokter/poliklinik/" + selectedPoli.getId(), 
                            DokterResponse.class
                    );
                    Platform.runLater(() -> {
                        lblBookingError.setText("");
                        if (response != null && response.isSuccess() && response.getData() != null) {
                            cbDokter.setItems(FXCollections.observableArrayList(response.getData()));
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> lblBookingError.setText("Gagal memuat dokter: " + e.getMessage()));
                    e.printStackTrace();
                }
            }).start();
        }
    }

    @FXML
    void handleDokterSelect(ActionEvent event) {}

    @FXML
    void handleBuatReservasi(ActionEvent event) {
        PoliklinikResponse poli = cbPoliklinik.getSelectionModel().getSelectedItem();
        DokterResponse dokter = cbDokter.getSelectionModel().getSelectedItem();
        LocalDate tanggal = dpTanggal.getValue();
        String keluhan = txtKeluhan.getText().trim();

        if (poli == null || dokter == null || tanggal == null || keluhan.isEmpty()) {
            lblBookingError.setText("Semua field form wajib diisi!");
            return;
        }

        if (tanggal.isBefore(LocalDate.now())) {
            lblBookingError.setText("Tanggal reservasi tidak boleh di masa lalu!");
            return;
        }

        ReservasiRequest request = new ReservasiRequest();
        request.setPoliklinikId(poli.getId());
        request.setDokterId(dokter.getId());
        request.setTanggalReservasi(tanggal);
        request.setKeluhan(keluhan);

        lblBookingError.setText("Mengirim data...");
        btnBook.setDisable(true);

        new Thread(() -> {
            try {
                ApiResponse<ReservasiResponse> response = HttpClientUtil.post("/api/pasien/reservasi", request, ReservasiResponse.class);

                Platform.runLater(() -> {
                    btnBook.setDisable(false);
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        lblBookingError.setText("");
                        ReservasiResponse res = response.getData();
                        
                        // Show Receipt Ticket
                        lblReceiptDokter.setText(res.getNamaDokter());
                        lblReceiptPoliklinik.setText(res.getNamaPoliklinik());
                        lblReceiptAntrean.setText(String.format("%02d", res.getNomorAntrean()));
                        lblReceiptTanggal.setText(res.getTanggalReservasi().toString());
                        lblReceiptStatus.setText(res.getStatus());
                        lblReceiptKeluhan.setText(res.getKeluhan());
                        vboxReceipt.setVisible(true);

                        // Reset Inputs
                        cbPoliklinik.getSelectionModel().clearSelection();
                        cbDokter.getSelectionModel().clearSelection();
                        dpTanggal.setValue(null);
                        txtKeluhan.setText("");

                        showAlert(Alert.AlertType.INFORMATION, "Pemesanan Berhasil", 
                                "Reservasi Anda berhasil dibuat! Nomor antrean: " + res.getNomorAntrean());
                    } else {
                        lblBookingError.setText(response != null ? response.getMessage() : "Gagal membuat reservasi.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnBook.setDisable(false);
                    lblBookingError.setText("Error: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // === Data Fetching ===

    private void loadPoliklinikData() {
        new Thread(() -> {
            try {
                ApiResponse<List<PoliklinikResponse>> response = HttpClientUtil.getList("/api/pasien/poliklinik", PoliklinikResponse.class);
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        cbPoliklinik.setItems(FXCollections.observableArrayList(response.getData()));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadHistoryData() {
        new Thread(() -> {
            try {
                ApiResponse<List<ReservasiResponse>> response = HttpClientUtil.getList("/api/pasien/reservasi/riwayat", ReservasiResponse.class);
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        historyList.clear();
                        historyList.addAll(response.getData());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

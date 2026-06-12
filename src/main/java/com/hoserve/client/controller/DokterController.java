package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.ClientSession;
import com.hoserve.client.http.HttpClientUtil;
import com.hoserve.dto.request.RekamMedisRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.dto.response.RekamMedisResponse;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Controller for the Doctor Dashboard Portal.
 */
public class DokterController {

    @FXML private Button btnNavAntrean;
    @FXML private Button btnNavHistory;
    @FXML private Label lblMenuTitle;
    @FXML private Label lblWelcomeMessage;

    // === Panels ===
    @FXML private HBox panelAntrean;
    @FXML private VBox panelHistory;

    // === Queue Controls ===
    @FXML private TableView<ReservasiResponse> tableAntrean;
    @FXML private TableColumn<ReservasiResponse, Integer> colAntreanNo;
    @FXML private TableColumn<ReservasiResponse, String> colAntreanNama;
    @FXML private TableColumn<ReservasiResponse, String> colAntreanKeluhan;
    @FXML private TableColumn<ReservasiResponse, String> colAntreanStatus;

    // === EMR Form ===
    @FXML private Label lblFormPasienNama;
    @FXML private Label lblFormKeluhan;
    @FXML private VBox vboxFormFields;
    @FXML private TextField txtDiagnosis;
    @FXML private TextField txtTindakan;
    @FXML private TextArea txtResep;
    @FXML private TextArea txtCatatan;
    @FXML private Label lblEmrError;
    @FXML private Button btnSaveEmr;

    private ReservasiResponse selectedReservasi;
    private final ObservableList<ReservasiResponse> antreanList = FXCollections.observableArrayList();

    // === History (EMR lookup) ===
    @FXML private ComboBox<PasienItem> cbSearchPasien;
    @FXML private TableView<RekamMedisResponse> tableMedis;
    @FXML private TableColumn<RekamMedisResponse, LocalDateTime> colMedisTanggal;
    @FXML private TableColumn<RekamMedisResponse, String> colMedisKeluhan;
    @FXML private TableColumn<RekamMedisResponse, String> colMedisDiagnosis;
    @FXML private TableColumn<RekamMedisResponse, String> colMedisTindakan;
    @FXML private TableColumn<RekamMedisResponse, String> colMedisResep;
    @FXML private TableColumn<RekamMedisResponse, String> colMedisCatatan;

    private final ObservableList<RekamMedisResponse> medisHistoryList = FXCollections.observableArrayList();

    // Helper class to map patients uniquely in the dropdown
    public static class PasienItem {
        private final Long id;
        private final String nama;

        public PasienItem(Long id, String nama) {
            this.id = id;
            this.nama = nama;
        }

        public Long getId() { return id; }
        public String getNama() { return nama; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PasienItem)) return false;
            PasienItem that = (PasienItem) o;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }

    @FXML
    public void initialize() {
        lblWelcomeMessage.setText("Selamat bertugas, dr. " + ClientSession.getInstance().getNama());
        lblEmrError.setText("");

        // 1. Setup Queue Table Columns
        colAntreanNo.setCellValueFactory(new PropertyValueFactory<>("nomorAntrean"));
        colAntreanNama.setCellValueFactory(new PropertyValueFactory<>("namaPasien"));
        colAntreanKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        colAntreanStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colAntreanStatus.setCellFactory(column -> new TableCell<>() {
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

        tableAntrean.setItems(antreanList);

        // 2. Setup EMR History Table Columns
        colMedisTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPemeriksaan"));
        colMedisTanggal.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        colMedisKeluhan.setCellValueFactory(new PropertyValueFactory<>("keluhan"));
        colMedisDiagnosis.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        colMedisTindakan.setCellValueFactory(new PropertyValueFactory<>("tindakan"));
        colMedisResep.setCellValueFactory(new PropertyValueFactory<>("resepObat"));
        colMedisCatatan.setCellValueFactory(new PropertyValueFactory<>("catatan"));

        tableMedis.setItems(medisHistoryList);

        // 3. ComboBox setup
        cbSearchPasien.setConverter(new StringConverter<>() {
            @Override
            public String toString(PasienItem object) {
                return object != null ? object.getNama() : "Pilih Pasien";
            }
            @Override
            public PasienItem fromString(String string) { return null; }
        });

        // 4. Selection listener for table queue
        tableAntrean.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectQueueItem(newVal));

        // 5. Fetch Queue
        loadQueueData();

        // 6. Auto-Refresh (Polling) setiap 10 detik
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            // Hanya refresh jika panel antrean sedang dibuka/dilihat oleh dokter
            if (panelAntrean.isVisible()) {
                loadQueueData();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // === Navigation Handlers ===

    @FXML
    void showPanelAntrean(ActionEvent event) {
        setMenuActive(btnNavAntrean, "Antrean Pasien Hari Ini");
        panelAntrean.setVisible(true);
        panelHistory.setVisible(false);
    }

    @FXML
    void showPanelHistory(ActionEvent event) {
        setMenuActive(btnNavHistory, "Riwayat Medis Pasien");
        panelAntrean.setVisible(false);
        panelHistory.setVisible(true);
        
        // Build patient dropdown from queue data
        populatePatientDropdown();
    }

    private void setMenuActive(Button activeBtn, String title) {
        btnNavAntrean.getStyleClass().remove("sidebar-btn-active");
        btnNavHistory.getStyleClass().remove("sidebar-btn-active");
        activeBtn.getStyleClass().add("sidebar-btn-active");
        lblMenuTitle.setText(title);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        ClientSession.getInstance().clear();
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
    }

    // === Queue Actions ===

    private void selectQueueItem(ReservasiResponse reservasi) {
        this.selectedReservasi = reservasi;
        lblEmrError.setText("");

        if (reservasi != null) {
            lblFormPasienNama.setText(reservasi.getNamaPasien());
            lblFormKeluhan.setText(reservasi.getKeluhan());

            // Only allow editing/submitting if the status is MENUNGGU or DITERIMA
            if ("MENUNGGU".equals(reservasi.getStatus()) || "DITERIMA".equals(reservasi.getStatus())) {
                vboxFormFields.setDisable(false);
                btnSaveEmr.setDisable(false);
                txtDiagnosis.setText("");
                txtTindakan.setText("");
                txtResep.setText("");
                txtCatatan.setText("");
            } else {
                vboxFormFields.setDisable(true);
                btnSaveEmr.setDisable(true);
                lblEmrError.setText("Catatan rekam medis telah diisi / kunjungan selesai.");
                
                // Try to load historical data to fill the form for view
                loadAndFillCompletedEmr(reservasi.getPasienId(), reservasi.getId());
            }
        } else {
            lblFormPasienNama.setText("Belum memilih pasien");
            lblFormKeluhan.setText("-");
            vboxFormFields.setDisable(true);
            btnSaveEmr.setDisable(true);
            txtDiagnosis.setText("");
            txtTindakan.setText("");
            txtResep.setText("");
            txtCatatan.setText("");
        }
    }

    private void loadAndFillCompletedEmr(Long pasienId, Long reservasiId) {
        new Thread(() -> {
            try {
                ApiResponse<List<RekamMedisResponse>> response = HttpClientUtil.getList(
                        "/api/dokter/rekam-medis/pasien/" + pasienId, 
                        RekamMedisResponse.class
                );
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        response.getData().stream()
                                .filter(emr -> emr.getReservasiId() != null && emr.getReservasiId().equals(reservasiId))
                                .findFirst()
                                .ifPresent(emr -> {
                                    txtDiagnosis.setText(emr.getDiagnosis());
                                    txtTindakan.setText(emr.getTindakan());
                                    txtResep.setText(emr.getResepObat());
                                    txtCatatan.setText(emr.getCatatan());
                                });
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void refreshQueue(ActionEvent event) {
        loadQueueData();
    }

    @FXML
    void handleSaveEmr(ActionEvent event) {
        if (selectedReservasi == null) return;

        String diagnosis = txtDiagnosis.getText().trim();
        String tindakan = txtTindakan.getText().trim();
        String resep = txtResep.getText().trim();
        String catatan = txtCatatan.getText().trim();

        if (diagnosis.isEmpty()) {
            lblEmrError.setText("Diagnosis wajib diisi!");
            return;
        }

        RekamMedisRequest request = new RekamMedisRequest();
        request.setReservasiId(selectedReservasi.getId());
        request.setKeluhan(selectedReservasi.getKeluhan()); // Complained is linked directly
        request.setDiagnosis(diagnosis);
        request.setTindakan(tindakan);
        request.setResepObat(resep);
        request.setCatatan(catatan);

        lblEmrError.setText("Menyimpan...");
        btnSaveEmr.setDisable(true);

        new Thread(() -> {
            try {
                ApiResponse<RekamMedisResponse> response = HttpClientUtil.post("/api/dokter/rekam-medis", request, RekamMedisResponse.class);
                
                Platform.runLater(() -> {
                    btnSaveEmr.setDisable(false);
                    if (response != null && response.isSuccess()) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Rekam medis pasien berhasil disimpan.");
                        selectQueueItem(null);
                        loadQueueData();
                    } else {
                        lblEmrError.setText(response != null ? response.getMessage() : "Gagal menyimpan rekam medis.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    btnSaveEmr.setDisable(false);
                    lblEmrError.setText("Error: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // === EMR Lookup / History Actions ===

    @FXML
    void handleSearchPasienSelect(ActionEvent event) {
        PasienItem selectedPasien = cbSearchPasien.getSelectionModel().getSelectedItem();
        medisHistoryList.clear();

        if (selectedPasien != null) {
            new Thread(() -> {
                try {
                    ApiResponse<List<RekamMedisResponse>> response = HttpClientUtil.getList(
                            "/api/dokter/rekam-medis/pasien/" + selectedPasien.getId(), 
                            RekamMedisResponse.class
                    );
                    Platform.runLater(() -> {
                        if (response != null && response.isSuccess() && response.getData() != null) {
                            medisHistoryList.addAll(response.getData());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // === Data Fetching ===

    private void loadQueueData() {
        new Thread(() -> {
            try {
                ApiResponse<List<ReservasiResponse>> response = HttpClientUtil.getList(
                        "/api/dokter/antrean?tanggal=" + LocalDate.now(), 
                        ReservasiResponse.class
                );
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        antreanList.clear();
                        antreanList.addAll(response.getData());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void populatePatientDropdown() {
        // Find unique patients in queue to let doctor lookup history
        List<PasienItem> patients = antreanList.stream()
                .map(r -> new PasienItem(r.getPasienId(), r.getNamaPasien()))
                .distinct()
                .collect(Collectors.toList());

        cbSearchPasien.setItems(FXCollections.observableArrayList(patients));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

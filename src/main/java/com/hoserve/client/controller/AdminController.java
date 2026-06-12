package com.hoserve.client.controller;

import com.hoserve.client.ClientApplication;
import com.hoserve.client.http.ClientSession;
import com.hoserve.client.http.HttpClientUtil;
import com.hoserve.dto.request.DokterRequest;
import com.hoserve.dto.request.PasienRequest;
import com.hoserve.dto.response.ApiResponse;
import com.hoserve.dto.response.DokterResponse;
import com.hoserve.dto.response.PasienResponse;
import com.hoserve.dto.response.PoliklinikResponse;
import com.hoserve.entity.Poliklinik;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for the Admin Dashboard UI.
 */
public class AdminController {

    // === Navigation Buttons ===
    @FXML private Button btnNavDokter;
    @FXML private Button btnNavPasien;
    @FXML private Button btnNavPoliklinik;
    @FXML private Label lblMenuTitle;
    @FXML private Label lblAdminName;

    // === Panels Stack ===
    @FXML private VBox panelDokter;
    @FXML private VBox panelPasien;
    @FXML private VBox panelPoliklinik;

    // ==================== DOKTER COMPONENTS ====================
    @FXML private TextField txtSearchDokter;
    @FXML private TableView<DokterResponse> tableDokter;
    @FXML private TableColumn<DokterResponse, String> colDokterNama;
    @FXML private TableColumn<DokterResponse, String> colDokterSpesialisasi;
    @FXML private TableColumn<DokterResponse, String> colDokterTelepon;
    @FXML private TableColumn<DokterResponse, String> colDokterPoliklinik;
    @FXML private TableColumn<DokterResponse, String> colDokterEmail;

    @FXML private Label lblFormDokterTitle;
    @FXML private TextField txtDokterNama;
    @FXML private TextField txtDokterEmail;
    @FXML private TextField txtDokterTelepon;
    @FXML private TextField txtDokterSpesialisasi;
    @FXML private ComboBox<PoliklinikResponse> cbDokterPoliklinik;
    @FXML private TextField txtDokterUsername;
    @FXML private PasswordField txtDokterPassword;
    @FXML private VBox vboxDokterPassword;
    @FXML private Label lblDokterError;
    @FXML private Button btnSaveDokter;
    @FXML private Button btnDeleteDokter;

    private DokterResponse selectedDokter;
    private final ObservableList<DokterResponse> dokterDataList = FXCollections.observableArrayList();

    // ==================== PASIEN COMPONENTS ====================
    @FXML private TextField txtSearchPasien;
    @FXML private TableView<PasienResponse> tablePasien;
    @FXML private TableColumn<PasienResponse, String> colPasienNama;
    @FXML private TableColumn<PasienResponse, LocalDate> colPasienLahir;
    @FXML private TableColumn<PasienResponse, String> colPasienTelepon;
    @FXML private TableColumn<PasienResponse, String> colPasienAlamat;
    @FXML private TableColumn<PasienResponse, String> colPasienEmail;

    @FXML private Label lblFormPasienTitle;
    @FXML private TextField txtPasienNama;
    @FXML private TextField txtPasienEmail;
    @FXML private TextField txtPasienTelepon;
    @FXML private DatePicker dpPasienLahir;
    @FXML private TextField txtPasienAlamat;
    @FXML private TextField txtPasienUsername;
    @FXML private Label lblPasienError;
    @FXML private Button btnSavePasien;
    @FXML private Button btnDeletePasien;

    private PasienResponse selectedPasien;
    private final ObservableList<PasienResponse> pasienDataList = FXCollections.observableArrayList();

    // ==================== POLIKLINIK COMPONENTS ====================
    @FXML private TextField txtSearchPoliklinik;
    @FXML private TableView<PoliklinikResponse> tablePoliklinik;
    @FXML private TableColumn<PoliklinikResponse, String> colPoliklinikNama;
    @FXML private TableColumn<PoliklinikResponse, String> colPoliklinikDeskripsi;
    @FXML private TableColumn<PoliklinikResponse, String> colPoliklinikLokasi;

    @FXML private Label lblFormPoliklinikTitle;
    @FXML private TextField txtPoliklinikNama;
    @FXML private TextField txtPoliklinikDeskripsi;
    @FXML private TextField txtPoliklinikLokasi;
    @FXML private Label lblPoliklinikError;
    @FXML private Button btnSavePoliklinik;
    @FXML private Button btnDeletePoliklinik;

    private PoliklinikResponse selectedPoliklinik;
    private final ObservableList<PoliklinikResponse> poliklinikDataList = FXCollections.observableArrayList();

    // === Initialize ===
    @FXML
    public void initialize() {
        lblAdminName.setText("Selamat bekerja, " + ClientSession.getInstance().getNama());

        // 1. Setup Dokter Table
        colDokterNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colDokterSpesialisasi.setCellValueFactory(new PropertyValueFactory<>("spesialisasi"));
        colDokterTelepon.setCellValueFactory(new PropertyValueFactory<>("nomorTelepon"));
        colDokterPoliklinik.setCellValueFactory(new PropertyValueFactory<>("namaPoliklinik"));
        colDokterEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        setupDokterFiltering();

        // 2. Setup Pasien Table
        colPasienNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colPasienLahir.setCellValueFactory(new PropertyValueFactory<>("tanggalLahir"));
        colPasienTelepon.setCellValueFactory(new PropertyValueFactory<>("nomorTelepon"));
        colPasienAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colPasienEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        setupPasienFiltering();

        // 3. Setup Poliklinik Table
        colPoliklinikNama.setCellValueFactory(new PropertyValueFactory<>("namaPoliklinik"));
        colPoliklinikDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        colPoliklinikLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        setupPoliklinikFiltering();

        // 4. Setup ComboBox converter for Poliklinik selection
        cbDokterPoliklinik.setConverter(new StringConverter<>() {
            @Override
            public String toString(PoliklinikResponse object) {
                return object != null ? object.getNamaPoliklinik() : "Pilih Poliklinik";
            }
            @Override
            public PoliklinikResponse fromString(String string) {
                return null;
            }
        });

        // 5. Table Selection Listeners
        tableDokter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectDokter(newVal));
        tablePasien.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectPasien(newVal));
        tablePoliklinik.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectPoliklinik(newVal));

        // 6. Load data from backend
        loadAllData();
    }

    // === Navigation Actions ===
    @FXML
    void showPanelDokter(ActionEvent event) {
        setMenuActive(btnNavDokter, "Manajemen Dokter");
        panelDokter.setVisible(true);
        panelPasien.setVisible(false);
        panelPoliklinik.setVisible(false);
    }

    @FXML
    void showPanelPasien(ActionEvent event) {
        setMenuActive(btnNavPasien, "Manajemen Pasien");
        panelDokter.setVisible(false);
        panelPasien.setVisible(true);
        panelPoliklinik.setVisible(false);
    }

    @FXML
    void showPanelPoliklinik(ActionEvent event) {
        setMenuActive(btnNavPoliklinik, "Manajemen Poliklinik");
        panelDokter.setVisible(false);
        panelPasien.setVisible(false);
        panelPoliklinik.setVisible(true);
    }

    private void setMenuActive(Button activeBtn, String title) {
        btnNavDokter.getStyleClass().remove("sidebar-btn-active");
        btnNavPasien.getStyleClass().remove("sidebar-btn-active");
        btnNavPoliklinik.getStyleClass().remove("sidebar-btn-active");
        activeBtn.getStyleClass().add("sidebar-btn-active");
        lblMenuTitle.setText(title);
    }

    @FXML
    void handleLogout(ActionEvent event) {
        ClientSession.getInstance().clear();
        ClientApplication.changeScene("/client/fxml/login.fxml", "HoServe - Clinic Management System", 850, 600);
    }

    // ==================== DOKTER LOGIC ====================

    private void setupDokterFiltering() {
        FilteredList<DokterResponse> filteredList = new FilteredList<>(dokterDataList, p -> true);
        txtSearchDokter.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(dokter -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String lowerFilter = newVal.toLowerCase();
                return dokter.getNama().toLowerCase().contains(lowerFilter) || 
                       dokter.getSpesialisasi().toLowerCase().contains(lowerFilter) ||
                       (dokter.getNamaPoliklinik() != null && dokter.getNamaPoliklinik().toLowerCase().contains(lowerFilter));
            });
        });
        SortedList<DokterResponse> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableDokter.comparatorProperty());
        tableDokter.setItems(sortedList);
    }

    @FXML
    void handleSearchDokter() {} // Handled via listener

    private void selectDokter(DokterResponse dokter) {
        this.selectedDokter = dokter;
        if (dokter != null) {
            lblFormDokterTitle.setText("EDIT DATA DOKTER");
            txtDokterNama.setText(dokter.getNama());
            txtDokterEmail.setText(dokter.getEmail());
            txtDokterTelepon.setText(dokter.getNomorTelepon());
            txtDokterSpesialisasi.setText(dokter.getSpesialisasi());
            txtDokterUsername.setText(dokter.getUsername());
            txtDokterUsername.setDisable(true);
            vboxDokterPassword.setVisible(true); // Password becomes optional for updates
            txtDokterPassword.setPromptText("(Kosongkan jika tidak diubah)");
            
            // Find and select corresponding Poliklinik in Combobox
            if (dokter.getPoliklinikId() != null) {
                cbDokterPoliklinik.getItems().stream()
                        .filter(p -> p.getId().equals(dokter.getPoliklinikId()))
                        .findFirst()
                        .ifPresent(p -> cbDokterPoliklinik.getSelectionModel().select(p));
            } else {
                cbDokterPoliklinik.getSelectionModel().clearSelection();
            }

            btnDeleteDokter.setVisible(true);
        } else {
            clearFormDokter(null);
        }
    }

    @FXML
    void clearFormDokter(ActionEvent event) {
        selectedDokter = null;
        lblFormDokterTitle.setText("TAMBAH DOKTER BARU");
        txtDokterNama.setText("");
        txtDokterEmail.setText("");
        txtDokterTelepon.setText("");
        txtDokterSpesialisasi.setText("");
        txtDokterUsername.setText("");
        txtDokterUsername.setDisable(false);
        txtDokterPassword.setText("");
        txtDokterPassword.setPromptText("Minimal 6 karakter");
        cbDokterPoliklinik.getSelectionModel().clearSelection();
        lblDokterError.setText("");
        btnDeleteDokter.setVisible(false);
        tableDokter.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSaveDokter(ActionEvent event) {
        String nama = txtDokterNama.getText().trim();
        String email = txtDokterEmail.getText().trim();
        String telepon = txtDokterTelepon.getText().trim();
        String spesialisasi = txtDokterSpesialisasi.getText().trim();
        PoliklinikResponse poliklinik = cbDokterPoliklinik.getSelectionModel().getSelectedItem();
        String username = txtDokterUsername.getText().trim();
        String password = txtDokterPassword.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || spesialisasi.isEmpty() || 
            username.isEmpty() || (selectedDokter == null && password.isEmpty())) {
            lblDokterError.setText("Semua field wajib diisi (kecuali password saat edit)!");
            return;
        }

        DokterRequest request = new DokterRequest();
        request.setNama(nama);
        request.setEmail(email);
        request.setNomorTelepon(telepon);
        request.setSpesialisasi(spesialisasi);
        request.setUsername(username);
        request.setPassword(password.isEmpty() ? null : password);
        request.setPoliklinikId(poliklinik != null ? poliklinik.getId() : null);

        lblDokterError.setText("Menyimpan...");
        new Thread(() -> {
            try {
                ApiResponse<DokterResponse> response;
                if (selectedDokter == null) {
                    // Create
                    response = HttpClientUtil.post("/api/admin/dokter", request, DokterResponse.class);
                } else {
                    // Update
                    response = HttpClientUtil.put("/api/admin/dokter/" + selectedDokter.getId(), request, DokterResponse.class);
                }

                Platform.runLater(() -> {
                    if (response != null && response.isSuccess()) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data dokter berhasil disimpan.");
                        clearFormDokter(null);
                        loadDokterData();
                    } else {
                        lblDokterError.setText(response != null ? response.getMessage() : "Gagal menyimpan data.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblDokterError.setText("Error: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleDeleteDokter(ActionEvent event) {
        if (selectedDokter == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Dokter: " + selectedDokter.getNama());
        alert.setContentText("Apakah Anda yakin ingin menghapus data dokter ini? Aksi ini tidak dapat dibatalkan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    ApiResponse<Void> response = HttpClientUtil.delete("/api/admin/dokter/" + selectedDokter.getId(), Void.class);
                    Platform.runLater(() -> {
                        if (response != null && response.isSuccess()) {
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data dokter berhasil dihapus.");
                            clearFormDokter(null);
                            loadDokterData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", response != null ? response.getMessage() : "Gagal menghapus dokter.");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Koneksi terputus: " + e.getMessage()));
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // ==================== PASIEN LOGIC ====================

    private void setupPasienFiltering() {
        FilteredList<PasienResponse> filteredList = new FilteredList<>(pasienDataList, p -> true);
        txtSearchPasien.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(pasien -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String lowerFilter = newVal.toLowerCase();
                return pasien.getNama().toLowerCase().contains(lowerFilter) || 
                       pasien.getEmail().toLowerCase().contains(lowerFilter) ||
                       pasien.getNomorTelepon().contains(lowerFilter);
            });
        });
        SortedList<PasienResponse> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tablePasien.comparatorProperty());
        tablePasien.setItems(sortedList);
    }

    @FXML
    void handleSearchPasien() {}

    private void selectPasien(PasienResponse pasien) {
        this.selectedPasien = pasien;
        if (pasien != null) {
            lblFormPasienTitle.setText("EDIT DATA PASIEN");
            txtPasienNama.setText(pasien.getNama());
            txtPasienEmail.setText(pasien.getEmail());
            txtPasienTelepon.setText(pasien.getNomorTelepon());
            dpPasienLahir.setValue(pasien.getTanggalLahir());
            txtPasienAlamat.setText(pasien.getAlamat());
            txtPasienUsername.setText(pasien.getUsername());
            txtPasienUsername.setDisable(true);
            btnDeletePasien.setVisible(true);
        } else {
            clearFormPasien(null);
        }
    }

    @FXML
    void clearFormPasien(ActionEvent event) {
        selectedPasien = null;
        lblFormPasienTitle.setText("PILIH PASIEN UNTUK EDIT");
        txtPasienNama.setText("");
        txtPasienEmail.setText("");
        txtPasienTelepon.setText("");
        dpPasienLahir.setValue(null);
        txtPasienAlamat.setText("");
        txtPasienUsername.setText("");
        txtPasienUsername.setDisable(true);
        lblPasienError.setText("");
        btnDeletePasien.setVisible(false);
        tablePasien.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSavePasien(ActionEvent event) {
        if (selectedPasien == null) return;

        String nama = txtPasienNama.getText().trim();
        String email = txtPasienEmail.getText().trim();
        String telepon = txtPasienTelepon.getText().trim();
        LocalDate lahir = dpPasienLahir.getValue();
        String alamat = txtPasienAlamat.getText().trim();

        if (nama.isEmpty() || email.isEmpty() || telepon.isEmpty() || lahir == null || alamat.isEmpty()) {
            lblPasienError.setText("Semua field wajib diisi!");
            return;
        }

        PasienRequest request = new PasienRequest();
        request.setNama(nama);
        request.setEmail(email);
        request.setNomorTelepon(telepon);
        request.setTanggalLahir(lahir);
        request.setAlamat(alamat);

        lblPasienError.setText("Menyimpan...");
        new Thread(() -> {
            try {
                ApiResponse<PasienResponse> response = HttpClientUtil.put("/api/admin/pasien/" + selectedPasien.getId(), request, PasienResponse.class);

                Platform.runLater(() -> {
                    if (response != null && response.isSuccess()) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data pasien berhasil diupdate.");
                        clearFormPasien(null);
                        loadPasienData();
                    } else {
                        lblPasienError.setText(response != null ? response.getMessage() : "Gagal mengupdate data.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblPasienError.setText("Error: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleDeletePasien(ActionEvent event) {
        if (selectedPasien == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Pasien: " + selectedPasien.getNama());
        alert.setContentText("Apakah Anda yakin ingin menghapus data pasien ini? Aksi ini akan menghapus semua riwayat reservasi & medis pasien.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    ApiResponse<Void> response = HttpClientUtil.delete("/api/admin/pasien/" + selectedPasien.getId(), Void.class);
                    Platform.runLater(() -> {
                        if (response != null && response.isSuccess()) {
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data pasien berhasil dihapus.");
                            clearFormPasien(null);
                            loadPasienData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", response != null ? response.getMessage() : "Gagal menghapus pasien.");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Koneksi terputus: " + e.getMessage()));
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // ==================== POLIKLINIK LOGIC ====================

    private void setupPoliklinikFiltering() {
        FilteredList<PoliklinikResponse> filteredList = new FilteredList<>(poliklinikDataList, p -> true);
        txtSearchPoliklinik.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(poli -> {
                if (newVal == null || newVal.trim().isEmpty()) return true;
                String lowerFilter = newVal.toLowerCase();
                return poli.getNamaPoliklinik().toLowerCase().contains(lowerFilter) || 
                       poli.getLokasi().toLowerCase().contains(lowerFilter);
            });
        });
        SortedList<PoliklinikResponse> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tablePoliklinik.comparatorProperty());
        tablePoliklinik.setItems(sortedList);
    }

    @FXML
    void handleSearchPoliklinik() {}

    private void selectPoliklinik(PoliklinikResponse poliklinik) {
        this.selectedPoliklinik = poliklinik;
        if (poliklinik != null) {
            lblFormPoliklinikTitle.setText("EDIT POLIKLINIK");
            txtPoliklinikNama.setText(poliklinik.getNamaPoliklinik());
            txtPoliklinikDeskripsi.setText(poliklinik.getDeskripsi());
            txtPoliklinikLokasi.setText(poliklinik.getLokasi());
            btnDeletePoliklinik.setVisible(true);
        } else {
            clearFormPoliklinik(null);
        }
    }

    @FXML
    void clearFormPoliklinik(ActionEvent event) {
        selectedPoliklinik = null;
        lblFormPoliklinikTitle.setText("TAMBAH POLIKLINIK BARU");
        txtPoliklinikNama.setText("");
        txtPoliklinikDeskripsi.setText("");
        txtPoliklinikLokasi.setText("");
        lblPoliklinikError.setText("");
        btnDeletePoliklinik.setVisible(false);
        tablePoliklinik.getSelectionModel().clearSelection();
    }

    @FXML
    void handleSavePoliklinik(ActionEvent event) {
        String nama = txtPoliklinikNama.getText().trim();
        String deskripsi = txtPoliklinikDeskripsi.getText().trim();
        String lokasi = txtPoliklinikLokasi.getText().trim();

        if (nama.isEmpty() || lokasi.isEmpty()) {
            lblPoliklinikError.setText("Nama dan Lokasi Poliklinik wajib diisi!");
            return;
        }

        // The request body requires a Poliklinik entity schema in the backend controller
        Poliklinik request = new Poliklinik();
        request.setNamaPoliklinik(nama);
        request.setDeskripsi(deskripsi);
        request.setLokasi(lokasi);

        lblPoliklinikError.setText("Menyimpan...");
        new Thread(() -> {
            try {
                ApiResponse<PoliklinikResponse> response;
                if (selectedPoliklinik == null) {
                    response = HttpClientUtil.post("/api/admin/poliklinik", request, PoliklinikResponse.class);
                } else {
                    response = HttpClientUtil.put("/api/admin/poliklinik/" + selectedPoliklinik.getId(), request, PoliklinikResponse.class);
                }

                Platform.runLater(() -> {
                    if (response != null && response.isSuccess()) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data poliklinik berhasil disimpan.");
                        clearFormPoliklinik(null);
                        loadPoliklinikData();
                    } else {
                        lblPoliklinikError.setText(response != null ? response.getMessage() : "Gagal menyimpan poliklinik.");
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> lblPoliklinikError.setText("Error: " + e.getMessage()));
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    void handleDeletePoliklinik(ActionEvent event) {
        if (selectedPoliklinik == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Poliklinik: " + selectedPoliklinik.getNamaPoliklinik());
        alert.setContentText("Apakah Anda yakin ingin menghapus poliklinik ini? Aksi ini akan melepaskan dokter yang bertugas di poliklinik ini.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            new Thread(() -> {
                try {
                    ApiResponse<Void> response = HttpClientUtil.delete("/api/admin/poliklinik/" + selectedPoliklinik.getId(), Void.class);
                    Platform.runLater(() -> {
                        if (response != null && response.isSuccess()) {
                            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Poliklinik berhasil dihapus.");
                            clearFormPoliklinik(null);
                            loadPoliklinikData();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", response != null ? response.getMessage() : "Gagal menghapus poliklinik.");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Koneksi terputus: " + e.getMessage()));
                    e.printStackTrace();
                }
            }).start();
        }
    }

    // ==================== DATA FETCHING ====================

    private void loadAllData() {
        loadPoliklinikData(); // load this first since cbDokterPoliklinik depends on it
        loadDokterData();
        loadPasienData();
    }

    private void loadDokterData() {
        new Thread(() -> {
            try {
                ApiResponse<List<DokterResponse>> response = HttpClientUtil.getList("/api/admin/dokter", DokterResponse.class);
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        dokterDataList.clear();
                        dokterDataList.addAll(response.getData());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadPasienData() {
        new Thread(() -> {
            try {
                ApiResponse<List<PasienResponse>> response = HttpClientUtil.getList("/api/admin/pasien", PasienResponse.class);
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        pasienDataList.clear();
                        pasienDataList.addAll(response.getData());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadPoliklinikData() {
        new Thread(() -> {
            try {
                ApiResponse<List<PoliklinikResponse>> response = HttpClientUtil.getList("/api/admin/poliklinik", PoliklinikResponse.class);
                Platform.runLater(() -> {
                    if (response != null && response.isSuccess() && response.getData() != null) {
                        poliklinikDataList.clear();
                        poliklinikDataList.addAll(response.getData());
                        
                        // Populate doctor form ComboBox
                        cbDokterPoliklinik.setItems(poliklinikDataList);
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

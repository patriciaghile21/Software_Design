package org.example.eurovision_manager.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;
import org.example.eurovision_manager.export.ExportStrategy;
import org.example.eurovision_manager.export.CsvExportStrategy;
import org.example.eurovision_manager.export.JsonExportStrategy;
import org.example.eurovision_manager.export.XmlExportStrategy;
import org.example.eurovision_manager.observer.NotificationService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EntryController {

    @FXML private TableView<Entry> entriesTable;
    @FXML private TableColumn<Entry, String> countryColumn;
    @FXML private TableColumn<Entry, String> artistColumn;
    @FXML private TableColumn<Entry, String> titleColumn;
    @FXML private TableColumn<Entry, Integer> yearColumn;
    @FXML private TextField sortField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private ComboBox<String> exportComboBox;

    @FXML private Button addEntryButton;
    @FXML private Button editEntryButton;
    @FXML private Button deleteEntryButton;
    @FXML private Button viewEntriesButton;
    @FXML private Button backEntryButton;

    private final EntryService entryService = EntryService.getInstance();
    private User currentUser;

    @FXML
    public void initialize() {
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));

        sortComboBox.getItems().addAll(
                "Country (A-Z)", "Country (Z-A)",
                "Artist (A-Z)", "Artist (Z-A)",
                "Year (Ascending)", "Year (Descending)"
        );

        if (exportComboBox != null) {
            exportComboBox.getItems().addAll("Export data as CSV", "Export data as JSON", "Export data as XML");
            exportComboBox.getSelectionModel().selectFirst();
        }

        entryService.addObserver(new NotificationService());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;

        boolean isStaff = user.getRole().equals("ADMIN") || user.getRole().equals("HOD");
        addEntryButton.setVisible(isStaff);
        editEntryButton.setVisible(isStaff);
        deleteEntryButton.setVisible(isStaff);
    }

    @FXML
    protected void onViewEntriesButton() {
        List<Entry> entries = entryService.getAllEntries();
        entriesTable.setItems(FXCollections.observableArrayList(entries));
    }

    @FXML
    protected void onSearchAction() {
        String keyword = sortField.getText();
        if (keyword != null && !keyword.trim().isEmpty()) {
            List<Entry> results = entryService.searchEntries(keyword);
            entriesTable.setItems(FXCollections.observableArrayList(results));
        } else {
            onViewEntriesButton();
        }
    }

    @FXML
    protected void onSortAction() {
        String selected = sortComboBox.getValue();
        if (selected == null) return;
        String dbColumn = "id";
        boolean ascending = true;
        switch (selected) {
            case "Country (A-Z)": dbColumn = "country"; ascending = true; break;
            case "Country (Z-A)": dbColumn = "country"; ascending = false; break;
            case "Artist (A-Z)": dbColumn = "artist"; ascending = true; break;
            case "Artist (Z-A)": dbColumn = "artist"; ascending = false; break;
            case "Year (Ascending)": dbColumn = "release_year"; ascending = true; break;
            case "Year (Descending)": dbColumn = "release_year"; ascending = false; break;
        }
        List<Entry> sortedList = entryService.getSortedEntries(dbColumn, ascending);
        entriesTable.setItems(FXCollections.observableArrayList(sortedList));
    }

    @FXML
    protected void onAddEntryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eurovision_manager/view/addEntry.fxml"));
            Scene scene = new Scene(loader.load());
            AddEntryController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            Stage stage = new Stage();
            stage.setTitle("Add New Entry");
            stage.setScene(scene);
            stage.showAndWait();
            onViewEntriesButton();
        } catch (Exception e) {
            showError("Could not load Add window: " + e.getMessage());
        }
    }

    @FXML
    protected void onEditEntryButton() {
        Entry selectedEntry = entriesTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eurovision_manager/view/editEntry.fxml"));
                Scene scene = new Scene(loader.load());
                EditEntryController controller = loader.getController();
                controller.initData(selectedEntry, currentUser);
                Stage stage = new Stage();
                stage.setTitle("Edit Entry");
                stage.setScene(scene);
                stage.showAndWait();
                onViewEntriesButton();
            } catch (Exception e) {
                showError("Could not load Edit window: " + e.getMessage());
            }
        } else {
            showError("Please select an entry to edit!");
        }
    }

    @FXML
    protected void onDeleteEntryButton() {
        Entry selectedEntry = entriesTable.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            try {
                entryService.deleteEntry(selectedEntry.getId(), currentUser);
                showInfo("Success", "Entry deleted successfully!");
                onViewEntriesButton();
            } catch (RuntimeException e) {
                showError(e.getMessage());
            }
        } else {
            showError("Please select an entry to delete!");
        }
    }

    @FXML
    protected void onBackEntryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eurovision_manager/view/loginESC.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Eurovision Manager - Login");
            stage.show();

            Stage currentStage = (Stage) entriesTable.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            showError("Back button failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleExport() {
        if (exportComboBox == null || exportComboBox.getValue() == null) {
            showError("Please select an export format first!");
            return;
        }

        String selectedFormat = exportComboBox.getValue();
        List<Entry> entriesToExport = new ArrayList<>(entriesTable.getItems());

        if (entriesToExport.isEmpty()) {
            showError("There are no entries to export!");
            return;
        }

        String extension = "";
        ExportStrategy strategy = null;

        switch (selectedFormat) {
            case "Export data as CSV":
                strategy = new CsvExportStrategy();
                extension = "csv";
                break;
            case "Export data as JSON":
                strategy = new JsonExportStrategy();
                extension = "json";
                break;
            case "Export data as XML":
                strategy = new XmlExportStrategy();
                extension = "xml";
                break;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Exported Data");
        fileChooser.setInitialFileName("eurovision_data." + extension);

        Stage currentStage = (Stage) entriesTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(currentStage);

        if (file != null && strategy != null) {
            strategy.exportData(entriesToExport, file.getAbsolutePath());
            showInfo("Export Successful", "Data exported successfully to: " + file.getAbsolutePath());
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Action Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
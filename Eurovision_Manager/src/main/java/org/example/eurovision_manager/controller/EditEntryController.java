package org.example.eurovision_manager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;

public class EditEntryController {

    @FXML
    private TextField countryField1;
    @FXML
    private TextField artistField1;
    @FXML
    private TextField titleField1;
    @FXML
    private TextField yearField1;
    @FXML
    private Button editButton;

    private final EntryService entryService = new EntryService();
    private Entry currentEntry;
    private User currentUser;

    public void initData(Entry entry, User user) {
        this.currentEntry = entry;
        this.currentUser = user;

        countryField1.setText(entry.getCountry());
        artistField1.setText(entry.getArtist());
        titleField1.setText(entry.getTitle());
        yearField1.setText(String.valueOf(entry.getReleaseYear()));
    }

    @FXML
    protected void onEditButton() {
        try {
            String country = countryField1.getText();
            String artist = artistField1.getText();
            String title = titleField1.getText();

            if (country.isEmpty() || artist.isEmpty() || title.isEmpty() || yearField1.getText().isEmpty()) {
                showError("All fields are required.");
                return;
            }

            int year = Integer.parseInt(yearField1.getText());

            currentEntry.setCountry(country);
            currentEntry.setArtist(artist);
            currentEntry.setTitle(title);
            currentEntry.setReleaseYear(year);

            entryService.updateEntry(currentEntry, currentUser);

            closeWindow();
        } catch (NumberFormatException e) {
            showError("Year must be a valid number.");
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
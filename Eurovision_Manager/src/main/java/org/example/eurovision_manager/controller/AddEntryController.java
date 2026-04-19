package org.example.eurovision_manager.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.eurovision_manager.model.entity.Entry;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.EntryService;

public class AddEntryController {

    @FXML
    private TextField countryField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField yearField;
    @FXML
    private Button addButton;

    private final EntryService entryService = EntryService.getInstance();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    protected void onAddButton() {
        try {
            String country = countryField.getText();
            String artist = artistField.getText();
            String title = titleField.getText();

            if (country.isEmpty() || artist.isEmpty() || title.isEmpty() || yearField.getText().isEmpty()) {
                showError("All fields are required.");
                return;
            }

            int year = Integer.parseInt(yearField.getText());

            Entry newEntry = new Entry(country, artist, title, year);
            entryService.createEntry(newEntry, currentUser);

            closeWindow();
        } catch (NumberFormatException e) {
            showError("Year must be a valid number.");
        } catch (RuntimeException e) {
            showError(e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addButton.getScene().getWindow();
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
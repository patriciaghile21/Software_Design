package org.example.eurovision_manager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.eurovision_manager.model.entity.User;
import org.example.eurovision_manager.model.service.UserService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password cannot be empty!");
            return;
        }

        try {
            User loggedInUser = userService.login(username, password);

            if (loggedInUser != null) {
                showInfo("Login Successful", "Welcome, " + loggedInUser.getUsername() + " (" + loggedInUser.getRole() + ")");
                navigateToMain(loggedInUser);
            } else {
                showError("Invalid username or password. Please try again.");
            }
        } catch (Exception e) {
            showError("Database connection error: " + e.getMessage());
        }
    }

    private void navigateToMain(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/eurovision_manager/view/entry.fxml"));
            Scene scene = new Scene(loader.load());

            EntryController entryController = loader.getController();
            entryController.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle("Eurovision Catalog - " + user.getRole());
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            showError("Could not open catalog: " + e.getMessage());
            e.printStackTrace();
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
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
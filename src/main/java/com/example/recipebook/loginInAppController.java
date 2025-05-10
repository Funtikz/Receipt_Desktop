package com.example.recipebook;

import com.example.recipebook.bd.Session;
import com.example.recipebook.bd.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class loginInAppController {

    @FXML
    private TextField inputEmail;

    @FXML
    private PasswordField inputPassword;

    @FXML
    private Button buttonСontinue;

    @FXML
    private Button registrationButton;

    @FXML
    void initialize() {
    }

    @FXML
    private void onButtonContinueClicked(ActionEvent event) {
        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля.");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            int userId = userDAO.loginUser(email, password);

            if (userId != -1) {
                // Сохраняем userId в сессии
                Session.setUserId(userId);

                // Успешный вход, переходим на главное окно
                loadMainScreen(event);
            } else {
                showAlert("Ошибка", "Неверный email или пароль.");
            }

        } catch (SQLException e) {
            showAlert("Ошибка", "Ошибка при авторизации: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onRegistrationButtonClicked(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registrationInApp.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void loadMainScreen(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

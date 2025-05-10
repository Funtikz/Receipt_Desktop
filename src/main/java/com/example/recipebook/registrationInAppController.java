package com.example.recipebook;

import com.example.recipebook.bd.UserDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class registrationInAppController {

    @FXML
    private TextField inputEmail1; // имя

    @FXML
    private TextField inputEmail; // email

    @FXML
    private PasswordField inputPassword;

    @FXML
    private void initialize() {
    }

    @FXML
    private void onButtonContinueClicked() {
        String name = inputEmail1.getText();
        String email = inputEmail.getText();
        String password = inputPassword.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Ошибка", "Пожалуйста, заполните все поля.");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();

            if (userDAO.isEmailTaken(email)) {
                showAlert("Ошибка", "Такой email уже используется.");
                return;
            }

            userDAO.registerUser(name, email, password);
            showAlert("Успех", "Регистрация прошла успешно!");

            // TODO: переключение на экран входа или главную

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Ошибка", "Не удалось зарегистрироваться: " + e.getMessage());
        }
    }

    @FXML
    private void onLoginButtonClicked(ActionEvent event) throws IOException {
        // Замените путь на "LoginInApp.fxml"
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("loginInApp.fxml"));
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

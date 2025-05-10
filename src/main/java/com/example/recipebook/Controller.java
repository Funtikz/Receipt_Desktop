package com.example.recipebook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private Button loginButton;

    @FXML
    private Button registrationButton;

    @FXML
    public void initialize() {
        // Назначение событий можно делать либо здесь, либо прямо в FXML через onAction
        loginButton.setOnAction(this::handleLogin);
        registrationButton.setOnAction(this::handleRegistration);
    }

    private void handleLogin(ActionEvent event) {
        switchScene("loginInApp.fxml");
    }

    private void handleRegistration(ActionEvent event) {
        switchScene("registrationInApp.fxml");
    }

    private void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

            // Получаем текущую сцену через кнопку и меняем
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

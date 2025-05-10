package com.example.recipebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class firstRecipeDarkController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editDarkButton;

    @FXML
    private Button mainMenuDarkButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    void get(MouseEvent event) {

    }

    @FXML
    void initialize() {
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'firstRecipeDark.fxml'.";
        assert editDarkButton != null : "fx:id=\"editDarkButton\" was not injected: check your FXML file 'firstRecipeDark.fxml'.";
        assert mainMenuDarkButton != null : "fx:id=\"mainMenuDarkButton\" was not injected: check your FXML file 'firstRecipeDark.fxml'.";
        assert recipeFoodButton != null : "fx:id=\"recipeFoodButton\" was not injected: check your FXML file 'firstRecipeDark.fxml'.";

    }

}


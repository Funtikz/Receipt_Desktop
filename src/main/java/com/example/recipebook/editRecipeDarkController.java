package com.example.recipebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class editRecipeDarkController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private RadioButton breakfast;

    @FXML
    private ToggleGroup category;

    @FXML
    private RadioButton dessert;

    @FXML
    private RadioButton dinner;

    @FXML
    private RadioButton lunch;

    @FXML
    private Button mainMenuButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    private RadioButton salad;

    @FXML
    private Button saveButton;

    @FXML
    void get(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }

}

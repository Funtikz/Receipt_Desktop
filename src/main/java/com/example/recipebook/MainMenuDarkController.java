package com.example.recipebook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainMenuDarkController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane addreceptButton;

    @FXML
    private RadioButton breakfast;

    @FXML
    private ToggleGroup category;

    @FXML
    private RadioButton darkTheme;

    @FXML
    private RadioButton dessert;

    @FXML
    private RadioButton dinner;

    @FXML
    private Button favoriteButton;

    @FXML
    private Button finallyButton;

    @FXML
    private Button finallyButton1;

    @FXML
    private ChoiceBox<String> fontSelection;
    @FXML
    private RadioButton lunch;

    @FXML
    private Button mainMenuButton;

    @FXML
    private AnchorPane recipeFoodButton;

    @FXML
    private AnchorPane recipeFoodButton1;

    @FXML
    private AnchorPane recipeFoodButton2;

    @FXML
    private AnchorPane recipeFoodButton21;

    @FXML
    private RadioButton salad;

    @FXML
    private ToggleGroup theme;

    @FXML
    private RadioButton whiteTheme;

    @FXML
    void get(MouseEvent event) {

    }

    @FXML
    void getFontSelection(MouseEvent event) {

    }

    @FXML
    void initialize() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontSelection.getItems().addAll("System", "TimesNewRoman");
    }
}

